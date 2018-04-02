package com.elytradev.bithop.tile

import com.elytradev.bithop.block.BlockBitHop
import com.elytradev.bithop.block.ModBlocks
import com.elytradev.concrete.inventory.ConcreteItemStorage
import com.elytradev.concrete.inventory.IContainerInventoryHolder
import com.elytradev.concrete.inventory.ValidatedInventoryView
import com.elytradev.concrete.inventory.ValidatedItemHandlerView
import net.minecraft.block.BlockChest
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntityChest
import net.minecraft.tileentity.TileEntityHopper
import net.minecraft.util.EntitySelectors
import net.minecraft.util.EnumFacing
import net.minecraft.util.ITickable
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.items.CapabilityItemHandler

val CAPACITY = 5

class TileEntityBitHop : TileEntity(), IContainerInventoryHolder, ITickable {
    val inv = ConcreteItemStorage(CAPACITY).withName("${ModBlocks.BITHOP.unlocalizedName}.name")
    var direction: EnumFacing? = null
    var cooldown = 8;
    var maxCooldown = 8;
    init {
        inv.listen{markDirty()}
    }

    override fun getContainerInventory() = ValidatedInventoryView(inv)


    fun pushItems() {
        val slot = getFirstFullSlot()
        if (slot != -1) {

        }
    }

    private fun getFirstFullSlot(): Int = (0 until CAPACITY).firstOrNull{inv.getCanExtract(it)} ?: -1

    private fun getInventoryForHopperTransfer() {
        getInventoryAtPosition(getWorld(), pos.add(BlockBitHop.getFacing(blockMetadata).directionVec), false)
    }

    override fun update() {
        if(!world.isRemote) {
            if (cooldown > 0) {
                cooldown--
                this.markDirty()
                return
            }
            val slot = getFirstFullSlot()
            if (slot != -1) {
                var tile = world.getTileEntity(getPos().offset(direction))
                if (tile == null || !tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, direction?.getOpposite())) return
                var cap = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, direction?.opposite)
                var extract = inv.extractItem(slot, 1, true)
                if (!extract.isEmpty) {
                    var insert = cap?.insertItem(1, extract, false)
                    if (!insert!!.isEmpty) inv.extractItem(slot, 1, false)
                }
            }
            cooldown = maxCooldown
        }
    }

    override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true
        return super.hasCapability(capability, facing)
    }

    override fun <T : Any?> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return ValidatedItemHandlerView(inv) as T
        }
        return super.getCapability(capability, facing)
    }
}

fun getInventoryAtPosition(worldIn: World, pos: BlockPos, scanEntities: Boolean = false): IInventory? {
    val state = worldIn.getBlockState(pos)
    val block = state.block
    if (block.hasTileEntity(state)) {
        val te = worldIn.getTileEntity(pos)
        if (te is IInventory) {
            return if (te is TileEntityChest && block is BlockChest)
                block.getContainer(worldIn, pos, true) ?: te
            else
                te
        }
    }

    return if (scanEntities) {
        val inRange = worldIn.getEntitiesInAABBexcluding(null,
                AxisAlignedBB(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(),
                        pos.x + 1.0, pos.y + 1.0, pos.z + 1.0),
                EntitySelectors.HAS_INVENTORY)

        if (inRange.isNotEmpty())
            inRange[worldIn.rand.nextInt(inRange.size)] as IInventory
        else
            null
    } else null
}