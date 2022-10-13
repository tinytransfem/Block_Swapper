package com.kuraion.blockswapper;

import com.kuraion.blockswapper.init.EnchantmentsInit;
import com.kuraion.blockswapper.init.ItemsInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SwapperInteraction {
    @SubscribeEvent
    public static void swapperMainHandInteraction(PlayerInteractEvent.RightClickBlock event) {

        //create and fill variables
        Player player = event.getEntity();
        Direction facing = event.getFace();
        BlockPos pos = event.getPos();
        Level world = event.getLevel();

        ItemStack heldMain = player.getMainHandItem();
        ItemStack heldOff = player.getOffhandItem();

        BlockState oldBlockState = world.getBlockState(pos);
        BlockState newBlockState = Blocks.AIR.defaultBlockState();

        SoundType oldSound = oldBlockState.getSoundType();

        //create and fill prerequisites
        boolean facingBlock = (facing != null);
        boolean playerCanEditBlock = player.mayUseItemAt(pos.offset(facing.getNormal()), facing, heldMain);
        boolean creative = player.isCreative() && ServerConfig.doesCreativeAffect();
        boolean canMine = creative;
        boolean notEntity = !oldBlockState.hasBlockEntity();
        boolean notIllegalPlanting = ServerConfig.canPlant() || (!player.getOffhandItem().is(Tags.Items.SEEDS) && !player.getOffhandItem().is(Tags.Items.CROPS));

        //validate swapper hand
        if (heldMain.is(ItemsInit.COPPER_BLOCK_SWAPPER.get())) {
            if  (Items.STONE_AXE.isCorrectToolForDrops(oldBlockState) ||
                    Items.STONE_PICKAXE.isCorrectToolForDrops(oldBlockState) ||
                    Items.STONE_SHOVEL.isCorrectToolForDrops(oldBlockState) ||
                    Items.STONE_SWORD.isCorrectToolForDrops(oldBlockState) ||
                    Items.STONE_HOE.isCorrectToolForDrops(oldBlockState)) {
                canMine = true;
            }
        } else if (heldMain.is(ItemsInit.NETHERITE_BLOCK_SWAPPER.get())) {
            if  (Items.DIAMOND_AXE.isCorrectToolForDrops(oldBlockState) ||
                    Items.DIAMOND_PICKAXE.isCorrectToolForDrops(oldBlockState) ||
                    Items.DIAMOND_SHOVEL.isCorrectToolForDrops(oldBlockState) ||
                    Items.DIAMOND_SWORD.isCorrectToolForDrops(oldBlockState) ||
                    Items.DIAMOND_HOE.isCorrectToolForDrops(oldBlockState)) {
                canMine = true;
            }
        } else return;

        //validate block hand
        if (heldOff.getItem() instanceof BlockItem _bi) newBlockState = _bi.getBlock().withPropertiesOf(oldBlockState);
        else if (player.getMainHandItem().getEnchantmentLevel(EnchantmentsInit.EXCAVATING.get()) > 0 || creative) {
            if (ServerConfig.getExcavateDisabled(oldBlockState.getBlock()) && !creative) {
                world.playSound(player, pos, oldSound.getBreakSound(), SoundSource.BLOCKS, oldSound.getVolume(), oldSound.getPitch());
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
                return;
            }
        } else return;

        //validate prerequisites
        if (ServerConfig.getSwapDisabled(oldBlockState.getBlock())) canMine = false;
        if (player.hasEffect(MobEffect.byId(4)) && ServerConfig.doesFatigueAffect()) canMine = false;

        if (!facingBlock || !playerCanEditBlock || !notEntity || !notIllegalPlanting) return;
        if (newBlockState.equals(oldBlockState) || (!canMine && !creative)) {
            world.playSound(player, pos, oldSound.getBreakSound(), SoundSource.BLOCKS, oldSound.getVolume(), oldSound.getPitch());
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
            return;
        }

        //set drops
        boolean normalDrops = !player.isCreative();
        ItemStack drops = null;
        if (normalDrops && player.getMainHandItem().getEnchantmentLevel(Enchantments.SILK_TOUCH) > 0) {
            normalDrops = false;
            drops = oldBlockState.getBlock().asItem().getDefaultInstance();
        }

        //execute swap
        world.playSound(player, pos, oldSound.getBreakSound(), SoundSource.BLOCKS, oldSound.getVolume(), oldSound.getPitch());
        if(!world.isClientSide()) {
            world.destroyBlock(pos, normalDrops);
            if (!player.isCreative() && !normalDrops) world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), drops));
            if (newBlockState.hasProperty(BlockStateProperties.PERSISTENT) && !oldBlockState.hasProperty(BlockStateProperties.PERSISTENT)) {
                world.setBlock(pos, newBlockState.setValue(BlockStateProperties.PERSISTENT, true), 11);
                world.gameEvent(GameEvent.BLOCK_CHANGE, event.getPos(), GameEvent.Context.of(player, newBlockState.setValue(BlockStateProperties.PERSISTENT, true)));
            } else {
                world.setBlock(pos, newBlockState, 11);
                world.gameEvent(GameEvent.BLOCK_CHANGE, event.getPos(), GameEvent.Context.of(player, newBlockState));
            }
            if (!player.isCreative() && newBlockState != Blocks.AIR.defaultBlockState()) heldOff.shrink(1);
            heldMain.hurtAndBreak(1, player, (pl) -> { pl.broadcastBreakEvent(EquipmentSlot.MAINHAND); });
        }
        SoundType newSound = newBlockState.getSoundType();
        world.playSound(player, pos, newSound.getPlaceSound(), SoundSource.BLOCKS, newSound.getVolume(), newSound.getPitch());

        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
    }

    @SubscribeEvent
    public static void swapperOffhandInteraction(PlayerInteractEvent.RightClickBlock event) {

        //create and fill variables
        Player player = event.getEntity();
        Direction facing = event.getFace();
        BlockPos pos = event.getPos();
        Level world = event.getLevel();

        ItemStack heldMain = player.getMainHandItem();
        ItemStack heldOff = player.getOffhandItem();

        BlockState oldBlockState = world.getBlockState(pos);
        BlockState newBlockState = Blocks.AIR.defaultBlockState();

        SoundType oldSound = oldBlockState.getSoundType();

        //create and fill prerequisites
        boolean facingBlock = (facing != null);
        boolean playerCanEditBlock = player.mayUseItemAt(pos.offset(facing.getNormal()), facing, heldOff);
        boolean creative = player.isCreative() && ServerConfig.doesCreativeAffect();
        boolean canMine = creative;
        boolean notEntity = !oldBlockState.hasBlockEntity();
        boolean notIllegalPlanting = ServerConfig.canPlant() || (!player.getMainHandItem().is(Tags.Items.SEEDS) && !player.getMainHandItem().is(Tags.Items.CROPS));

        //validate swapper hand
        if (heldOff.is(ItemsInit.COPPER_BLOCK_SWAPPER.get())) {
            if  (Items.STONE_AXE.isCorrectToolForDrops(oldBlockState) ||
                    Items.STONE_PICKAXE.isCorrectToolForDrops(oldBlockState) ||
                    Items.STONE_SHOVEL.isCorrectToolForDrops(oldBlockState) ||
                    Items.STONE_SWORD.isCorrectToolForDrops(oldBlockState) ||
                    Items.STONE_HOE.isCorrectToolForDrops(oldBlockState)) {
                canMine = true;
            }
        } else if (heldOff.is(ItemsInit.NETHERITE_BLOCK_SWAPPER.get())) {
            if  (Items.DIAMOND_AXE.isCorrectToolForDrops(oldBlockState) ||
                    Items.DIAMOND_PICKAXE.isCorrectToolForDrops(oldBlockState) ||
                    Items.DIAMOND_SHOVEL.isCorrectToolForDrops(oldBlockState) ||
                    Items.DIAMOND_SWORD.isCorrectToolForDrops(oldBlockState) ||
                    Items.DIAMOND_HOE.isCorrectToolForDrops(oldBlockState)) {
                canMine = true;
            }
        } else return;

        //validate block hand
        if (heldMain.getItem() instanceof BlockItem _bi) newBlockState = _bi.getBlock().withPropertiesOf(oldBlockState);
        else if (player.getOffhandItem().getEnchantmentLevel(EnchantmentsInit.EXCAVATING.get()) > 0 || creative) {
            if (ServerConfig.getExcavateDisabled(oldBlockState.getBlock()) && !creative) {
                world.playSound(player, pos, oldSound.getBreakSound(), SoundSource.BLOCKS, oldSound.getVolume(), oldSound.getPitch());
                player.swing(InteractionHand.OFF_HAND);
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.FAIL);
                return;
            }
        } else return;

        //validate prerequisites
        if (ServerConfig.getSwapDisabled(oldBlockState.getBlock())) canMine = false;
        if (player.hasEffect(MobEffect.byId(4)) && ServerConfig.doesFatigueAffect()) canMine = false;

        if (!facingBlock || !playerCanEditBlock || !notEntity || !notIllegalPlanting) return;
        if (newBlockState.equals(oldBlockState) || (!canMine && !creative)) {
            world.playSound(player, pos, oldSound.getBreakSound(), SoundSource.BLOCKS, oldSound.getVolume(), oldSound.getPitch());
            player.swing(InteractionHand.OFF_HAND);
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.FAIL);
            return;
        }

        //set drops
        boolean normalDrops = !player.isCreative();
        ItemStack drops = null;
        if (normalDrops && player.getOffhandItem().getEnchantmentLevel(Enchantments.SILK_TOUCH) > 0) {
            normalDrops = false;
            drops = oldBlockState.getBlock().asItem().getDefaultInstance();
        }

        //execute swap
        world.playSound(player, pos, oldSound.getBreakSound(), SoundSource.BLOCKS, oldSound.getVolume(), oldSound.getPitch());
        if(!world.isClientSide()) {
            world.destroyBlock(pos, normalDrops);
            if (!player.isCreative() && !normalDrops) world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), drops));
            if (newBlockState.hasProperty(BlockStateProperties.PERSISTENT) && !oldBlockState.hasProperty(BlockStateProperties.PERSISTENT)) {
                world.setBlock(pos, newBlockState.setValue(BlockStateProperties.PERSISTENT, true), 11);
                world.gameEvent(GameEvent.BLOCK_CHANGE, event.getPos(), GameEvent.Context.of(player, newBlockState.setValue(BlockStateProperties.PERSISTENT, true)));
            } else {
                world.setBlock(pos, newBlockState, 11);
                world.gameEvent(GameEvent.BLOCK_CHANGE, event.getPos(), GameEvent.Context.of(player, newBlockState));
            }
            if (!player.isCreative() && newBlockState != Blocks.AIR.defaultBlockState()) heldMain.shrink(1);
            heldOff.hurtAndBreak(1, player, (pl) -> { pl.broadcastBreakEvent(EquipmentSlot.OFFHAND); });
        }
        SoundType newSound = newBlockState.getSoundType();
        world.playSound(player, pos, newSound.getPlaceSound(), SoundSource.BLOCKS, newSound.getVolume(), newSound.getPitch());
        player.swing(InteractionHand.OFF_HAND);

        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.FAIL);
    }
}
