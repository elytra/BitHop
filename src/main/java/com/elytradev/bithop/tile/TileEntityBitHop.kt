package com.elytradev.bithop.tile

import com.elytradev.bithop.block.BlockBitHop
import com.elytradev.bithop.block.ModBlocks
import com.elytradev.bithop.block.getFacing
import com.elytradev.bithop.util.handleTransfer
import net.minecraftforge.items.CapabilityItemHandler


class TileEntityBitHop: TileEntityBaseHop() {
    override val CAPACITY get() = 5
    override val unlocalizedName get() = BlockBitHop.unlocalizedName

    init {
        inv.listen(this::markDirty)
    }

    override fun activateHopper() {
        val tile = world.getTileEntity(getPos().offset(getFacing(blockMetadata))) ?: return
        val cap = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getFacing(blockMetadata).opposite) ?: return
        handleTransfer(inv, cap)
    }
}