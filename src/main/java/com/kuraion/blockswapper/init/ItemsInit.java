package com.kuraion.blockswapper.init;

import com.kuraion.blockswapper.BlockSwapperMod;
import com.kuraion.blockswapper.items.BlockSwapperItem;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ItemsInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BlockSwapperMod.MODID);

    public static final RegistryObject<Item> NETHERITE_BLOCK_SWAPPER = ITEMS.register("netherite_block_swapper", () -> new BlockSwapperItem(Tiers.NETHERITE, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).fireResistant()));
    public static final RegistryObject<Item> COPPER_BLOCK_SWAPPER = ITEMS.register("copper_block_swapper", () -> new BlockSwapperItem(new Tier() {
        @Override
        public int getUses() {
            return 375;
        }

        @Override
        public float getSpeed() {
            return 5;
        }

        @Override
        public float getAttackDamageBonus() {
            return 2;
        }

        @Override
        public int getLevel() {
            return 0;
        }

        @Override
        public int getEnchantmentValue() {
            return 0;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(Items.COPPER_BLOCK);
        }
    }, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
}