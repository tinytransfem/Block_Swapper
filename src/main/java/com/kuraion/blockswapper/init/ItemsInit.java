package com.kuraion.blockswapper.init;

import com.kuraion.blockswapper.BlockSwapperMod;
import com.kuraion.blockswapper.items.BlockSwapperItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ItemsInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BlockSwapperMod.MODID);

    public static final RegistryObject<Item> DIAMOND_BLOCK_SWAPPER = ITEMS.register("diamond_block_swapper", () -> new BlockSwapperItem(Tiers.DIAMOND, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
    public static final RegistryObject<Item> COPPER_BLOCK_SWAPPER = ITEMS.register("copper_block_swapper", () -> new BlockSwapperItem(Tiers.IRON, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
}