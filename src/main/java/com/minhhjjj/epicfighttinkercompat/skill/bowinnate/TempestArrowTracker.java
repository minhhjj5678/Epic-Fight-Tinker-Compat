package com.minhhjjj.epicfighttinkercompat.skill.bowinnate;

import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

@Mod.EventBusSubscriber(modid = EpicFightTinkerCompat.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TempestArrowTracker {
    private static final Map<AbstractArrow, Vec3> TRACKED_ARROWS = new WeakHashMap<>();
    private static final int TICKS_BEFORE_TARGETING = 10;
    private static final double BOOST_SPEED = 3;

    public static void addArrow(AbstractArrow arrow, Vec3 targetPos) {
        if (arrow == null || targetPos == null) return;
        TRACKED_ARROWS.put(arrow, targetPos);
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START || TRACKED_ARROWS.isEmpty()) return;

        Iterator<Map.Entry<AbstractArrow, Vec3>> iterator = TRACKED_ARROWS.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<AbstractArrow, Vec3> entry = iterator.next();
            AbstractArrow arrow = entry.getKey();
            if (arrow == null) {
                iterator.remove();
                continue;
            }

            Vec3 currentPos = arrow.position();
            Vec3 nextPos = currentPos.add(arrow.getDeltaMovement());
            BlockHitResult hitResult = arrow.level().clip(new ClipContext(currentPos, nextPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, arrow));
            if (arrow.tickCount > TICKS_BEFORE_TARGETING || hitResult.getType() == HitResult.Type.BLOCK) {
                iterator.remove();
                double speed = arrow.getDeltaMovement().length() * (hitResult.getType() == HitResult.Type.BLOCK ? BOOST_SPEED * ((double) arrow.tickCount / TICKS_BEFORE_TARGETING) : BOOST_SPEED);
                Vec3 vec3 = entry.getValue().subtract(arrow.position()).normalize().scale(speed);
                arrow.setDeltaMovement(vec3);
                double d0 = vec3.horizontalDistance();
                arrow.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * (double)(180F / (float)Math.PI)));
                arrow.setXRot((float)(Mth.atan2(vec3.y, d0) * (double)(180F / (float)Math.PI)));
                arrow.yRotO = arrow.getYRot();
                arrow.xRotO = arrow.getXRot();
                arrow.hasImpulse = true;
            } else if (arrow.getDeltaMovement().length() < 1) {
                double speed = 1.0d;
                arrow.setDeltaMovement(arrow.getDeltaMovement().normalize().scale(speed));
                arrow.hasImpulse = true;
            }
        }
    }

}
