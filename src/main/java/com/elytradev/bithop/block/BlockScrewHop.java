package com.elytradev.bithop.block;

import com.elytradev.bithop.BitHop;
import com.elytradev.bithop.tile.TileEntityScrewHop;
import com.google.common.base.Predicate;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public class BlockScrewHop extends BlockHopTE<TileEntityScrewHop> {

    private static final AxisAlignedBB x = new AxisAlignedBB(4/16.0, 0.0D, 0.0D, 12/16.0, 1.0D, 1.0D);
    private static final AxisAlignedBB z = new AxisAlignedBB(0.0D, 0.0D, 4/16.0, 1.0D, 1.0D, 12/16.0);

    public BlockScrewHop(Material material, String name) {
        super(material, name);
        this.setDefaultState(blockState.getBaseState());

        setCreativeTab(BitHop.creativeTab);
    }

    @Override
    public int getGuiId() {
        return 2;
    }

    @Override
    public Class<TileEntityScrewHop> getTileEntityClass() {
        return TileEntityScrewHop.class;
    }

    @Override
    public TileEntityScrewHop createTileEntity(World world, IBlockState state) {
        return new TileEntityScrewHop();
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        EnumFacing enumfacing = facing.getOpposite();

        if (enumfacing == EnumFacing.UP || enumfacing == EnumFacing.DOWN)
        {
            enumfacing = placer.getHorizontalFacing();
        }

        return this.getDefaultState().withProperty(getFACING(), enumfacing);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntityScrewHop tile = getTileEntity(world, pos);
        IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
        for(int i=0;i<5;i++) {
            ItemStack stack = itemHandler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
            }
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
        switch(state.getValue(getFACING())) {
            case NORTH:
                return x;
            case SOUTH:
                return x;
            case EAST:
                return z;
            case WEST:
                return z;
            case UP:
                return x;
            case DOWN:
                return x;
            default:
                throw new AssertionError("Case missing for ScrewHop AABB"); //this should NEVER happen
        }
    }

    @NotNull
    @Override
    public Predicate<EnumFacing> facingFilter() {
        return FacingFiltersKt.getScrewHopFacingFilter();
    }
}
