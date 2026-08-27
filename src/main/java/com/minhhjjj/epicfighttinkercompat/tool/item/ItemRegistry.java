package com.minhhjjj.epicfighttinkercompat.tool.item;

import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.common.registration.ItemDeferredRegisterExtension;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EpicFightTinkerCompat.MODID);
    public static final ItemDeferredRegisterExtension TOOLS = new ItemDeferredRegisterExtension(EpicFightTinkerCompat.MODID);

    public static final RegistryObject<Item> STICKY_HANDLE = ITEMS.register("sticky_handle",
            () -> new Item(new Item.Properties())); 

    public static final RegistryObject<Item> WIND_ESSENCE = ITEMS.register("wind_essence",
            () -> new Item(new Item.Properties()));

    public static final ItemObject<ModifiableItem> ODACHI;
    public static final ItemObject<ModifiableItem> ODACHI_SHEATH;
    public static final ItemObject<ModifiableItem> UNSHEATHED_ODACHI;

    static {
        ODACHI = TOOLS.register("odachi", () -> new ModifiableItem((new Item.Properties()).stacksTo(1), EFTToolDefinitions.ODACHI));
        ODACHI_SHEATH = TOOLS.register("odachi_sheath", () -> new ModifiableItem((new Item.Properties()).stacksTo(1), EFTToolDefinitions.ODACHI_SHEATH));
        UNSHEATHED_ODACHI = TOOLS.register("unsheathed_odachi", () -> new ModifiableItem((new Item.Properties()).stacksTo(1), EFTToolDefinitions.UNSHEATHED_ODACHI));
    }
}