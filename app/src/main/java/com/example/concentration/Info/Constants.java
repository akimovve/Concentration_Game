package com.example.concentration.Info;

public class Constants {
    public static int levelNumber = 0;
    public static int numberOfBut = 0;
    public int delayForFirstAppearance = 280;
    public int delayBetweenAppearance = 70;
    public int timeCardIsOpen = 500;
    public int timeCardIsClose = 600;

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
