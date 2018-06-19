package com.elytradev.bithop.block

import com.elytradev.bithop.tile.TileEntityBitHop
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.world.World

object BlockBitHop: BlockHopTE<TileEntityBitHop>(Material.IRON, "bithop") {
    override fun getTileEntityClass() = TileEntityBitHop::class.java

    override fun createTileEntity(world: World, state: IBlockState) = TileEntityBitHop()

    override val guiId = 0
    override val facingFilter = noUpFacingFilter
}