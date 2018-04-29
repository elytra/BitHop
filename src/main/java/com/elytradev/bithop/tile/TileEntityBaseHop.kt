package com.elytradev.bithop.tile

import com.elytradev.concrete.inventory.ConcreteItemStorage
import com.elytradev.concrete.inventory.IContainerInventoryHolder
import com.elytradev.concrete.inventory.ValidatedInventoryView
import com.elytradev.concrete.inventory.ValidatedItemHandlerView
import net.minecraft.inventory.IInventory
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraft.util.ITickable
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.items.CapabilityItemHandler

const val INV_TAG = "Inventory"

abstract class TileEntityBaseHop : KotlinTEWrapper(), IContainerInventoryHolder, ITickable {
    val inv by lazy { ConcreteItemStorage(CAPACITY).withName("$unlocalizedName.name") }
    var cooldown = 0

    abstract val unlocalizedName: String
    abstract val CAPACITY: Int
    open val MAX_COOLDOWN = 8

    abstract fun activateHopper()
    open fun containerInventoryView(): ValidatedInventoryView = ValidatedInventoryView(inv)

    final override fun update() {
        if(!world.isRemote) {
            if (cooldown > 0) {
                cooldown--
                return
            }
            activateHopper()
            cooldown = MAX_COOLDOWN
        }
    }

    final override fun getContainerInventory(): IInventory = containerInventoryView()

    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
        val tag = super.writeToNBT(compound)
        tag.setTag(INV_TAG, inv.serializeNBT())
        return tag
    }

    override fun readFromNBT(compound: NBTTagCompound) {
        super.readFromNBT(compound)
        inv.deserializeNBT(compound.getCompoundTag(INV_TAG))
    }

    override fun hasCapability(capability: Capability<*>?, facing: EnumFacing?): Boolean {
        return when (capability) {
            null -> false
            CapabilityItemHandler.ITEM_HANDLER_CAPABILITY -> true
            else -> super.hasCapability(capability, facing)
        }
    }

    override fun <T : Any?> getCapability(capability: Capability<T>?, facing: EnumFacing?): T? {
        return when (capability) {
            null -> null
            CapabilityItemHandler.ITEM_HANDLER_CAPABILITY -> ValidatedItemHandlerView(inv) as? T
            else -> super.getCapability(capability, facing)
        }
    }
}