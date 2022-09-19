package com.kuraion.blockswapper.items;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;

public class BlockSwapperItem extends TieredItem {
    private Tier tier;

    public BlockSwapperItem(Tier tier, Properties properties) {
        super(tier, properties);
    }
}
