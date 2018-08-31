package com.elytradev.bithop.block

import com.elytradev.bithop.BitHop
import com.google.common.base.Predicate
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyDirection
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World


abstract class BlockHopTE<TE: TileEntity>(material: Material, name: String):
        BlockTileEntity<TE>(material, name) {
    abstract val guiId: Int
    abstract fun facingFilter(): Predicate<EnumFacing>
    lateinit var FACING: PropertyDirection

    init {
        this.defaultState = blockState.baseState
        setCreativeTab(BitHop.creativeTab)
    }

    final override fun onBlockActivated(world: World, pos: BlockPos?, state: IBlockState?, player: EntityPlayer?, hand: EnumHand?, side: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        if (!world.isRemote && !player!!.isSneaking) {
            player.openGui(BitHop.instance, guiId, world, pos!!.x, pos.y, pos.z)
        }
        return true
    }

    final override fun createBlockState(): BlockStateContainer {
        FACING = PropertyDirection.create("facing", facingFilter())
        return BlockStateContainer(this, FACING)
    }

    final override fun isOpaqueCube(state: IBlockState?) = false

    final override fun isFullCube(state: IBlockState?) = false

    final override fun getMetaFromState(state: IBlockState): Int =
        (state.getValue(FACING) as EnumFacing).index or 8

    final override fun getStateFromMeta(meta: Int): IBlockState {
        return defaultState.withProperty(FACING, getFacing(meta))
    }

    override fun getStateForPlacement(worldIn: World?, pos: BlockPos?, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float, meta: Int, placer: EntityLivingBase?): IBlockState {
        val enumfacing = facing.opposite
        val filtered = if (enumfacing == EnumFacing.UP) {EnumFacing.DOWN} else enumfacing
        return this.defaultState.withProperty(FACING, filtered)
    }
}

fun getFacing(meta: Int): EnumFacing {
    return EnumFacing.getFront(meta and 7)
}