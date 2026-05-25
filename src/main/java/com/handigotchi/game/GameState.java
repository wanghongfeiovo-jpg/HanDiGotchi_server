package com.handigotchi.game;

import java.util.HashMap;
import java.util.Map;

import static com.handigotchi.game.GameConstants.POOP_DELAY_MIN_MS;
import static com.handigotchi.game.GameConstants.POOP_DELAY_RANGE_MS;

public class GameState {

    private String petType;
    private String stage = "egg";
    private int hunger = 100;
    private int happy = 100;
    private int energy = 100;
    private boolean sleeping;
    private boolean sick;
    private boolean dead;
    private boolean ended;
    private double survivalSeconds;
    private int lifetimeSickCount;
    private boolean endingActive;
    private String endingType;
    private boolean endlessMode;
    private double sickSeconds;
    private int eggClickCount;
    private final Map<String, Integer> snackStreak = new HashMap<>();
    private int poopCount;
    private long nextPoopAt;

    public GameState() {
        resetSnackStreaks();
        scheduleNextPoop();
    }

    public void resetSnackStreaks() {
        snackStreak.clear();
        snackStreak.put("pudding", 0);
        snackStreak.put("cookie", 0);
        snackStreak.put("cake", 0);
    }

    public void scheduleNextPoop() {
        long delay = POOP_DELAY_MIN_MS + (long) (Math.random() * POOP_DELAY_RANGE_MS);
        nextPoopAt = System.currentTimeMillis() + delay;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    public int getHunger() {
        return hunger;
    }

    public void setHunger(int hunger) {
        this.hunger = hunger;
    }

    public int getHappy() {
        return happy;
    }

    public void setHappy(int happy) {
        this.happy = happy;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public boolean isSleeping() {
        return sleeping;
    }

    public void setSleeping(boolean sleeping) {
        this.sleeping = sleeping;
    }

    public boolean isSick() {
        return sick;
    }

    public void setSick(boolean sick) {
        this.sick = sick;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public boolean isEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    public double getSurvivalSeconds() {
        return survivalSeconds;
    }

    public void setSurvivalSeconds(double survivalSeconds) {
        this.survivalSeconds = survivalSeconds;
    }

    public int getLifetimeSickCount() {
        return lifetimeSickCount;
    }

    public void setLifetimeSickCount(int lifetimeSickCount) {
        this.lifetimeSickCount = lifetimeSickCount;
    }

    public boolean isEndingActive() {
        return endingActive;
    }

    public void setEndingActive(boolean endingActive) {
        this.endingActive = endingActive;
    }

    public String getEndingType() {
        return endingType;
    }

    public void setEndingType(String endingType) {
        this.endingType = endingType;
    }

    public boolean isEndlessMode() {
        return endlessMode;
    }

    public void setEndlessMode(boolean endlessMode) {
        this.endlessMode = endlessMode;
    }

    public double getSickSeconds() {
        return sickSeconds;
    }

    public void setSickSeconds(double sickSeconds) {
        this.sickSeconds = sickSeconds;
    }

    public int getEggClickCount() {
        return eggClickCount;
    }

    public void setEggClickCount(int eggClickCount) {
        this.eggClickCount = eggClickCount;
    }

    public Map<String, Integer> getSnackStreak() {
        return snackStreak;
    }

    public int getPoopCount() {
        return poopCount;
    }

    public void setPoopCount(int poopCount) {
        this.poopCount = poopCount;
    }

    public long getNextPoopAt() {
        return nextPoopAt;
    }

    public void setNextPoopAt(long nextPoopAt) {
        this.nextPoopAt = nextPoopAt;
    }
}
