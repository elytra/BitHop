package com.elytradev.bithop.tile

import com.elytradev.bithop.block.BlockBitHop
import com.elytradev.bithop.block.ModBlocks
import com.elytradev.bithop.util.canInsertExtract
import com.elytradev.bithop.util.getFirstEmptySlotCap
import com.elytradev.bithop.util.handleTransfer
import net.minecraft.util.EnumFacing
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandler


class TileEntityStickHop: TileEntityBaseHop() {
    override val CAPACITY get() = 5
    override val unlocalizedName get() = ModBlocks.BITHOP.unlocalizedName

    init {
        inv.listen(this::markDirty)
    }

    override fun activateHopper() {
        handlePush()
        handlePull()
    }

    fun getFirstTransferrableSlotSticky(to: IItemHandler): Int = (0 until inv.slots).firstOrNull{!inv.getStackInSlot(it).isEmpty && inv.getStackInSlot(it).count != 1 && canInsertExtract(inv, to, it)} ?: -1

    fun handlePush() {
        val tile = world.getTileEntity(getPos().offset(BlockBitHop.getFacing(blockMetadata))) ?: return
        val cap = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, BlockBitHop.getFacing(blockMetadata).opposite)!!
        val slotFull = getFirstTransferrableSlotSticky(cap)
        if (slotFull == -1) return
        val itemExtract = inv.extractItem(slotFull, 1, true)
        if (!itemExtract.isEmpty) {
            val insertSlot = getFirstEmptySlotCap(cap, itemExtract)
            if (insertSlot != -1) {
                val insert = cap.insertItem(insertSlot, itemExtract, false)
                if (insert.isEmpty) inv.extractItem(slotFull, 1, false)
            }
        }
    }
    private fun handlePull() {
        val tile = world.getTileEntity(getPos().offset(EnumFacing.UP)) ?: return
        val cap = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN) ?: return
        handleTransfer(cap, inv)
    }
}