package com.cs349.a4;

public class Config {
    enum SegType {
        HEAD,
        TORSO,
        LEFTUPPERARM,
        RIGHTUPPERARM,
        LEFTLOWERARM,
        RIGHTLOWERARM,
        LEFTUPPERLEG,
        RIGHTUPPERLEG,
        LEFTLOWERLEG,
        RIGHTLOWERLEG,
        HAND,
        FEET
    }

    public static final int HEAD_LIMIT = 50;
    public static final int LOWERARM_LIMIT = 135;
    public static final int LEG_LIMIT = 90;
    public static final int HAND_FEET_LIMIT = 35;

    public static final int headH = 370;
    public static final int headW = 270;
    public static final int torsoL = 500;
    public static final int torsoW = 400;

    public static final int upperArmL = 500;
    public static final int lowerArmL = 360;
    public static final int armW = 250;

    public static final int upperLegL = 470;
    public static final int lowerLegL = 480;
    public static final int legW = 210;

    public static final int handL = 300;
    public static final int handW = handL;

    public static final int feetL = 200;
    public static final int feetW = 150;

}
