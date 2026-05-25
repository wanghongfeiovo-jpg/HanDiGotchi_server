package com.handigotchi.game;

import java.util.Map;

public final class GameConstants {

    public static final long GAME_TICK_MS = 4500L;
    public static final double GAME_TICK_SEC = GAME_TICK_MS / 1000.0;
    public static final int EGG_HATCH_CLICKS = 3;

    public static final int STAT_DECAY_PER_TICK = 2;
    public static final int POOP_HAPPY_PENALTY_PER = 1;
    public static final int SLEEP_ENERGY_REGEN = 15;

    public static final int PLAY_HAPPY_GAIN = 40;
    public static final int PLAY_ENERGY_COST = 20;
    public static final int PLAY_MIN_ENERGY = 20;

    public static final int MED_STAT_RESTORE = 50;
    public static final int CLEAN_HAPPY_GAIN = 15;
    
        //结局速度
    public static final double SICK_DEATH_SEC = 45;
    public static final double SURVIVAL_BABY_TO_TEEN_SEC = 60;
    public static final double SURVIVAL_TEEN_TO_ADULT_SEC = 120;
    public static final double SURVIVAL_ADULT_TO_ENDING_SEC = 120;

        // public static final double SICK_DEATH_SEC = 5;
        // public static final double SURVIVAL_BABY_TO_TEEN_SEC = 10;
        // public static final double SURVIVAL_TEEN_TO_ADULT_SEC = 10;
        // public static final double SURVIVAL_ADULT_TO_ENDING_SEC = 10;
        // public static final double SURVIVAL_ADULT_TO_ENDLESS_SEC = 10;

    public static final long POOP_DELAY_MIN_MS = 45_000L;
    public static final long POOP_DELAY_RANGE_MS = 45_000L;

    public static final Map<String, MealDef> MEALS = Map.of(
            "onigiri", new MealDef(20, 5),
            "dumpling", new MealDef(35, 10),
            "burger", new MealDef(50, 15),
            "omurice", new MealDef(80, 25)
    );

    public static final Map<String, SnackDef> SNACKS = Map.of(
            "pudding", new SnackDef(15, 5),
            "cookie", new SnackDef(30, 3),
            "cake", new SnackDef(50, 2)
    );

    private GameConstants() {
    }

    public record MealDef(int hunger, int energy) {
    }

    public record SnackDef(int happy, int overload) {
    }
}
