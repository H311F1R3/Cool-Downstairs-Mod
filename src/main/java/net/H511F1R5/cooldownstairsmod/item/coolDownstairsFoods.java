package net.H511F1R5.cooldownstairsmod.item;

import net.minecraft.world.food.FoodProperties;

public class coolDownstairsFoods {
    public static final FoodProperties BUBBLEGUM_BALL = new FoodProperties.Builder()
            .nutrition(1)
            .saturationMod(0.1f)
            .fast()
            .alwaysEat()
            .build();

    public static final FoodProperties CHICKEN_SHANK = new FoodProperties.Builder()
            .nutrition(8)
            .saturationMod(0.8f)
            .meat()
            .build();
}
