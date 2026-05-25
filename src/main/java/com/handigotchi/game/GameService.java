package com.handigotchi.game;

import com.handigotchi.model.GameStatusDto;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.handigotchi.game.GameConstants.CLEAN_HAPPY_GAIN;
import static com.handigotchi.game.GameConstants.EGG_HATCH_CLICKS;
import static com.handigotchi.game.GameConstants.GAME_TICK_SEC;
import static com.handigotchi.game.GameConstants.MEALS;
import static com.handigotchi.game.GameConstants.MED_STAT_RESTORE;
import static com.handigotchi.game.GameConstants.PLAY_ENERGY_COST;
import static com.handigotchi.game.GameConstants.PLAY_HAPPY_GAIN;
import static com.handigotchi.game.GameConstants.PLAY_MIN_ENERGY;
import static com.handigotchi.game.GameConstants.POOP_HAPPY_PENALTY_PER;
import static com.handigotchi.game.GameConstants.SICK_DEATH_SEC;
import static com.handigotchi.game.GameConstants.SLEEP_ENERGY_REGEN;
import static com.handigotchi.game.GameConstants.SNACKS;
import static com.handigotchi.game.GameConstants.STAT_DECAY_PER_TICK;
import static com.handigotchi.game.GameConstants.SURVIVAL_ADULT_TO_ENDING_SEC;
import static com.handigotchi.game.GameConstants.SURVIVAL_BABY_TO_TEEN_SEC;
import static com.handigotchi.game.GameConstants.SURVIVAL_TEEN_TO_ADULT_SEC;

@Service
public class GameService {

    private final GameState state = new GameState();

    public synchronized GameStatusDto getStatus() {
        return toDto(null);
    }

    public synchronized GameStatusDto reset() {
        return restartInternal();
    }

    public synchronized GameStatusDto restart() {
        return restartInternal();
    }

    private synchronized GameStatusDto restartInternal() {
        state.setStage("egg");
        state.setHunger(100);
        state.setHappy(100);
        state.setEnergy(100);
        state.setSleeping(false);
        state.setSick(false);
        state.setDead(false);
        state.setEnded(false);
        state.setSurvivalSeconds(0);
        state.setLifetimeSickCount(0);
        state.setEndingActive(false);
        state.setEndingType(null);
        state.setEndlessMode(false);
        state.setSickSeconds(0);
        state.setEggClickCount(0);
        state.resetSnackStreaks();
        state.setPoopCount(0);
        state.scheduleNextPoop();
        state.setPetType(null);
        return toDto(null);
    }

    public synchronized GameStatusDto selectCharacter(String type) {
        if (!"h".equals(type) && !"d".equals(type)) {
            return toDto("Invalid character type");
        }
        state.setPetType(type);
        return toDto(null);
    }

    public synchronized GameStatusDto eggClick() {
        if (!"egg".equals(state.getStage()) || state.isEnded()) {
            return toDto(null);
        }
        state.setEggClickCount(state.getEggClickCount() + 1);
        if (state.getEggClickCount() >= EGG_HATCH_CLICKS) {
            evolveTo("baby");
        }
        return toDto(null);
    }

    public synchronized GameStatusDto feedMeal(String mealId) {
        if (cannotInteract() || "egg".equals(state.getStage())) {
            return toDto(null);
        }
        GameConstants.MealDef meal = MEALS.get(mealId);
        if (meal == null) {
            return toDto("Unknown meal");
        }
        if (state.isSleeping()) {
            state.setSleeping(false);
        }
        state.resetSnackStreaks();
        state.setHunger(clamp(state.getHunger() + meal.hunger()));
        state.setEnergy(clamp(state.getEnergy() + meal.energy()));
        checkStatCollapse();
        return toDto(null);
    }

    public synchronized GameStatusDto feedSnack(String snackId) {
        if (cannotInteract() || "egg".equals(state.getStage())) {
            return toDto(null);
        }
        GameConstants.SnackDef snack = SNACKS.get(snackId);
        if (snack == null) {
            return toDto("Unknown snack");
        }
        if (state.isSleeping()) {
            state.setSleeping(false);
        }
        state.setHappy(clamp(state.getHappy() + snack.happy()));
        applySugarStreak(snackId, snack.overload());
        checkStatCollapse();
        return toDto(null);
    }

