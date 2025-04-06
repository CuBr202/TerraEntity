package org.confluence.terraentity.api;

/**
 * 设置FTW难度
 */
public class FTWSetter {
    private static boolean isFTW = false;
    public static void setFTW(boolean ftw) {
        isFTW = ftw;
    }
    public static boolean isFTW() {
        return isFTW;
    }
}
