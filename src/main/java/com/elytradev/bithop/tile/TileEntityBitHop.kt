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
import net.minecraft.world.WorldServer
import net.minecraft.world.chunk.Chunk
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandler
import com.google.common.base.Predicates
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.network.play.server.SPacketUpdateTileEntity




class TileEntityBitHop : TileEntity(), IContainerInventoryHolder, ITickable {
    companion object {
        const val MAX_COOLDOWN = 8
        const val CAPACITY = 5
    }
    val inv = ConcreteItemStorage(CAPACITY).withName("${ModBlocks.BITHOP.unlocalizedName}.name")
    var cooldown = 8
    init {
        inv.listen{this.markDirty()}
    }

    override fun getContainerInventory() = ValidatedInventoryView(inv)

    private fun getFirstFullSlot(): Int = (0 until CAPACITY).firstOrNull{inv.getCanExtract(it)} ?: -1

    private fun getFirstEmptySlot(slotTotal: Int, cap: IItemHandler, test: ItemStack): Int = (0 until slotTotal).firstOrNull{cap.insertItem(it, test, true).isEmpty()} ?: -1

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
                    val insertSlot = getFirstEmptySlot(cap.slots, cap, extract)
                    if (insertSlot != -1) {
                        val insert = cap.insertItem(insertSlot, extract, false)
                        if (insert.isEmpty) inv.extractItem(slot, 1, false)
                    }
                }
            }
            cooldown = MAX_COOLDOWN
            this.markDirty()
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
    override fun markDirty() {
        super.markDirty()
        // again, I've copy-pasted this like 12 times, should probably go into Concrete
        if (!hasWorld() || getWorld().isRemote) return
        val ws = getWorld() as WorldServer
        val c = getWorld().getChunkFromBlockCoords(getPos())
        val packet = SPacketUpdateTileEntity(getPos(), 0, updateTag)
        for (player in getWorld().getPlayers(EntityPlayerMP::class.java, Predicates.alwaysTrue())) {
            if (ws.playerChunkMap.isPlayerWatchingChunk(player, c.x, c.z)) {
                player.connection.sendPacket(packet)
            }
        }
    }

}