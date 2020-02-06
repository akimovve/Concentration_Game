package com.example.concentration.Info;

public class Variables {
    public int changeDelay;
    private static int change = 100;
    private static boolean flag;

    private static int changeable = 0;

    public Variables(boolean flag) {
        Variables.flag = flag;
        this.changeDelay = getUniqueChange();
    }

    private static int getUniqueChange() {
        if (flag) {
            changeable += change;
            change += 10;
        } else {
            changeable = 0;
            change = 100;
        }
        return changeable;
    }
}
