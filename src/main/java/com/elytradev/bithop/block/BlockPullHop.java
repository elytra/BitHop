package com.elytradev.bithop.block;

import com.elytradev.bithop.BitHop;
import com.elytradev.bithop.tile.TileEntityPullHop;
import com.google.common.base.Predicate;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class BlockPullHop extends BlockHopTE<TileEntityPullHop> {

    public BlockPullHop(Material material, String name) {
        super(material, name);
        this.setDefaultState(blockState.getBaseState());

        setCreativeTab(BitHop.creativeTab);
    }

    @Override
    public int getGuiId() {
        return 3;
    }

    @Override
    public Class<TileEntityPullHop> getTileEntityClass() {
        return TileEntityPullHop.class;
    }

    @Override
    public TileEntityPullHop createTileEntity(World world, IBlockState state) {
        return new TileEntityPullHop();
    }

    @NotNull
    @Override
    public Predicate<EnumFacing> getFacingFilter() {
        return FacingFiltersKt.getNoUpFacingFilter();
    }
}
