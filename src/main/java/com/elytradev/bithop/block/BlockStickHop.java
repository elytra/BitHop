package com.elytradev.bithop.block;

import com.elytradev.bithop.BitHop;
import com.elytradev.bithop.tile.TileEntityStickHop;
import com.google.common.base.Predicate;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

public class BlockStickHop extends BlockHopTE<TileEntityStickHop> {

    public BlockStickHop(Material material, String name) {
        super(material, name);
        this.setDefaultState(blockState.getBaseState());

        setCreativeTab(BitHop.creativeTab);
    }

    @Override
    public int getGuiId() {
        return 4;
    }

    @Override
    public Class<TileEntityStickHop> getTileEntityClass() {
        return TileEntityStickHop.class;
    }

    @Override
    public TileEntityStickHop createTileEntity(World world, IBlockState state) {
        return new TileEntityStickHop();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @NotNull
    @Override
    public Predicate<EnumFacing> getFacingFilter() {
        return FacingFiltersKt.getNoUpFacingFilter();
    }
}
