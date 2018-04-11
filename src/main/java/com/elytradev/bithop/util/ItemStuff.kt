package com.elytradev.bithop.util

import net.minecraft.item.ItemStack
import net.minecraftforge.items.IItemHandler

fun getFirstFullSlotCap(cap: IItemHandler): Int = (0 until cap.slots).firstOrNull{!cap.getStackInSlot(it).isEmpty} ?: -1
fun getFirstEmptySlotCap(cap: IItemHandler, test: ItemStack): Int = (0 until cap.slots).firstOrNull{cap.insertItem(it, test, true).isEmpty} ?: -1

fun handleTransfer(from: IItemHandler, to: IItemHandler) {
    val slotFull = getFirstFullSlotCap(from)
    if (slotFull == -1) return
    val itemExtract = from.extractItem(slotFull, 1, true)
    if (!itemExtract.isEmpty) {
        val insertSlot = getFirstEmptySlotCap(to, itemExtract)
        if (insertSlot != -1) {
            val insert = to.insertItem(insertSlot, itemExtract, false)
            if (insert.isEmpty) from.extractItem(slotFull, 1, false)
        }
    }
}