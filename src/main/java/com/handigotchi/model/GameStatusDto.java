package com.handigotchi.model;

import java.util.Map;

public class GameStatusDto {

    private String stage;
    private String petType;
    private int hunger;
    private int happy;
    private int energy;
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
    private Map<String, Integer> snackStreak;
    private int poopCount;
    private String message;

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

    public void setSnackStreak(Map<String, Integer> snackStreak) {
        this.snackStreak = snackStreak;
    }

    public int getPoopCount() {
        return poopCount;
    }

    public void setPoopCount(int poopCount) {
        this.poopCount = poopCount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
