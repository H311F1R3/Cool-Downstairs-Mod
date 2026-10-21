package net.H511F1R5.cooldownstairsmod.mixin.util;

import net.minecraft.util.Mth;

public class PlacementDelayUtil {
    // vanilla flying speed with sprint = ~0.10 blocks/tick
    private static final double MAX_FLY_SPEED   = 0.10;

    // can never go below 1 tick or hypixel will throw a fuckin fit
    private static final int    MIN_DELAY        = 1;
    private static final int    VANILLA_DELAY    = 5;

    public static int scaleDelay(int vanilla, double hSpeed) {

        // t = speed / maxSpeed, clamped [0, 1]
        double t = Mth.clamp(hSpeed / MAX_FLY_SPEED, 0.0, 1.0);

        // ease-out curve: sqrt(t) responds quickly at low speeds,
        // flattens near max — avoids brutal jump at full sprint
        double curve = Math.sqrt(t);

        // lerp from VANILLA_DELAY down to MIN_DELAY
        int scaled = (int) Math.round(
            Mth.lerp(curve, VANILLA_DELAY, MIN_DELAY)
        );

        return Math.max(scaled, MIN_DELAY);
    }
}

