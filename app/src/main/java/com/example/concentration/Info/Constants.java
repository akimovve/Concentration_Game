package com.example.concentration.Info;

public class Constants {
    private static int levelNumber = 0;
    private static int numberOfBut = 0;
    public int delayForFirstAppearance = 250;
    public int delayBetweenAppearance = 70;
    public int timeCardIsOpen = 200;
    public int timeCardIsClose = 150;

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
