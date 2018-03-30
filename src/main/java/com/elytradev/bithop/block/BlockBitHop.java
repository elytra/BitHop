package com.elytradev.bithop.block;

import com.elytradev.bithop.BitHop;
import com.elytradev.bithop.tile.TileEntityBitHopKt;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockBitHop extends BlockTileEntity<TileEntityBitHopKt> {

    protected String name;

    public BlockBitHop(Material material, String name) {
        super(material, name);
        this.setDefaultState(blockState.getBaseState());

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

//    public int getMetaFromState(IBlockState state){
//        return state.getValue(IS_BRASS) ? 1 : 0;
//    }
//
//    public IBlockState getStateFromMeta(int meta){
//        return getDefaultState().withProperty(IS_BRASS, meta == 1);
//    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
        return this.getDefaultState();
    }
}
