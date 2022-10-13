package com.kuraion.blockswapper.init;

import com.kuraion.blockswapper.BlockSwapperMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.MissingMappingsEvent;

import java.util.List;

@Mod.EventBusSubscriber(modid = BlockSwapperMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MissingMappings {

    @SubscribeEvent
    public static void missingMapping(MissingMappingsEvent event)
    {
        if (event.getKey() != ForgeRegistries.Keys.ITEMS)
            return;

        List<String> removedItems = List.of("diamond_block_swapper");
        for (MissingMappingsEvent.Mapping<Item> mapping : event.getAllMappings(ForgeRegistries.Keys.ITEMS))
        {
            ResourceLocation regName = mapping.getKey();
            if (regName != null && regName.getNamespace().equals("blockswapper"))
            {
                String path = regName.getPath();
                if (removedItems.stream().anyMatch(s -> s.equals(path)))
                {
                    switch (path) {
                        case "diamond_block_swapper":
                            mapping.remap(ItemsInit.NETHERITE_BLOCK_SWAPPER.get());
                            break;
                    }
                }
            }
        }
    }
}
