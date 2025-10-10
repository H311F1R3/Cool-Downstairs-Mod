package net.H511F1R5.cooldownstairsmod.item.custom;

import com.google.common.collect.ImmutableMultimap;
import net.H511F1R5.cooldownstairsmod.coolDownstairsMod;
import net.H511F1R5.cooldownstairsmod.effect.coolDownstairsEffectRegistry;
import com.google.common.collect.Multimap;
import net.H511F1R5.cooldownstairsmod.item.modArmorMaterials;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ForgeMod;

import java.util.UUID;

public class modArmorItems extends ArmorItem {
    private static final UUID[] ARMOR_MODIFIERS = new UUID[]{UUID.fromString("92739222-22a8-4fe6-98d8-f6384e17d875"), UUID.fromString("88159420-8a34-4bd8-abb8-e5be847cc063")};
    private Multimap<Attribute, AttributeModifier> AttributeMapDealmaker;
    private Multimap<Attribute, AttributeModifier> AttributeMapFootball;

    public modArmorItems(ArmorMaterial pMaterial, Type pType) {
        super(pMaterial, pType, new Item.Properties());
    }

    // This class is for adding passive armor stats, not active effects like the turtle shell. \
    private void buildDealmakerAttributes(modArmorMaterials materialIn) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        UUID uuid = ARMOR_MODIFIERS[type.ordinal()];
        builder.put(Attributes.ARMOR, new AttributeModifier(uuid, "Armor modifier", materialIn.getDefenseForType(this.type), AttributeModifier.Operation.ADDITION));
        AttributeMapDealmaker = builder.build();
    }

}
