package com.minhhjjj.epicfighttinkercompat.mixin.itemspoof.common;

import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.world.capabilities.item.Style;
import yesman.epicfight.world.capabilities.item.WeaponCapability;

import java.util.Map;
import java.util.function.Function;

@Mixin(value = WeaponCapability.class, remap = false)
public interface MixinWeaponCapability {

    @Accessor("innateSkill")
    Map<Style, Function<ItemStack, Skill>> getInnateSkill();

}