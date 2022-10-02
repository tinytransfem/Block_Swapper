package com.kuraion.blockswapper;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;
import java.util.List;

public class ServerConfig {
    public static final String CATEGORY_CONFIG = "Config";
    public static final String CATEGORY_GENERAL = "General";
    public static final String CATEGORY_ENCHANTMENT = "Enchantment";

    public static ForgeConfigSpec SERVER_CONFIG;

    public static ForgeConfigSpec.BooleanValue ENABLE_EXCAVATING;
    public static ForgeConfigSpec.BooleanValue ENABLE_FATIGUE;
    public static ForgeConfigSpec.BooleanValue ENABLE_CREATIVE;

    public static ForgeConfigSpec.BooleanValue ENABLE_TABLE;
    public static ForgeConfigSpec.BooleanValue ENABLE_TRADE;
    public static ForgeConfigSpec.BooleanValue ENABLE_LOOT;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> SWAP_BLACKLIST;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> EXCAVATE_BLACKLIST;

    public static Boolean getEnchantmentEnabled() { return ENABLE_EXCAVATING.get(); }

    public static Boolean doesFatigueAffect() { return ENABLE_FATIGUE.get(); }

    public static Boolean doesCreativeAffect() { return ENABLE_CREATIVE.get(); }

    public static Boolean getSwapDisabled(Block block) { return SWAP_BLACKLIST.get().contains(ForgeRegistries.BLOCKS.getKey(block).toString()); }

    public static Boolean getExcavateDisabled(Block block) { return EXCAVATE_BLACKLIST.get().contains(ForgeRegistries.BLOCKS.getKey(block).toString()); }

    public static Boolean getEnchantmentSource(ForgeConfigSpec.BooleanValue source) {
        return source.get();
    }

    static {

        ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();

        SERVER_BUILDER.push(CATEGORY_CONFIG);

        setupGeneral(SERVER_BUILDER);
        setupEnchant(SERVER_BUILDER);

        SERVER_BUILDER.pop();

        SERVER_CONFIG = SERVER_BUILDER.build();

    }

    private static void setupGeneral(ForgeConfigSpec.Builder SERVER_BUILDER) {

        SERVER_BUILDER.push(CATEGORY_GENERAL);

        SERVER_BUILDER.comment("Enable swapping and excavating of ALL blocks when in creative mode", "default = true");
        ENABLE_CREATIVE = SERVER_BUILDER.define("affectedByCreativeMode", true);

        SERVER_BUILDER.comment("\nDisable block swapping when under the effect of Mining Fatigue", "default = true");
        ENABLE_FATIGUE = SERVER_BUILDER.define("affectedByMiningFatigue", true);

        SERVER_BUILDER.comment("\nGeneral block blacklist (swapping & excavating)", "example = [\"minecraft:stone\", \"minecraft:oak_log\"]", "default = [\"minecraft:spawner\"]");
        SWAP_BLACKLIST = SERVER_BUILDER.defineList("swapBlacklist", List.of("minecraft:spawner"), entry -> true );

        SERVER_BUILDER.pop();
    }

    private static void setupEnchant(ForgeConfigSpec.Builder SERVER_BUILDER) {

        SERVER_BUILDER.push(CATEGORY_ENCHANTMENT);

        SERVER_BUILDER.comment("Enable the Excavating enchantment", "default = true");
        ENABLE_EXCAVATING = SERVER_BUILDER.define("enableExcavating", true);

        SERVER_BUILDER.comment("\nEnable getting the Excavating enchantment from enchantment tables", "default = false");
        ENABLE_TABLE = SERVER_BUILDER.define("ExcavatingFromEnchantmentTable", false);

        SERVER_BUILDER.comment("\nEnable villagers to trade Excavating books", "default = false");
        ENABLE_TRADE = SERVER_BUILDER.define("ExcavatingCanBeTraded", false);

        SERVER_BUILDER.comment("\nEnable Excavating books generating in loot chests", "default = true");
        ENABLE_LOOT = SERVER_BUILDER.define("ExcavatingFromLootChests", true);

        SERVER_BUILDER.comment("\nExcavating block blacklist (only excavating)", "example = [\"minecraft:obsidian\", \"minecraft:anvil\"]", "default = []");
        EXCAVATE_BLACKLIST = SERVER_BUILDER.defineList("excavateBlacklist", List.of(), entry -> true );

        SERVER_BUILDER.pop();
    }
}