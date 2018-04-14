package com.elytradev.bithop.tile;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

/**
 * This class exists solely to add @Nullable annotations to fields which forge falsely promises will be nonnull
 */
public abstract class KotlinTEWrapper extends TileEntity {
    @Override
    public boolean hasCapability(@Nullable Capability<?> capability, @Nullable EnumFacing facing) {
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nullable Capability<T> capability, @Nullable EnumFacing facing) {
        return super.getCapability(capability, facing);
    }
}
