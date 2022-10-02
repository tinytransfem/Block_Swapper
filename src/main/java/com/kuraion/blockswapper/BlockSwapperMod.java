package com.kuraion.blockswapper;

import com.kuraion.blockswapper.init.EnchantmentsInit;
import com.kuraion.blockswapper.init.ItemsInit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Mod("blockswapper")
public class BlockSwapperMod {
    public static final Logger LOGGER = LogManager.getLogger(BlockSwapperMod.class);
    public static final String MODID = "blockswapper";
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, MODID), () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
    private static int messageID = 0;

    public BlockSwapperMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ItemsInit.ITEMS.register(modBus);
        EnchantmentsInit.ENCHANTMENTS.register(modBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ServerConfig.SERVER_CONFIG);

        MinecraftForge.EVENT_BUS.addListener(SwapperInteraction::onRightClick);
    }

    public static <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder,
                                             BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
        PACKET_HANDLER.registerMessage(messageID, messageType, encoder, decoder, messageConsumer);
        messageID++;
    }
}
