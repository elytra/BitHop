package com.elytradev.bithop.block;

import com.elytradev.bithop.BitHop;
import com.elytradev.bithop.tile.TileEntityFluxHop;
import com.google.common.base.Predicate;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class BlockFluxHop extends BlockHopTE<TileEntityFluxHop> {

    public BlockFluxHop(Material material, String name) {
        super(material, name);
        this.setDefaultState(blockState.getBaseState());

        setCreativeTab(BitHop.creativeTab);
    }

    @Override
    public int getGuiId() {
        return 1;
    }

    @Override
    public Class<TileEntityFluxHop> getTileEntityClass() {
        return TileEntityFluxHop.class;
    }

    @Override
    public TileEntityFluxHop createTileEntity(World world, IBlockState state) {
        return new TileEntityFluxHop();
    }

    @NotNull
    @Override
    public Predicate<EnumFacing> facingFilter() {
        return FacingFiltersKt.getNoUpFacingFilter();
    }
}
