package com.kuraion.blockswapper.enchantments;

import com.kuraion.blockswapper.ServerConfig;
import com.kuraion.blockswapper.init.ItemsInit;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.MendingEnchantment;

public class ExcavatingEnchantment extends Enchantment {
    public ExcavatingEnchantment(Enchantment.Rarity rarity, EquipmentSlot... slots) {super(rarity, EnchantmentCategory.DIGGER, slots);}

    @Override
    public int getMinCost(int enchantmentLevel) {return enchantmentLevel * 25;}

    @Override
    public int getMaxCost(int enchantmentLevel) {return this.getMinCost(enchantmentLevel) + 50;}

    @Override
    public int getMaxLevel() {return 1;}

    @Override
    public boolean checkCompatibility(Enchantment ench) {return !(ench instanceof MendingEnchantment) && super.checkCompatibility(ench);}

    @Override
    public boolean isTreasureOnly() {return ServerConfig.getEnchantmentEnabled() && !ServerConfig.getEnchantmentSource(ServerConfig.ENABLE_TABLE);}

    @Override
    public boolean isTradeable() {return ServerConfig.getEnchantmentEnabled() && ServerConfig.getEnchantmentSource(ServerConfig.ENABLE_TRADE);}

    @Override
    public boolean isDiscoverable() {return ServerConfig.getEnchantmentEnabled() && ServerConfig.getEnchantmentSource(ServerConfig.ENABLE_LOOT);}

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return ServerConfig.getEnchantmentEnabled()
               && ServerConfig.getEnchantmentSource(ServerConfig.ENABLE_TABLE)
               && (stack.getItem() == ItemsInit.DIAMOND_BLOCK_SWAPPER.get() || stack.getItem() == Items.BOOK);
    }

    @Override
    public boolean canEnchant(ItemStack stack) {return (stack.getItem() == ItemsInit.DIAMOND_BLOCK_SWAPPER.get() || stack.getItem() == Items.BOOK) && ServerConfig.getEnchantmentEnabled();}

    @Override
    public boolean isAllowedOnBooks() {return ServerConfig.getEnchantmentEnabled();}
}
