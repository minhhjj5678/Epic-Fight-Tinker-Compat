package com.minhhjjj.epicfighttinkercompat;

import com.minhhjjj.epicfighttinkercompat.gameasset.profiles.ModifierProfileReloadListener;
import com.minhhjjj.epicfighttinkercompat.modifiers.ChargeableModule;
import com.minhhjjj.epicfighttinkercompat.modifiers.TagLimitModule;
import com.minhhjjj.epicfighttinkercompat.network.NetworkManager;
import com.minhhjjj.epicfighttinkercompat.tool.capabilities.TinkerWeaponCapabilityProvider;
import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraftforge.resource.PathPackResources;
import slimeknights.tconstruct.library.materials.MaterialRegistry;

import com.minhhjjj.epicfighttinkercompat.stats.EpicFightBindingStats;
import com.minhhjjj.epicfighttinkercompat.stats.EpicFightHandleStats;
import com.minhhjjj.epicfighttinkercompat.stats.EpicFightHeadStats;
import com.minhhjjj.epicfighttinkercompat.stats.EpicFightToolStats;
import com.minhhjjj.epicfighttinkercompat.stats.armor.EpicFightHelmetStats;
import com.minhhjjj.epicfighttinkercompat.stats.armor.EpicFightChestplateStats;
import com.minhhjjj.epicfighttinkercompat.stats.armor.EpicFightLeggingsStats;
import com.minhhjjj.epicfighttinkercompat.stats.armor.EpicFightBootsStats;
import com.minhhjjj.epicfighttinkercompat.stats.armor.EpicFightMailleStats;
import com.minhhjjj.epicfighttinkercompat.modifiers.EpicFightModifiers;
import com.minhhjjj.epicfighttinkercompat.tool.item.ItemRegistry;
import com.minhhjjj.epicfighttinkercompat.client.armor.ArmorTextureBaker;

import org.slf4j.Logger;
import slimeknights.tconstruct.library.modifiers.ModifierManager;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;

@Mod(EpicFightTinkerCompat.MODID)
public class EpicFightTinkerCompat
{
    public static final String MODID = "epicfighttinkercompat";
    public static final Logger LOGGER = LogUtils.getLogger();

    public EpicFightTinkerCompat(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();
        EpicFightModifiers.MODIFIERS.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
//        modEventBus.addListener(this::onAddPackFinders);
        MinecraftForge.EVENT_BUS.register(this);
        ItemRegistry.ITEMS.register(modEventBus);

        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(TinkerWeaponCapabilityProvider::register);
    }

    @SuppressWarnings("null")
    private void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
        EpicFightToolStats.register();
        NetworkManager.register();
        ModifierModule.LOADER.register(ResourceLocation.fromNamespaceAndPath(MODID, "tag_limit"), TagLimitModule.LOADER);
        ModifierModule.LOADER.register(ResourceLocation.fromNamespaceAndPath(MODID, "chargeable"), ChargeableModule.LOADER);
        MaterialRegistry.getInstance().registerStatType(EpicFightHandleStats.TYPE);
        MaterialRegistry.getInstance().registerStatType(EpicFightHeadStats.TYPE);
        MaterialRegistry.getInstance().registerStatType(EpicFightBindingStats.TYPE);
        MaterialRegistry.getInstance().registerStatType(EpicFightHelmetStats.TYPE);
        MaterialRegistry.getInstance().registerStatType(EpicFightChestplateStats.TYPE);
        MaterialRegistry.getInstance().registerStatType(EpicFightLeggingsStats.TYPE);
        MaterialRegistry.getInstance().registerStatType(EpicFightBootsStats.TYPE);
        MaterialRegistry.getInstance().registerStatType(EpicFightMailleStats.TYPE);


    });
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ItemRegistry.STICKY_HANDLE.get());
            event.accept(ItemRegistry.WIND_ESSENCE.get());
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }

    @SubscribeEvent
    public void onAddReloadListeners(AddReloadListenerEvent event)
    {
        event.addListener(new EpicFightCacheReloadListener());
        event.addListener(new ModifierProfileReloadListener());
    }

    private static class EpicFightCacheReloadListener extends SimplePreparableReloadListener<Void> {
        @Override
        protected Void prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
            return null;
        }

        @Override
        protected void apply(Void unused, ResourceManager resourceManager, ProfilerFiller profiler) {
            LOGGER.info("Reloaded Epic Fight material stat cache");
        }
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class PackRegistrar {
        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void onAddPackFinders(AddPackFindersEvent event) {
            if (event.getPackType() == PackType.SERVER_DATA) {
                var resourcePath = ModList.get().getModFileById(MODID).getFile().findResource("datapacks", "epicfight_override");

                var pack = Pack.readMetaAndCreate(
                        MODID + "_epicfight_override",
                        Component.literal("Epic Fight Override Datapack"),
                        true,
                        (path) -> new PathPackResources(path, true, resourcePath),
                        PackType.SERVER_DATA,
                        Pack.Position.TOP,
                        PackSource.BUILT_IN
                );

                if (pack != null) {
                    LOGGER.info("[EpicFight TiC Compat] Registering built-in datapack: " + pack.getId());
                    event.addRepositorySource((packConsumer) -> packConsumer.accept(pack));
                }
            }
        }
    }


    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
        }

        @SubscribeEvent
        public static void onRegisterClientReloadListeners(RegisterClientReloadListenersEvent event)
        {
            event.registerReloadListener(new EpicFightCacheReloadListener());
            if (!ModList.get().isLoaded("epictinkersarmorfix")) {
                event.registerReloadListener((ResourceManagerReloadListener) manager -> {
                    LOGGER.info("[EpicFight TiC Compat] Reloading resource packs, clearing baked armor cache...");
                    ArmorTextureBaker.clearCache();
                });
            }
        }
    }
}