    public synchronized GameStatusDto play() {
        if (cannotInteract() || "egg".equals(state.getStage()) || state.isSick()) {
            return toDto(null);
        }
        if (state.isSleeping()) {
            state.setSleeping(false);
        }
        if (state.getEnergy() < PLAY_MIN_ENERGY) {
            return toDto("Too tired... 💤");
        }
        state.setHappy(clamp(state.getHappy() + PLAY_HAPPY_GAIN));
        if (!state.isEndlessMode()) {
            state.setEnergy(clamp(state.getEnergy() - PLAY_ENERGY_COST));
        }
        checkStatCollapse();
        return toDto(null);
    }

    public synchronized GameStatusDto toggleSleep() {
        if (state.isEnded() || state.isDead() || "egg".equals(state.getStage())
                || "baby".equals(state.getStage()) || state.isSick()) {
            return toDto(null);
        }
        if (state.isSleeping()) {
            state.setSleeping(false);
        } else {
            state.setSleeping(true);
        }
        return toDto(null);
    }

    public synchronized GameStatusDto cureSick() {
        if (!state.isSick()) {
            return toDto(null);
        }
        state.setSick(false);
        state.setSickSeconds(0);
        state.setHunger(clamp(state.getHunger() + MED_STAT_RESTORE));
        state.setHappy(clamp(state.getHappy() + MED_STAT_RESTORE));
        state.setEnergy(clamp(state.getEnergy() + MED_STAT_RESTORE));
        return toDto(null);
    }

    public synchronized GameStatusDto cleanPoop() {
        if (state.getPoopCount() <= 0) {
            return toDto(null);
        }
        state.setPoopCount(state.getPoopCount() - 1);
        state.setHappy(clamp(state.getHappy() + CLEAN_HAPPY_GAIN));
        return toDto(null);
    }

    public synchronized GameStatusDto continueAfterHappyEnding() {
        if (!"happy".equals(state.getEndingType()) || !state.isEndingActive()) {
            return toDto(null);
        }
        state.setEndingActive(false);
        state.setEndlessMode(true);
        return toDto(null);
    }

    public synchronized GameStatusDto restartFromHappyEnding() {
        return restart();
    }

    @Scheduled(fixedRate = GameConstants.GAME_TICK_MS)
    public synchronized void processTick() {
        if (state.isEnded() || state.isDead()) {
            return;
        }
        if ("egg".equals(state.getStage())) {
            return;
        }
        if (state.isEndlessMode()) {
            return;
        }

        // === 宝贝，把这三行加在这里！===
        // 如果结局已经触发，直接停止时间（不拉粑粑，不掉数值，不会生病）
        if (state.isEndingActive()) {
            return; 
        }
        // ============================

        trySpawnPoop();

        if (state.isSick()) {
            state.setSickSeconds(state.getSickSeconds() + GAME_TICK_SEC);
            if (state.getSickSeconds() >= SICK_DEATH_SEC) {
                triggerDeath();
            }
            return;
        }

        if (state.isSleeping() && !"baby".equals(state.getStage())) {
            state.setEnergy(clamp(state.getEnergy() + SLEEP_ENERGY_REGEN));
        } else {
            int happyDecay = STAT_DECAY_PER_TICK + state.getPoopCount() * POOP_HAPPY_PENALTY_PER;
            state.setHunger(clamp(state.getHunger() - STAT_DECAY_PER_TICK));
            state.setHappy(clamp(state.getHappy() - happyDecay));
            state.setEnergy(clamp(state.getEnergy() - STAT_DECAY_PER_TICK));
            checkStatCollapse();
        }

        state.setSurvivalSeconds(state.getSurvivalSeconds() + GAME_TICK_SEC);
        checkEvolution();
    }

    private void checkEvolution() {
        if (state.isEndlessMode() || state.isEndingActive()) {
            return;
        }
        double survival = state.getSurvivalSeconds();
        String stage = state.getStage();

        if ("baby".equals(stage)) {
            if (survival >= SURVIVAL_BABY_TO_TEEN_SEC) {
                evolveTo("teen");
            }
            return;
        }
        if ("teen".equals(stage)) {
            if (survival >= SURVIVAL_TEEN_TO_ADULT_SEC) {
                evolveTo("adult");
            }
            return;
        }
        if ("adult".equals(stage) && survival >= SURVIVAL_ADULT_TO_ENDING_SEC) {
            triggerEnding();
        }
    }

