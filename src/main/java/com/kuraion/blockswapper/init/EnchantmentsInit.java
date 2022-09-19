package com.kuraion.blockswapper.init;

import com.kuraion.blockswapper.BlockSwapperMod;
import com.kuraion.blockswapper.enchantments.ExcavatingEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EnchantmentsInit {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, BlockSwapperMod.MODID);

    public static final RegistryObject<Enchantment> EXCAVATING = ENCHANTMENTS.register("excavating", () -> new ExcavatingEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND));
}
