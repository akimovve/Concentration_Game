package com.example.concentration.info;

public class Variables {
    private static int change1 = 50, change2 = 60;
    private static int res = 0;

    public Variables() {}

    private static int getUniqueChange(boolean fl) {
        if (fl) {
            if (change1 <= 60) {
                res += change1;
                change1 += 10;
            } else {
                res = change2 == 60? (int) Math.round((Math.random() * 20) - 160) : res - change2;
                change2 += 60;
            }
        } else {
            res = 50;
            change1 = 60;
            change2 = 60;
        }
        return res;
    }

    public int setChange(boolean fl) {
        return getUniqueChange(fl);
    }
}
