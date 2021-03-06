package com.elytradev.bithop.tile

import com.elytradev.bithop.block.BlockFluxHop
import com.elytradev.bithop.block.ModBlocks
import com.elytradev.bithop.block.getFacing
import com.elytradev.bithop.util.handleTransfer
import net.minecraft.util.EnumFacing
import net.minecraftforge.items.CapabilityItemHandler

class TileEntityPullHop: TileEntityBaseHop() {
    override val CAPACITY get() = 5
    override val unlocalizedName get() = ModBlocks.PULLHOP.unlocalizedName

    override fun activateHopper() {
        handlePush()
        handlePull()
    }

    private fun handlePush() {
        val tile = world.getTileEntity(getPos().offset(getFacing(blockMetadata))) ?: return
        val cap = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getFacing(blockMetadata).opposite) ?: return
        handleTransfer(inv, cap)
    }

    private fun handlePull() {
        val tile = world.getTileEntity(getPos().offset(EnumFacing.UP)) ?: return
        val cap = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN) ?: return
        handleTransfer(cap, inv)
    }
}