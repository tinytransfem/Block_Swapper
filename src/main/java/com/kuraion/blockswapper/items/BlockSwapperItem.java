package com.kuraion.blockswapper.items;

import com.kuraion.blockswapper.init.EnchantmentsInit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

public class BlockSwapperItem extends TieredItem {
    private Tier tier;

    public BlockSwapperItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return super.getMaxDamage(stack);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        return enchantment == Enchantments.SILK_TOUCH || enchantment == Enchantments.MENDING || enchantment == Enchantments.UNBREAKING || enchantment == EnchantmentsInit.EXCAVATING.get();
    }
}
