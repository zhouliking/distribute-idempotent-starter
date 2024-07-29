package com.joelly.idempotent.switchs;


public class IdempotentProcessSwitch {
    /**
     * 前置处理开关
     */
    private static volatile boolean preAspectProcessSwitch = true;

    public static boolean isPreAspectProcessSwitch() {
        return preAspectProcessSwitch;
    }

    public static void setPreAspectProcessSwitch(boolean preAspectProcessSwitch) {
        IdempotentProcessSwitch.preAspectProcessSwitch = preAspectProcessSwitch;
    }
}
