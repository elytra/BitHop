package com.elytradev.bithop.tile

import com.elytradev.bithop.block.BlockFluxHop
import com.elytradev.bithop.block.ModBlocks
import com.elytradev.bithop.util.BitHopConfig
import com.elytradev.bithop.util.ObservableEnergyStorage
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
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.inventory.IInventory
import net.minecraftforge.energy.CapabilityEnergy

class TileEntityFluxHop : TileEntity(), IContainerInventoryHolder, ITickable {
    companion object {
        const val MAX_COOLDOWN = 8
        const val CAPACITY = 3
        const val ENERGY_STORAGE = 10_000
        const val INV_TAG = "Inventory"
    }
    val inv = ConcreteItemStorage(CAPACITY).withName("${ModBlocks.FLUXHOP.unlocalizedName}.name")
    val energy = ObservableEnergyStorage(ENERGY_STORAGE, BitHopConfig.fluxHopTransfer, 8*BitHopConfig.fluxHopTransfer)
    var cooldown = 8
    init {
        inv.listen(this::markDirty)
    }

    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
        val tag = super.writeToNBT(compound)
        tag.setTag(INV_TAG, inv.serializeNBT())
        val energyTag = CapabilityEnergy.ENERGY.storage.writeNBT(CapabilityEnergy.ENERGY, energy, null)
        tag.setTag("Energy", energyTag);
        return tag
    }

    override fun readFromNBT(compound: NBTTagCompound) {
        super.readFromNBT(compound)
        inv.deserializeNBT(compound.getCompoundTag(INV_TAG))
        val energyTag = compound.getTag("Energy")
        if (energyTag != null) {
            try {
                CapabilityEnergy.ENERGY.storage.readNBT(CapabilityEnergy.ENERGY, energy, null, energyTag)
            } catch (t: Throwable) {
            }

        }
    }

    override fun getContainerInventory(): IInventory { return ValidatedInventoryView(inv) }

    private fun getFirstFullSlot(): Int = (0 until CAPACITY).firstOrNull{inv.getCanExtract(it)} ?: -1

    private fun getFirstEmptySlot(slotTotal: Int, cap: IItemHandler, test: ItemStack): Int = (0 until slotTotal).firstOrNull{cap.insertItem(it, test, true).isEmpty()} ?: -1

    override fun update() {
        if(!world.isRemote) {
            if (cooldown > 0) {
                cooldown--
                return
            }
            val slot = getFirstFullSlot()
            if (slot != -1) {
                val tile = world.getTileEntity(getPos().offset(BlockFluxHop.getFacing(blockMetadata))) ?: return
                val capItem = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, BlockFluxHop.getFacing(blockMetadata).opposite)!!
                val capEnergy = tile.getCapability(CapabilityEnergy.ENERGY, BlockFluxHop.getFacing(blockMetadata).opposite)!!
                val itemExtract = inv.extractItem(slot, 1, true)
                if (!itemExtract.isEmpty) {
                    val insertSlot = getFirstEmptySlot(capItem.slots, capItem, itemExtract)
                    if (insertSlot != -1) {
                        val insert = capItem.insertItem(insertSlot, itemExtract, false)
                        if (insert.isEmpty) inv.extractItem(slot, 1, false)
                    }
                }
                val energyTransfer = energy.extractEnergy(8*BitHopConfig.fluxHopTransfer, true)
                if (energyTransfer != 0) {
                    val qty = capEnergy.receiveEnergy(8*BitHopConfig.fluxHopTransfer, false)
                    if (qty > 0) energy.extractEnergy(qty, false)
                }
            }
            cooldown = MAX_COOLDOWN
        }
    }

    override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
        return when (capability) {
            CapabilityItemHandler.ITEM_HANDLER_CAPABILITY -> true
            CapabilityEnergy.ENERGY -> true
            else -> super.hasCapability(capability, facing)
        }
    }

    override fun <T : Any?> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
        return when (capability) {
            CapabilityItemHandler.ITEM_HANDLER_CAPABILITY -> ValidatedItemHandlerView(inv) as T
            CapabilityEnergy.ENERGY -> CapabilityEnergy.ENERGY.cast(energy) as T
            else -> super.getCapability(capability, facing)
        }
    }
}