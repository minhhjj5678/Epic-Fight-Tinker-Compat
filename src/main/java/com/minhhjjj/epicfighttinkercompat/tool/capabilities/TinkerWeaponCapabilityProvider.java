package com.minhhjjj.epicfighttinkercompat.tool.capabilities;

import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;
import com.minhhjjj.epicfighttinkercompat.stats.EpicFightToolStats;
import com.minhhjjj.epicfighttinkercompat.tool.EpicFightArmorStatsHelper;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.ItemStack;
import yesman.epicfight.world.capabilities.item.ArmorCapability;
import yesman.epicfight.world.capabilities.item.CapabilityItem.Styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class TinkerWeaponCapabilityProvider implements ICapabilityProvider {
    private final LazyOptional<CapabilityItem> optionalCapability;
    public static final ResourceLocation EPIC_CAP_ID = ResourceLocation.fromNamespaceAndPath(EpicFightTinkerCompat.MODID, "weapon_cap");
    private static final Map<String, Function<Item, CapabilityItem.Builder>> WEAPON_CAPABILITY_PRESETS;

    static {
        Map<String, Function<Item, CapabilityItem.Builder>> presets = new HashMap<>();
        presets.put("cleaver", TCWeaponCapabilityPresets.CLEAVER);
        presets.put("vein_hammer", TCWeaponCapabilityPresets.TC_SWORD);
        presets.put("javelin", TCWeaponCapabilityPresets.JAVELIN);
        presets.put("sword", TCWeaponCapabilityPresets.TC_SWORD);
        presets.put("battlesign", TCWeaponCapabilityPresets.TC_SWORD);
        presets.put("sledge_hammer", TCWeaponCapabilityPresets.TC_SLEDGE_HAMMER);
        presets.put("hand_axe", TCWeaponCapabilityPresets.TC_AXE);
        presets.put("broad_axe", TCWeaponCapabilityPresets.TC_AXE);
        presets.put("scythe", TCWeaponCapabilityPresets.SCYTHE);
        presets.put("dagger", TCWeaponCapabilityPresets.TC_DAGGER);
        presets.put("sky_staff", TCWeaponCapabilityPresets.STAFF);
        presets.put("earth_staff", TCWeaponCapabilityPresets.STAFF);
        presets.put("ender_staff", TCWeaponCapabilityPresets.STAFF);
        presets.put("ichor_staff", TCWeaponCapabilityPresets.STAFF);
        presets.put("melting_pan", TCWeaponCapabilityPresets.STAFF);
        presets.put("longbow", TCWeaponCapabilityPresets.LONGBOW);
        presets.put("crossbow", TinkerCrossbowCapability.TCROSSBOW);
        presets.put("pickaxe", TCWeaponCapabilityPresets.TC_PICKAXE);
        presets.put("pickadze", TCWeaponCapabilityPresets.TC_PICKAXE);
        presets.put("war_pick", TCWeaponCapabilityPresets.TC_PICKAXE);
        presets.put("shovel", TCWeaponCapabilityPresets.TC_PICKAXE);
        presets.put("excavator", TCWeaponCapabilityPresets.TC_PICKAXE);
        presets.put("hoe", TCWeaponCapabilityPresets.TC_PICKAXE);
        presets.put("kama", TCWeaponCapabilityPresets.TC_PICKAXE);
        presets.put("mattock", TCWeaponCapabilityPresets.TC_PICKAXE);
        presets.put("plate_shield", TCWeaponCapabilityPresets.TC_SHIELD);
        presets.put("travelers_shield", TCWeaponCapabilityPresets.TC_SHIELD);
        presets.put("swasher", TCWeaponCapabilityPresets.TC_SWORD);
        presets.put("fishing_rod", TCWeaponCapabilityPresets.TC_SWORD);
        presets.put("katana", TCWeaponCapabilityPresets.KATANA);
        presets.put("fuma_shuriken", TCWeaponCapabilityPresets.FUMA_SHURIKEN);
        presets.put("shuriken", TCWeaponCapabilityPresets.SHURIKEN);
        presets.put("throwing_axe", TCWeaponCapabilityPresets.SHURIKEN);
        presets.put("battlestaff", TCWeaponCapabilityPresets.BATTLE_STAFF);
        presets.put("flamberge", TCWeaponCapabilityPresets.FLAMBERGE);
        WEAPON_CAPABILITY_PRESETS = Map.copyOf(presets);
    }

    public TinkerWeaponCapabilityProvider(String weaponType, ItemStack stack) {
        CapabilityItem eCapabilityItem = createCapabilityItem(weaponType, stack);

        if (eCapabilityItem == null) {
            this.optionalCapability = LazyOptional.empty();
        } else {
            this.optionalCapability = LazyOptional.of(() -> eCapabilityItem);
        }
    }

    @SuppressWarnings("null")
	private CapabilityItem createCapabilityItem(String weaponType, ItemStack stack) {
        ToolStack tool = ToolStack.from(stack);
        CapabilityItem.Builder builder = null;
        if (tool.getItem() instanceof net.minecraft.world.item.ArmorItem) {
            EpicFightArmorStatsHelper.ArmorStats armorStats = EpicFightArmorStatsHelper.resolveArmorStats(tool);
            double weight = armorStats.weight();
            double stunArmor = armorStats.stunArmor();

            builder = ArmorCapability.builder()
                .item(tool.getItem())
                .weight(weight)
                .stunArmor(stunArmor);
        }
        else {
            Function<Item, CapabilityItem.Builder> preset = WEAPON_CAPABILITY_PRESETS.get(weaponType);
            if (preset != null) {
                builder = preset.apply(tool.getItem());
            }
        }

        if (builder == null) {
            return null;
        }

        double impactBonus = tool.getStats().get(EpicFightToolStats.IMPACT);
        double strikesBonus = tool.getStats().get(EpicFightToolStats.MAX_STRIKES);
        double armorNegationBonus = tool.getStats().get(EpicFightToolStats.ARMOR_NEGATION);        
        if (!(tool.getItem() instanceof net.minecraft.world.item.ArmorItem)) {
            if (impactBonus != 0) {
                UUID impactUUID = UUID.nameUUIDFromBytes((EpicFightTinkerCompat.MODID + ":" + weaponType + ":impact").getBytes(java.nio.charset.StandardCharsets.UTF_8));
                builder.addStyleAttibutes(Styles.COMMON, Pair.of(
                    EpicFightAttributes.IMPACT.get(), 
                    new AttributeModifier(impactUUID, "Tinker Impact", impactBonus, AttributeModifier.Operation.ADDITION)
                ));
            }
            if (strikesBonus != 0) {
                UUID strikesUUID = UUID.nameUUIDFromBytes((EpicFightTinkerCompat.MODID + ":" + weaponType + ":strikes").getBytes(java.nio.charset.StandardCharsets.UTF_8));
                builder.addStyleAttibutes(Styles.COMMON, Pair.of(
                    EpicFightAttributes.MAX_STRIKES.get(), 
                    new AttributeModifier(strikesUUID, "Tinker Strikes", strikesBonus, AttributeModifier.Operation.ADDITION)
                ));
            }
            if (armorNegationBonus != 0) {
                UUID armorUUID = UUID.nameUUIDFromBytes((EpicFightTinkerCompat.MODID + ":" + weaponType + ":armor_negation").getBytes(java.nio.charset.StandardCharsets.UTF_8));
                builder.addStyleAttibutes(Styles.COMMON, Pair.of(
                    EpicFightAttributes.ARMOR_NEGATION.get(), 
                    new AttributeModifier(armorUUID, "Tinker Armor Negation", armorNegationBonus, AttributeModifier.Operation.ADDITION)
                ));
            }
        }

        return builder.build();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == EpicFightCapabilities.CAPABILITY_ITEM) {
            return optionalCapability.cast();
        }
        return LazyOptional.empty();
    }
}
