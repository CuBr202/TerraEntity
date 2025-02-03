package org.confluence.terraentity.utils;

public class Gomoku {
    public native int[] compute(int[] xs,int []ys,int time_turn);

    static {
        System.load(System.getProperty("user.dir")+ "/config/gomoku.dll");
    }
}
