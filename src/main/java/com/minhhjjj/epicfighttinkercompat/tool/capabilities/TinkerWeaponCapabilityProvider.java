package com.minhhjjj.epicfighttinkercompat.tool.capabilities;

import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;
import com.minhhjjj.epicfighttinkercompat.stats.EpicFightToolStats;
import com.minhhjjj.epicfighttinkercompat.tool.EpicFightArmorStatsHelper;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import yesman.epicfight.api.forgeevent.WeaponCapabilityPresetRegistryEvent;
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

@Mod.EventBusSubscriber(modid = EpicFightTinkerCompat.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TinkerWeaponCapabilityProvider implements ICapabilityProvider {
    private final LazyOptional<CapabilityItem> optionalCapability;
    public static final ResourceLocation EPIC_CAP_ID = ResourceLocation.fromNamespaceAndPath(EpicFightTinkerCompat.MODID, "weapon_cap");
    private static final Map<ResourceLocation, Function<Item, CapabilityItem.Builder>> WEAPON_CAPABILITY_PRESETS;

    static {
        Map<ResourceLocation, Function<Item, CapabilityItem.Builder>> presets = new HashMap<>();
        presets.put(rl("cleaver"), TCWeaponCapabilityPresets.CLEAVER);
        presets.put(rl("vein_hammer"), TCWeaponCapabilityPresets.TC_SWORD);
        presets.put(rl("javelin"), TCWeaponCapabilityPresets.JAVELIN);
        presets.put(rl("sword"), TCWeaponCapabilityPresets.TC_SWORD);
        presets.put(rl("battlesign"), TCWeaponCapabilityPresets.TC_SWORD);
        presets.put(rl("sledge_hammer"), TCWeaponCapabilityPresets.TC_SLEDGE_HAMMER);
        presets.put(rl("hand_axe"), TCWeaponCapabilityPresets.TC_AXE);
        presets.put(rl("broad_axe"), TCWeaponCapabilityPresets.TC_AXE);
        presets.put(rl("scythe"), TCWeaponCapabilityPresets.SCYTHE);
        presets.put(rl("dagger"), TCWeaponCapabilityPresets.TC_DAGGER);
        presets.put(rl("sky_staff"), TCWeaponCapabilityPresets.STAFF);
        presets.put(rl("earth_staff"), TCWeaponCapabilityPresets.STAFF);
        presets.put(rl("ender_staff"), TCWeaponCapabilityPresets.STAFF);
        presets.put(rl("ichor_staff"), TCWeaponCapabilityPresets.STAFF);
        presets.put(rl("melting_pan"), TCWeaponCapabilityPresets.STAFF);
        presets.put(rl("longbow"), TCWeaponCapabilityPresets.LONGBOW);
        presets.put(rl("crossbow"), TinkerCrossbowCapability.TCROSSBOW);
        presets.put(rl("pickaxe"), TCWeaponCapabilityPresets.TC_PICKAXE);
        presets.put(rl("pickadze"), TCWeaponCapabilityPresets.TC_PICKAXE);
        presets.put(rl("war_pick"), TCWeaponCapabilityPresets.TC_PICKAXE);
        presets.put(rl("shovel"), TCWeaponCapabilityPresets.TC_PICKAXE);
        presets.put(rl("excavator"), TCWeaponCapabilityPresets.TC_PICKAXE);
        presets.put(rl("hoe"), TCWeaponCapabilityPresets.TC_PICKAXE);
        presets.put(rl("kama"), TCWeaponCapabilityPresets.TC_PICKAXE);
        presets.put(rl("mattock"), TCWeaponCapabilityPresets.TC_PICKAXE);
        presets.put(rl("plate_shield"), TCWeaponCapabilityPresets.TC_SHIELD);
        presets.put(rl("travelers_shield"), TCWeaponCapabilityPresets.TC_SHIELD);
        presets.put(rl("swasher"), TCWeaponCapabilityPresets.SWASHER);
        presets.put(rl("fishing_rod"), TCWeaponCapabilityPresets.TC_SWORD);
        presets.put(rl("katana"), TCWeaponCapabilityPresets.KATANA);
        presets.put(rl("fuma_shuriken"), TCWeaponCapabilityPresets.FUMA_SHURIKEN);
        presets.put(rl("shuriken"), TCWeaponCapabilityPresets.SHURIKEN);
        presets.put(rl("throwing_axe"), TCWeaponCapabilityPresets.SHURIKEN);
        presets.put(rl("battlestaff"), TCWeaponCapabilityPresets.BATTLE_STAFF);
        presets.put(rl("flamberge"), TCWeaponCapabilityPresets.FLAMBERGE);
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
        if (!(stack.getItem() instanceof IModifiable)) return null;
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
            Function<Item, CapabilityItem.Builder> preset = WEAPON_CAPABILITY_PRESETS.getOrDefault(rl(weaponType), TCWeaponCapabilityPresets.UNKNOWN);
            if (preset != null) {
                builder = preset.apply(tool.getItem());
                if (builder instanceof TCWeaponCapability.Builder tcBuilder) {
                    tcBuilder.tool(stack);
                }
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

    private static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(EpicFightTinkerCompat.MODID, path);
    }

    @SubscribeEvent
    public static void register(WeaponCapabilityPresetRegistryEvent event) {
        event.getTypeEntry().putAll(WEAPON_CAPABILITY_PRESETS);
    }
}
