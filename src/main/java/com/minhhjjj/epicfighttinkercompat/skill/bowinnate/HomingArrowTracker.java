package com.minhhjjj.epicfighttinkercompat.skill.bowinnate;

import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber(modid = EpicFightTinkerCompat.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HomingArrowTracker {
    private static final Map<AbstractArrow, ArrowProfile> TRACKED_ARROWS = new WeakHashMap<>();
    private static final double MAX_RANGE = 20D;
    private static final int STOP_THRESHOLD = 40;

    public static class ArrowProfile {
        public LivingEntity target;
        public int lastTimeTargeting = 0;

        ArrowProfile(LivingEntity target) {
            this.target = target;
        }
    }

    public static void addArrow(AbstractArrow arrow,  LivingEntity entity) {
        if (arrow != null) {
            TRACKED_ARROWS.put(arrow, new ArrowProfile(entity));
        }
    }

    public static boolean updateTarget(AbstractArrow arrow) {
        AABB arrowAABB = arrow.getBoundingBox().inflate(MAX_RANGE);
        List<LivingEntity> livings = arrow.level().getEntitiesOfClass(LivingEntity.class, arrowAABB);
        List<LivingEntity> validEntities = new ArrayList<>();

        for(LivingEntity livingEntity : livings) {
            if (isValidTarget(livingEntity, arrow)) {
                validEntities.add(livingEntity);
            }
        }

        if (!validEntities.isEmpty()) {
            int randomI = arrow.level().random.nextInt(validEntities.size());
            TRACKED_ARROWS.replace(arrow, new ArrowProfile(validEntities.get(randomI)));
            return true;
        }

        return false;
    }

    public static boolean isValidTarget(LivingEntity target, AbstractArrow arrow) {
        Entity owner = arrow.getOwner();
        if (!target.isAlive()) return false;
        if (owner != null && target.is(owner)) return false;
        if (target.isSpectator()) return false;
        if (target instanceof TamableAnimal animal && animal.isTame()) return false;
        if (target instanceof Player player && player.isCreative()) return false;
        return (target instanceof Monster || (target instanceof NeutralMob neutralMob && owner instanceof LivingEntity livingOwner && neutralMob.isAngryAt(livingOwner)));
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START || TRACKED_ARROWS.isEmpty()) return;

        Iterator<Map.Entry<AbstractArrow, ArrowProfile>> iterator = TRACKED_ARROWS.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<AbstractArrow, ArrowProfile> entry = iterator.next();
            AbstractArrow arrow = entry.getKey();
            LivingEntity target = entry.getValue().target;
            if (arrow == null || arrow.isRemoved() || arrow.getDeltaMovement().lengthSqr() < 0.01D) {
                iterator.remove();
                continue;
            }

            if (arrow.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.END_ROD,
                        arrow.getX(), arrow.getY(), arrow.getZ(),
                        3,
                        0.0, 0.0, 0.0,
                        0.01
                );

                serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME,
                        arrow.getX(), arrow.getY(), arrow.getZ(),
                        3,
                        0.05, 0.05, 0.05,
                        0.02
                );
            }

            if (target == null || !isValidTarget(target, arrow)) {
                if (!updateTarget(arrow)) {
                    if (entry.getValue().lastTimeTargeting > STOP_THRESHOLD) {
                        iterator.remove();
                    } else {
                        entry.getValue().lastTimeTargeting++;
                    }
                    continue;
                }
                target = TRACKED_ARROWS.get(arrow).target;
            }
            if (target == null) {
                if (entry.getValue().lastTimeTargeting > STOP_THRESHOLD) {
                    iterator.remove();
                } else {
                    entry.getValue().lastTimeTargeting++;
                }
                continue;
            }
            entry.getValue().lastTimeTargeting = 0;

            BlockPos arrowPos = arrow.blockPosition();
            BlockState blockState = arrow.level().getBlockState(arrowPos);
            boolean isInsideBlock = false;
            if (!blockState.isAir()) {
                VoxelShape voxelShape = blockState.getShape(arrow.level(), arrowPos);
                if (!voxelShape.isEmpty()) {
                    for (AABB aabb : voxelShape.toAabbs()) {
                        if (aabb.move(arrowPos).contains(arrow.position())) {
                            isInsideBlock = true;
                            break;
                        }
                    }
                }
            }
            arrow.setNoPhysics(isInsideBlock);

            int ticks = arrow.tickCount;
            double speed = Math.max(arrow.getDeltaMovement().length(), 1.5D);
            Vec3 targetPos = target.getEyePosition();
            Vec3 vec3 = targetPos.subtract(arrow.position()).normalize().scale(speed);
            Vec3 smoothedMovement = arrow.getDeltaMovement().lerp(vec3, ticks * 0.01).normalize().scale(speed);
            arrow.setDeltaMovement(smoothedMovement);
            double d0 = smoothedMovement.horizontalDistance();
            arrow.setYRot((float)(Mth.atan2(smoothedMovement.x, smoothedMovement.z) * (double)(180F / (float)Math.PI)));
            arrow.setXRot((float)(Mth.atan2(smoothedMovement.y, d0) * (double)(180F / (float)Math.PI)));
            arrow.yRotO = arrow.getYRot();
            arrow.xRotO = arrow.getXRot();

            arrow.hasImpulse = true;
        }
    }

    @SuppressWarnings("removal")
    @SubscribeEvent
    public static void onArrowImpact(ProjectileImpactEvent event) {
        if (event.getProjectile() instanceof AbstractArrow arrow && event.getRayTraceResult().getType() == HitResult.Type.BLOCK && (TRACKED_ARROWS.containsKey(arrow) || arrow.isNoPhysics())) {
            event.setCanceled(true);
        }
    }
}
