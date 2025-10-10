package net.H511F1R5.cooldownstairsmod.item;

import net.H511F1R5.cooldownstairsmod.coolDownstairsMod;
import net.H511F1R5.cooldownstairsmod.sounds.cooldownstairsSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class modArmorMaterials implements ArmorMaterial {;

    private final String name;
    private final int durability;
    private final int[] durabilityForType;
    private final int protectionAmounts;
    private final int enchantmentValue;
    private final Supplier<SoundEvent> equipSound;
    private final float toughness;
    private float knockbackResistance = 0.0f;
    private Ingredient ingredient = null;


    private static final int[] BASE_DURABILITY = {3, 6, 8, 2};


    modArmorMaterials(String name, int durabilityModifier, int[] durabilityForType, int protectionAmounts,  int enchantmentValue, Supplier<SoundEvent> equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient, Ingredient ingredient) {
        this.name = coolDownstairsMod.MOD_ID + ":" + name;
        this.durability = durabilityModifier;
        this.durabilityForType = durabilityForType;
        this.protectionAmounts = protectionAmounts;
        this.enchantmentValue = enchantmentValue;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.ingredient = ingredient;
    }

    public modArmorMaterials(String name, int durabilityModifier, int[] durabilityForType, int protectionAmounts, int enchantmentValue, Supplier<SoundEvent> equipSound, int toughness) {
        this.name = coolDownstairsMod.MOD_ID + ":" + name;
        this.durability = durabilityModifier;
        this.durabilityForType = durabilityForType;
        this.protectionAmounts = protectionAmounts;
        this.enchantmentValue = enchantmentValue;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.ingredient = ingredient;
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return BASE_DURABILITY[type.ordinal()] * this.durability; // understand more abt what this impacts and why durability should be used with it.
    }

    @Override
    public int getDefenseForType(ArmorItem.Type pType) {
        return this.durabilityForType[pType.ordinal()];
    }

    @Override
    public int getEnchantmentValue() {
        return enchantmentValue;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.equipSound.get();
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.ingredient == null ? Ingredient.EMPTY : this.ingredient;
    }

    @Override
    public String getName() {
        return coolDownstairsMod.MOD_ID + ":" + this.name ;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }

    public void setRepairMaterial(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

}
