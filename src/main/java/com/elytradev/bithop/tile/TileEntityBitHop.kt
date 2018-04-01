package com.elytradev.bithop.tile

import com.elytradev.bithop.block.ModBlocks
import com.elytradev.concrete.inventory.ConcreteItemStorage
import com.elytradev.concrete.inventory.IContainerInventoryHolder
import com.elytradev.concrete.inventory.ValidatedInventoryView
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing

val CAPACITY = 5

class TileEntityBitHop : TileEntity(), IContainerInventoryHolder {
    val inv = ConcreteItemStorage(CAPACITY).withName("${ModBlocks.BITHOP.unlocalizedName}.name")
    var direction: EnumFacing? = null
    init {
        inv.listen{markDirty()}
        inv.listen{pushItems()}
    }

    override fun getContainerInventory() = ValidatedInventoryView(inv)


    fun pushItems() {
        val slot = getFirstFullSlot()
        if (slot != -1) {

        }
    }

    private fun getFirstFullSlot(): Int = (0 until CAPACITY).firstOrNull{inv.getCanExtract(it)} ?: -1
}