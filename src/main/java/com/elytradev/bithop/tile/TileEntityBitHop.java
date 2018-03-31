package com.elytradev.bithop.tile;

import com.elytradev.bithop.block.BlockBitHop;
import com.elytradev.bithop.block.ModBlocks;
import com.elytradev.concrete.inventory.ConcreteItemStorage;
import com.elytradev.concrete.inventory.IContainerInventoryHolder;
import com.elytradev.concrete.inventory.ValidatedInventoryView;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class TileEntityBitHop extends TileEntity implements IContainerInventoryHolder {

    public ConcreteItemStorage inv;
    public EnumFacing direction;

    public TileEntityBitHop() {
        this.inv = new ConcreteItemStorage(5).withName(ModBlocks.BITHOP.getUnlocalizedName() + ".name");
        EnumFacing enumfacing = BlockBitHop.getFacing(this.getBlockMetadata()).getOpposite();
        inv.listen(this::markDirty);
        inv.listen(this::pushItems);
    }

    @Override
    public IInventory getContainerInventory() {
        ValidatedInventoryView view = new ValidatedInventoryView(inv);
        return view;
    }

    public void pushItems() {
        int slot = getFirstFullSlot();
        if (slot != -1) {
            
        }
    }

    private int getFirstFullSlot() {
        int firstFullSlot = -1;
        for (int i = 4; i==0; i--) {
            if (inv.getCanExtract(i)) {
                firstFullSlot = i;
            }
        }
        return firstFullSlot;
    }

}