    private void trySpawnPoop() {
        if (state.isEndlessMode()) {
            return;
        }
        if (state.isDead() || state.isEnded() || "egg".equals(state.getStage()) || state.isSleeping()) {
            return;
        }
        if (System.currentTimeMillis() < state.getNextPoopAt()) {
            return;
        }
        if (state.getPoopCount() < 5) {
            state.setPoopCount(state.getPoopCount() + 1);
        }
        state.scheduleNextPoop();
    }

    private boolean cannotInteract() {
        return state.isEnded() || state.isDead()
                || (state.isEndingActive() && !state.isEndlessMode());
    }

    private void applySugarStreak(String snackId, int overload) {
        Map<String, Integer> streak = state.getSnackStreak();
        int count = streak.getOrDefault(snackId, 0) + 1;
        streak.put(snackId, count);
        if (count >= overload) {
            triggerSick();
        }
    }

    private void checkStatCollapse() {
        if (state.isEndlessMode()) {
            return;
        }
        if ("egg".equals(state.getStage()) || "baby".equals(state.getStage())
                || state.isDead() || state.isSick()) {
            return;
        }
        if (state.getHunger() <= 0 || state.getHappy() <= 0 || state.getEnergy() <= 0) {
            triggerSick();
        }
    }

    private void evolveTo(String nextStage) {
        state.setStage(nextStage);
        // Reset survival so teen/adult thresholds never carry over (e.g. 120s as teen → instant ending as adult).
        state.setSurvivalSeconds(0);

        if ("baby".equals(nextStage)) {
            state.setHunger(100);
            state.setHappy(100);
            state.setEnergy(100);
        }
    }

    private void triggerSick() {
        if (state.isSick() || state.isDead() || "baby".equals(state.getStage())
                || "egg".equals(state.getStage()) || state.isEndlessMode()) {
            return;
        }
        state.setSick(true);
        state.setSickSeconds(0);
        state.setLifetimeSickCount(state.getLifetimeSickCount() + 1);
        state.setSleeping(false);
    }

    private void triggerEnding() {
        if (state.isEndingActive() || state.isEndlessMode()) {
            return;
        }
        if (!"adult".equals(state.getStage())) {
            return;
        }
        if (state.getSurvivalSeconds() < SURVIVAL_ADULT_TO_ENDING_SEC) {
            return;
        }
        state.setEndingActive(true);
        String endingType = state.getLifetimeSickCount() <= 1 ? "happy" : "normal";
        state.setEndingType(endingType);
        if ("happy".equals(endingType)) {
            state.setHunger(100);
            state.setHappy(100);
            state.setEnergy(100);
        }
    }

    private void triggerDeath() {
        state.setDead(true);
        state.setSick(false);
    }

    private static int clamp(int value) {
        return Math.max(0, Math.min(100, value));
    }

    private GameStatusDto toDto(String message) {
        GameStatusDto dto = new GameStatusDto();
        dto.setStage(state.getStage());
        dto.setPetType(state.getPetType());
        dto.setHunger(state.getHunger());
        dto.setHappy(state.getHappy());
        dto.setEnergy(state.getEnergy());
        dto.setSleeping(state.isSleeping());
        dto.setSick(state.isSick());
        dto.setDead(state.isDead());
        dto.setEnded(state.isEnded());
        dto.setSurvivalSeconds(state.getSurvivalSeconds());
        dto.setLifetimeSickCount(state.getLifetimeSickCount());
        dto.setEndingActive(state.isEndingActive());
        dto.setEndingType(state.getEndingType());
        dto.setEndlessMode(state.isEndlessMode());
        dto.setSickSeconds(state.getSickSeconds());
        dto.setEggClickCount(state.getEggClickCount());
        dto.setSnackStreak(Map.copyOf(state.getSnackStreak()));
        dto.setPoopCount(state.getPoopCount());
        dto.setMessage(message);
        return dto;
    }
}
