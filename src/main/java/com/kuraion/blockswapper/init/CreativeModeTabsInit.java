package com.kuraion.blockswapper.init;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CreativeModeTabsInit {
    @SubscribeEvent
    public static void addItems(CreativeModeTabEvent.BuildContents event) {
        if (event.getTab() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ItemsInit.COPPER_BLOCK_SWAPPER);
            event.accept(ItemsInit.NETHERITE_BLOCK_SWAPPER);
        }
    }
}
