package com.elytradev.bithop.block;

import com.elytradev.bithop.tile.TileEntityFluxHop;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public abstract class BlockTileEntity<TE extends TileEntity> extends BlockBase {
    public BlockTileEntity(Material material, String name) {
        super(material, name);
    }

    public abstract Class<TE> getTileEntityClass();

    public TE getTileEntity(IBlockAccess world, BlockPos pos) {
        return (TE)world.getTileEntity(pos);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public abstract TE createTileEntity(World world, IBlockState state);

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        IItemHandler itemHandler = getTileEntity(world, pos).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
        if (itemHandler != null) {
            for(int i=0;i<itemHandler.getSlots();i++) {
                ItemStack stack = itemHandler.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
                }
            }
        }
        super.breakBlock(world, pos, state);
    }
}
