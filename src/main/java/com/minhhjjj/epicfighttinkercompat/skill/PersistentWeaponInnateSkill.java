package com.minhhjjj.epicfighttinkercompat.skill;

import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
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
    private static final Map<String, ItemStack> CURRENT_TOOLS = new ConcurrentHashMap<>();
    private static final Set<String> PENDING_STACK_RESTORE = ConcurrentHashMap.newKeySet();
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
                        String skillPath = persistentSkill.getRegistryName().getPath();
                        String clientPath = player.getUUID() + "-client-" + skillPath;
                        String serverPath = getMapKey(player, skillPath);
                        String resourcePath = KEY_RESOURCE + "_" + skillPath;
                        String stacksPath = KEY_STACKS + "_" + skillPath;

                        if (getUniqueUUID(oldItem).equals(getUniqueUUID(newItem))) {
                            CURRENT_TOOLS.put(serverPath, newItem);
                            CURRENT_TOOLS.put(clientPath,  newItem);
                            return;
                        }

                        SkillContainer container = playerPatch.getSkill(SkillSlots.WEAPON_INNATE);
                        if (container != null) {
                            ItemStack cachedItem = CURRENT_TOOLS.getOrDefault(serverPath, ItemStack.EMPTY);
                            if (cachedItem.isEmpty()) return;
                            float newResource = newItem.getOrCreateTag().getFloat(resourcePath);
                            int newStacks = newItem.getOrCreateTag().getInt(stacksPath);
                            cachedItem.getOrCreateTag().putFloat(resourcePath, container.getResource());
                            cachedItem.getOrCreateTag().putInt(stacksPath, container.getStack());

                            if (newStacks == 0) {
                                container.setStack(0);
                                container.setResource(newResource);
                            } else {
                                container.setResource(newResource);
                                container.setStack(newStacks);
                            }

                            CURRENT_TOOLS.put(serverPath, newItem);
                            CURRENT_TOOLS.put(clientPath, newItem);
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
        CURRENT_TOOLS.keySet().removeIf(key -> key.contains(player.getUUID().toString()));
        PENDING_STACK_RESTORE.removeIf(key -> key.contains(player.getUUID().toString()));
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
                String skillPath = this.getRegistryName().getPath();
                CURRENT_TOOLS.put(getMapKey(player, skillPath), itemStack);
                PENDING_STACK_RESTORE.add(getMapKey(player, skillPath));

                float resource = itemStack.getOrCreateTag().getFloat(KEY_RESOURCE + "_" + skillPath);
                int stacks = itemStack.getOrCreateTag().getInt(KEY_STACKS + "_"  + skillPath);
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
        String skillPath = this.getRegistryName().getPath();
        if (entity instanceof Player player) {
            ItemStack itemStack = CURRENT_TOOLS.remove(getMapKey(player, this.getRegistryName().getPath()));
            if (isValidTool(itemStack)) {
                float resource = container.getResource();
                int stacks = container.getStack();
                itemStack.getOrCreateTag().putFloat(KEY_RESOURCE + "_" + skillPath, resource);
                itemStack.getOrCreateTag().putInt(KEY_STACKS + "_" + skillPath, stacks);
            }
        }
    }

    public void updateContainer(SkillContainer container) {
        super.updateContainer(container);
        Entity entity = container.getExecutor().getOriginal();
        String skillPath = this.getRegistryName().getPath();
        if (entity instanceof Player player && PENDING_STACK_RESTORE.contains(getMapKey(player, skillPath))) {
            ItemStack itemStack = CURRENT_TOOLS.getOrDefault(getMapKey(player, skillPath), ItemStack.EMPTY);
            if (!itemStack.isEmpty()) {
                int stacks = itemStack.getOrCreateTag().getInt(KEY_STACKS + "_" + skillPath);
                float resource = itemStack.getOrCreateTag().getFloat(KEY_RESOURCE + "_" + skillPath);

                if (stacks == 0) {
                    container.setStack(0);
                    container.setResource(resource);
                } else {
                    container.setResource(resource);
                    container.setStack(stacks);
                }
            }
            PENDING_STACK_RESTORE.remove(getMapKey(player, this.getRegistryName().getPath()));
        }
    }

    public boolean isValidTool(ItemStack itemStack) {
        return itemStack != null && itemStack.getItem() instanceof IModifiable;
    }

    private static String getMapKey(LivingEntity entity, String skillId) {
        return entity.getUUID() + (entity.level().isClientSide ? "-client-" : "-server-") + skillId;
    }

    private static UUID getUniqueUUID(ItemStack stack) {
        if (!stack.getOrCreateTag().contains("eft_uuid_tool")) {
            stack.getOrCreateTag().putUUID("eft_uuid_tool", UUID.randomUUID());
        }
        return stack.getOrCreateTag().getUUID("eft_uuid_tool");
    }

}
