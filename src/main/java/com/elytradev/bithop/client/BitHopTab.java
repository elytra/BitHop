package com.elytradev.bithop.client;

import com.elytradev.bithop.BitHop;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.creativetab.CreativeTabs;

public class BitHopTab extends CreativeTabs {
    public BitHopTab() {
        super(BitHop.modId);
        //setBackgroundImageName("betterboilers.png");
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(Blocks.HOPPER);
    }
}
