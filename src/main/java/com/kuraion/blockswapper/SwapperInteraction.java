package com.kuraion.blockswapper;

import com.kuraion.blockswapper.init.EnchantmentsInit;
import com.kuraion.blockswapper.init.ItemsInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SwapperInteraction {
    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Direction facing = event.getFace();
        BlockPos pos = event.getPos();
        Level world = event.getLevel();

        ItemStack heldMain = player.getMainHandItem();
        ItemStack heldOff = player.getOffhandItem();

        BlockState oldBlockState = world.getBlockState(pos);
        BlockState newBlockState = null;
        if (heldOff.getItem() instanceof BlockItem _bi) newBlockState = _bi.getBlock().withPropertiesOf(oldBlockState);
        else if (!player.getOffhandItem().isEmpty()){
            return;
        }

        boolean notFacingBlock = (facing == null);
        boolean playerCanEditBlock = player.mayUseItemAt(pos.offset(facing.getNormal()), facing, heldMain);
        boolean noOffhand = player.getOffhandItem().isEmpty();
        if (noOffhand == true && player.getMainHandItem().getEnchantmentLevel(EnchantmentsInit.EXCAVATING.get()) == 1) {
            noOffhand = false;
            newBlockState = Blocks.AIR.defaultBlockState();
        }
        boolean canMine = player.isCreative();
        boolean isSwapper = true;
        boolean isEntity = oldBlockState.hasBlockEntity();

        if (heldMain.is(ItemsInit.COPPER_BLOCK_SWAPPER.get())) {
            if  (Items.STONE_AXE.isCorrectToolForDrops(oldBlockState) ||
                Items.STONE_PICKAXE.isCorrectToolForDrops(oldBlockState) ||
                Items.STONE_SHOVEL.isCorrectToolForDrops(oldBlockState) ||
                Items.STONE_SWORD.isCorrectToolForDrops(oldBlockState) ||
                Items.STONE_HOE.isCorrectToolForDrops(oldBlockState)) {
                    canMine = true;
            }
        } else if (heldMain.is(ItemsInit.DIAMOND_BLOCK_SWAPPER.get())) {
            if  (Items.DIAMOND_AXE.isCorrectToolForDrops(oldBlockState) ||
                Items.DIAMOND_PICKAXE.isCorrectToolForDrops(oldBlockState) ||
                Items.DIAMOND_SHOVEL.isCorrectToolForDrops(oldBlockState) ||
                Items.DIAMOND_SWORD.isCorrectToolForDrops(oldBlockState) ||
                Items.DIAMOND_HOE.isCorrectToolForDrops(oldBlockState)) {
                    canMine = true;
            }
        } else isSwapper = false;

        if (notFacingBlock || noOffhand || !isSwapper || !playerCanEditBlock || isEntity) return;
        if (newBlockState.equals(oldBlockState) || !canMine) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.FAIL);
            return;
        }

        world.playSound(player, pos, oldBlockState.getSoundType().getBreakSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
        if(!world.isClientSide()) {
            world.destroyBlock(pos, !player.isCreative());
            world.setBlock(pos, newBlockState, 11);
            if (!player.isCreative()) heldOff.shrink(1);
            world.gameEvent(GameEvent.BLOCK_CHANGE, event.getPos(), GameEvent.Context.of(player, newBlockState));
            heldMain.hurtAndBreak(1, player, (pl) -> { pl.broadcastBreakEvent(EquipmentSlot.MAINHAND); });
        }

        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
    }
}
