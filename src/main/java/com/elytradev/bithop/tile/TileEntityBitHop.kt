package com.elytradev.bithop.tile

import com.elytradev.bithop.block.BlockBitHop
import com.elytradev.bithop.block.ModBlocks
import com.elytradev.concrete.inventory.ConcreteItemStorage
import com.elytradev.concrete.inventory.IContainerInventoryHolder
import com.elytradev.concrete.inventory.ValidatedInventoryView
import com.elytradev.concrete.inventory.ValidatedItemHandlerView
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.ITickable
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandler

val CAPACITY = 5

class TileEntityBitHop : TileEntity(), IContainerInventoryHolder, ITickable {
    val inv = ConcreteItemStorage(CAPACITY).withName("${ModBlocks.BITHOP.unlocalizedName}.name")
    var cooldown = 8
    init {
        inv.listen{markDirty()}
    }

    override fun getContainerInventory() = ValidatedInventoryView(inv)

    private fun getFirstFullSlot(): Int = (0 until CAPACITY).firstOrNull{inv.getCanExtract(it)} ?: -1

    private fun getFirstEmptylot(slotTotal: Int, cap: IItemHandler, test: ItemStack): Int = (0 until slotTotal).firstOrNull{cap.insertItem(it, test, true).isEmpty()} ?: -1

    override fun update() {
        if(!world.isRemote) {
            if (cooldown > 0) {
                cooldown--
                this.markDirty()
                return
            }
            val slot = getFirstFullSlot()
            if (slot != -1) {
                val tile = world.getTileEntity(getPos().offset(BlockBitHop.getFacing(blockMetadata))) ?: return
                val cap = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, BlockBitHop.getFacing(blockMetadata).getOpposite())!!
                val extract = inv.extractItem(slot, 1, true)
                if (!extract.isEmpty) {
                    val insertSlot = getFirstEmptylot(cap.slots, cap, extract)
                    if (insertSlot != -1) {
                        val insert = cap.insertItem(insertSlot, extract, false)
                        if (insert.isEmpty) inv.extractItem(slot, 1, false)
                    }
                }
            }
            cooldown = 8
        }
    }

    override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
        return when (capability) {
            CapabilityItemHandler.ITEM_HANDLER_CAPABILITY -> true
            else -> super.hasCapability(capability, facing)
        }
    }

    override fun <T : Any?> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
        return when (capability) {
            CapabilityItemHandler.ITEM_HANDLER_CAPABILITY -> ValidatedItemHandlerView(inv) as T
            else -> super.getCapability(capability, facing)
        }
    }
}