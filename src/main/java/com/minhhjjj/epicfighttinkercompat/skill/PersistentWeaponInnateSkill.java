package com.minhhjjj.epicfighttinkercompat.skill;

import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.network.server.SPSetSkillContainerValue;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillBuilder;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber(modid = EpicFightTinkerCompat.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public abstract class PersistentWeaponInnateSkill extends WeaponInnateSkill {
    private static final Map<UUID, Map<ResourceLocation, ItemStack>> SERVER_CURRENT_TOOLS = new ConcurrentHashMap<>();
    private static final Map<UUID, Map<ResourceLocation, ItemStack>> CLIENT_CURRENT_TOOLS = new ConcurrentHashMap<>();

    // Using a nested CompoundTag allows storing multiple skill states concurrently for future multi-skill item support
    public static final String KEY_RESOURCE = "key_resource";
    public static final String KEY_STACKS = "key_stacks";

    @SubscribeEvent()
    public static void onWeaponSwap(LivingEquipmentChangeEvent event) {
        if (event.getSlot() == EquipmentSlot.MAINHAND && event.getEntity() instanceof ServerPlayer player) {
            ItemStack oldItem = event.getFrom();
            ItemStack newItem = event.getTo();

            if (oldItem.getItem() instanceof IModifiable && newItem.getItem() instanceof IModifiable) {
                PlayerPatch<?> playerPatch = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);
                CapabilityItem oldCapabilityItem = EpicFightCapabilities.getItemStackCapability(oldItem);
                CapabilityItem newCapabilityItem = EpicFightCapabilities.getItemStackCapability(newItem);

                if (playerPatch != null && !oldCapabilityItem.isEmpty() && !newCapabilityItem.isEmpty()) {
                    Skill oldSkill = oldCapabilityItem.getInnateSkill(playerPatch, oldItem);
                    Skill newSkill = newCapabilityItem.getInnateSkill(playerPatch, newItem);

                    if (oldSkill instanceof PersistentWeaponInnateSkill persistentSkill && oldSkill == newSkill) {
                        ResourceLocation skillResource = persistentSkill.getRegistryName();
                        String skillPath = skillResource.getPath();

                        if (getUniqueUUID(oldItem).equals(getUniqueUUID(newItem))) {
                            SERVER_CURRENT_TOOLS.computeIfAbsent(player.getUUID(), k -> new ConcurrentHashMap<>()).put(skillResource, newItem);
                            CLIENT_CURRENT_TOOLS.computeIfAbsent(player.getUUID(), k -> new ConcurrentHashMap<>()).put(skillResource, newItem);
                            return;
                        }

                        SkillContainer container = playerPatch.getSkill(SkillSlots.WEAPON_INNATE);
                        if (container != null) {
                            ItemStack cachedItem = SERVER_CURRENT_TOOLS.getOrDefault(player.getUUID(), new ConcurrentHashMap<>()).getOrDefault(persistentSkill.getRegistryName(), ItemStack.EMPTY);
                            if (cachedItem.isEmpty()) return;
                            float newResource = newItem.getOrCreateTag().getCompound(KEY_RESOURCE).getFloat(skillPath);
                            int newStacks = newItem.getOrCreateTag().getCompound(KEY_STACKS).getInt(skillPath);

                            CompoundTag itemTag = cachedItem.getOrCreateTag();
                            getOrCreateSubTag(itemTag, KEY_RESOURCE).putFloat(skillPath, container.getResource());
                            getOrCreateSubTag(itemTag, KEY_STACKS).putInt(skillPath, container.getStack());

                            if (newStacks == 0) {
                                container.setStack(0);
                                container.setResource(newResource);
                            } else {
                                container.setResource(newResource);
                                container.setStack(newStacks);
                            }

                            SERVER_CURRENT_TOOLS.computeIfAbsent(player.getUUID(), k -> new ConcurrentHashMap<>()).put(skillResource, newItem);
                            CLIENT_CURRENT_TOOLS.computeIfAbsent(player.getUUID(), k -> new ConcurrentHashMap<>()).put(skillResource, newItem);
                            if (newStacks == 0) {
                                EpicFightNetworkManager.sendToAll(SPSetSkillContainerValue.stacks(SkillSlots.WEAPON_INNATE, newStacks, player.getId()));
                                EpicFightNetworkManager.sendToAll(SPSetSkillContainerValue.resource(SkillSlots.WEAPON_INNATE, newResource, player.getId()));
                            } else {
                                EpicFightNetworkManager.sendToAll(SPSetSkillContainerValue.resource(SkillSlots.WEAPON_INNATE, newResource, player.getId()));
                                EpicFightNetworkManager.sendToAll(SPSetSkillContainerValue.stacks(SkillSlots.WEAPON_INNATE, newStacks, player.getId()));
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerRemove(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = event.getEntity();
        if (player == null) return;
        SERVER_CURRENT_TOOLS.remove(player.getUUID());
        CLIENT_CURRENT_TOOLS.remove(player.getUUID());
    }

    public PersistentWeaponInnateSkill(SkillBuilder<? extends WeaponInnateSkill> builder) {
        super(builder);
    }

    public void onInitiate(SkillContainer container) {
        super.onInitiate(container);
        Entity entity = container.getExecutor().getOriginal();
        if (entity instanceof Player player) {
            ItemStack itemStack = player.getMainHandItem();
            if (isValidTool(itemStack)) {
                getUniqueUUID(itemStack);
                ResourceLocation skillResource = this.getRegistryName();
                var currentToolMap = getMapSide(player);
                currentToolMap.computeIfAbsent(player.getUUID(), k -> new ConcurrentHashMap<>()).put(skillResource, itemStack);

                float resource = itemStack.getOrCreateTag().getCompound(KEY_RESOURCE).getFloat(skillResource.getPath());
                int stacks = itemStack.getOrCreateTag().getCompound(KEY_STACKS).getInt(skillResource.getPath());
                container.setMaxResource(this.consumption);
                if (stacks == 0) {
                    container.setStack(0);
                    container.setResource(resource);
                } else {
                    container.setResource(resource);
                    container.setStack(stacks);
                }
            }
        }
    }

    public void onRemoved(SkillContainer container) {
        super.onRemoved(container);
        Entity entity = container.getExecutor().getOriginal();
        ResourceLocation skillResource = this.getRegistryName();
        if (entity instanceof Player player) {
            var currentToolMap = getMapSide(player);
            ItemStack itemStack = currentToolMap.getOrDefault(player.getUUID(), new ConcurrentHashMap<>()).get(skillResource);
            if (isValidTool(itemStack)) {
                float resource = container.getResource();
                int stacks = container.getStack();

                CompoundTag itemTag = itemStack.getOrCreateTag();
                getOrCreateSubTag(itemTag, KEY_RESOURCE).putFloat(skillResource.getPath(), resource);
                getOrCreateSubTag(itemTag, KEY_STACKS).putInt(skillResource.getPath(), stacks);
            }
        }
    }

    public boolean isValidTool(ItemStack itemStack) {
        return itemStack != null && itemStack.getItem() instanceof IModifiable;
    }

    private static Map<UUID, Map<ResourceLocation, ItemStack>> getMapSide(Player player) {
        return player.level().isClientSide() ? CLIENT_CURRENT_TOOLS : SERVER_CURRENT_TOOLS;
    }

    private static UUID getUniqueUUID(ItemStack stack) {
        if (!stack.getOrCreateTag().contains("eft_uuid_tool")) {
            stack.getOrCreateTag().putUUID("eft_uuid_tool", UUID.randomUUID());
        }
        return stack.getOrCreateTag().getUUID("eft_uuid_tool");
    }

    private static CompoundTag getOrCreateSubTag(CompoundTag parent, String key) {
        if (!parent.contains(key, Tag.TAG_COMPOUND)) {
            CompoundTag tag = new CompoundTag();
            parent.put(key, tag);
        }
        return parent.getCompound(key);
    }

}
