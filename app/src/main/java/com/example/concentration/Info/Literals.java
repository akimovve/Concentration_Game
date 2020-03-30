package com.example.concentration.Info;

public class Literals {

    public static final short maxLevel = 5;
    private static int levelNumber = 0;
    private static int numberOfBut = 0;

    public final static int match = 2;
    public final static int miss = 1;
    public static int points = 0;

    public int delayForFirstAppearance = 350;
    public int delayBetweenAppearance = 80;
    public int timeCardIsOpen = 300;
    public int timeCardIsClose = 250;

    public static double getMaximumPoints() {
        int res = 0, c = 4;
        for (int i = 0; i < maxLevel; i++) {
            res += c * 2;
            c += 4;
        }
        return res;
    }

    public static int getNumberOFButtons(boolean flag) {
        if (flag) numberOfBut+=4;
        else numberOfBut = 4;
        return numberOfBut;
    }

    public static int getLevelNumber(boolean flag) {
        if (flag) levelNumber++;
        else levelNumber = 1;
        return levelNumber;
    }
}
