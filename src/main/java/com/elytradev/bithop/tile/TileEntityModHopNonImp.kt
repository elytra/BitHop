package com.elytradev.bithop.tile

import com.elytradev.bithop.block.BlockBitHop
import com.elytradev.bithop.block.BlockModHopNonImp
import com.elytradev.bithop.block.ModBlocks
import com.elytradev.bithop.block.getFacing
import com.elytradev.bithop.util.*
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraftforge.energy.CapabilityEnergy
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandler


class TileEntityModHopNonImp: TileEntityBaseHop() {
    /*
    NOTE: do not implement, just sketching out the bithop itself for v2.0
     */

    override val CAPACITY get() = 5
    override val unlocalizedName get() = BlockBitHop.unlocalizedName

    val pushFacing: EnumFacing = getFacing(blockMetadata)
    var pullFacing: EnumFacing = getFacing(blockMetadata)
    var hasPull: Boolean = false
    var hasFlux: Boolean = false
    var hasStick: Boolean = false

    val ENERGY_STORAGE = 0
    val energy = ObservableEnergyStorage(ENERGY_STORAGE, 8* BitHopConfig.fluxHopTransfer)

    init {
        inv.listen(this::markDirty)
        energy.listen(this::markDirty)
    }

    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
        val tag = super.writeToNBT(compound)
        CapabilityEnergy.ENERGY.storage.writeNBT(CapabilityEnergy.ENERGY, energy, null) ?.let {
            tag.setTag("Energy", it)
        }
        tag.setBoolean("HasPull", hasPull)
        tag.setBoolean("HasFlux", hasFlux)
        tag.setBoolean("HasStick", hasStick)
        return tag
    }

    override fun readFromNBT(compound: NBTTagCompound) {
        super.readFromNBT(compound)
        compound.getTag("Energy") ?.let {
            try {
                CapabilityEnergy.ENERGY.storage.readNBT(CapabilityEnergy.ENERGY, energy, null, it)
            } catch (t: Throwable) { }
        }
        hasPull = compound.getBoolean("HasPull")
        hasFlux = compound.getBoolean("HasFlux")
        hasStick = compound.getBoolean("HasStick")
    }

    override fun activateHopper() {
        if (hasStick) handlePushSticky() else handlePush()
        if (hasPull) handlePull()
        if (hasFlux) handleEnergy()
    }

    private fun handlePush() {
        val tile = world.getTileEntity(getPos().offset(pushFacing)) ?: return
        val cap = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, pushFacing.opposite) ?: return
        handleTransfer(inv, cap)
    }

    private fun handlePull() {
        val tilePull = world.getTileEntity(getPos().offset(pullFacing)) ?: return
        val capPull = tilePull.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, pullFacing.opposite) ?: return
        handleTransfer(capPull, inv)
    }

    private fun handleEnergy() {
        val tile = world.getTileEntity(getPos().offset(pushFacing)) ?: return
        val capEnergy = tile.getCapability(CapabilityEnergy.ENERGY, pushFacing.opposite) ?: return
        val energyExtract = energy.extractEnergy(8*BitHopConfig.fluxHopTransfer, true)
        if (energyExtract != 0) {
            val qty = capEnergy.receiveEnergy(energyExtract, false)
            if (qty > 0) energy.extractEnergy(qty, false)
        }
    }

    private fun getFirstTransferrableSlotSticky(to: IItemHandler): Int = (0 until inv.slots).firstOrNull{!inv.getStackInSlot(it).isEmpty && inv.getStackInSlot(it).count != 1 && canInsertExtract(inv, to, it) } ?: -1

    private fun handlePushSticky() {
        val tile = world.getTileEntity(getPos().offset(pushFacing)) ?: return
        val cap = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, pushFacing.opposite) ?: return
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
}