package com.elytradev.bithop.tile

import com.elytradev.bithop.block.BlockFluxHop
import com.elytradev.bithop.block.ModBlocks
import com.elytradev.bithop.util.BitHopConfig
import com.elytradev.bithop.util.ObservableEnergyStorage
import com.elytradev.bithop.util.handleTransfer
import com.elytradev.concrete.inventory.ValidatedInventoryView
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.energy.CapabilityEnergy

const val ENERGY_STORAGE = 10_000

class TileEntityFluxHop: TileEntityBaseHop() {
    override val CAPACITY get() = 3
    override val unlocalizedName get() = ModBlocks.FLUXHOP.unlocalizedName

    val energy = ObservableEnergyStorage(ENERGY_STORAGE, 8*BitHopConfig.fluxHopTransfer)
    init {
        inv.listen(this::markDirty)
        energy.listen(this::markDirty)
    }

    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
        val tag = super.writeToNBT(compound)
        CapabilityEnergy.ENERGY.storage.writeNBT(CapabilityEnergy.ENERGY, energy, null) ?.let {
            tag.setTag("Energy", it)
        }
        return tag
    }

    override fun readFromNBT(compound: NBTTagCompound) {
        super.readFromNBT(compound)
        compound.getTag("Energy") ?.let {
            try {
                CapabilityEnergy.ENERGY.storage.readNBT(CapabilityEnergy.ENERGY, energy, null, it)
            } catch (t: Throwable) { }
        }
    }

    override fun containerInventoryView(): ValidatedInventoryView {
        val view = super.containerInventoryView()
        return if (world.isRemote) {
            view
        } else {
            view.withField(0) { energy.energyStored }
                    .withField(1) { energy.maxEnergyStored }
        }
    }

    override fun activateHopper() {
        handlePush()
        handlePull()
        handleEnergy()
    }

    private fun handlePush() {
        val tile = world.getTileEntity(getPos().offset(BlockFluxHop.getFacing(blockMetadata))) ?: return
        val capItem = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, BlockFluxHop.getFacing(blockMetadata).opposite) ?: return
        handleTransfer(inv, capItem)
    }

    private fun handlePull() {
        val tile = world.getTileEntity(getPos().offset(EnumFacing.UP)) ?: return
        val capItem = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN) ?: return
        handleTransfer(capItem, inv)
    }

    private fun handleEnergy() {
        val tile = world.getTileEntity(getPos().offset(BlockFluxHop.getFacing(blockMetadata))) ?: return
        val capEnergy = tile.getCapability(CapabilityEnergy.ENERGY, BlockFluxHop.getFacing(blockMetadata).opposite) ?: return
        val energyExtract = energy.extractEnergy(8*BitHopConfig.fluxHopTransfer, true)
        if (energyExtract != 0) {
            val qty = capEnergy.receiveEnergy(energyExtract, false)
            if (qty > 0) energy.extractEnergy(qty, false)
        }
    }

    override fun hasCapability(capability: Capability<*>?, facing: EnumFacing?): Boolean {
        return when (capability) {
            CapabilityEnergy.ENERGY -> true
            else -> super.hasCapability(capability, facing)
        }
    }

    override fun <T : Any?> getCapability(capability: Capability<T>?, facing: EnumFacing?): T? {
        return when (capability) {
            CapabilityEnergy.ENERGY -> CapabilityEnergy.ENERGY.cast(energy) as T
            else -> super.getCapability(capability, facing)
        }
    }
}