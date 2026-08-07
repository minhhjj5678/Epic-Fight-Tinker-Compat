package com.minhhjjj.epicfighttinkercompat.mixin.wom;

import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.objectweb.asm.tree.ClassNode;
import net.minecraftforge.fml.loading.FMLLoader;

import java.util.List;
import java.util.Set;

public class OptionalCompatMixinPlugin implements IMixinConfigPlugin {

    private boolean isWomLoaded = false;

    @Override
    public void onLoad(String mixinPackage) {
        try {
            isWomLoaded = FMLLoader.getLoadingModList().getModFileById("wom") != null;
        } catch (Exception e) {
            isWomLoaded = false;
        }
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return isWomLoaded;
    }

    @Override
    public String getRefMapperConfig() { return null; }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() { return null; }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}