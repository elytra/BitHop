package com.elytradev.bithop.block;

import com.elytradev.bithop.BitHop;
import com.elytradev.bithop.tile.TileEntityBitHopKt;
import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockBitHop extends BlockTileEntity<TileEntityBitHopKt> {

    protected String name;

    public static final PropertyDirection FACING = PropertyDirection.create("facing", new Predicate<EnumFacing>()
    {
        public boolean apply(@Nullable EnumFacing p_apply_1_)
        {
            return p_apply_1_ != EnumFacing.UP;
        }
    });
    public static EnumFacing getFacing(int meta)
    {
        return EnumFacing.getFront(meta & 7);
    }

//    public static final PropertyBool ENABLED = PropertyBool.create("enabled");

    public BlockBitHop(Material material, String name) {
        super(material, name);
        this.setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.DOWN));

        setCreativeTab(BitHop.creativeTab);
    }

    @Override
    public Class<TileEntityBitHopKt> getTileEntityClass() {
        return TileEntityBitHopKt.class;
    }

    @Override
    public TileEntityBitHopKt createTileEntity(World world, IBlockState state) {
        return new TileEntityBitHopKt();
    }

    @Override
    public BlockStateContainer createBlockState(){
        return new BlockStateContainer(this);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, getFacing(meta));
    }

    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | ((EnumFacing)state.getValue(FACING)).getIndex();
        i |= 8;

        return i;
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        EnumFacing enumfacing = facing.getOpposite();

        if (enumfacing == EnumFacing.UP)
        {
            enumfacing = EnumFacing.DOWN;
        }

        return this.getDefaultState().withProperty(FACING, enumfacing);
    }
}
