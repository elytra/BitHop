package com.elytradev.bithop.block

import com.google.common.base.Predicate
import net.minecraft.util.EnumFacing

val noUpFacingFilter: Predicate<EnumFacing> = Predicate {it != EnumFacing.UP}
val screwHopFacingFilter: Predicate<EnumFacing> = Predicate {it != EnumFacing.UP && it != EnumFacing.DOWN}
