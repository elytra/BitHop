package com.elytradev.bithop.util

import net.minecraft.item.ItemStack
import net.minecraftforge.items.IItemHandler

fun getFirstTransferrableSlotCap(from: IItemHandler, to: IItemHandler): Int = (0 until from.slots).firstOrNull{!from.getStackInSlot(it).isEmpty && canInsertExtract(from, to, it)} ?: -1
fun getFirstEmptySlotCap(cap: IItemHandler, test: ItemStack): Int = (0 until cap.slots).firstOrNull{cap.insertItem(it, test, true).isEmpty} ?: -1

fun canInsertExtract(from: IItemHandler, to: IItemHandler, slot: Int): Boolean {
    val itemExtract = from.extractItem(slot, 1, true)
    if (!itemExtract.isEmpty) {
        val insertSlot = getFirstEmptySlotCap(to, itemExtract)
        if (insertSlot != -1) {
            val insert = to.insertItem(insertSlot, itemExtract, true)
            if (insert.isEmpty) return true
        }
    }
    return false
}

fun handleTransfer(from: IItemHandler, to: IItemHandler) {

    val slotFull = getFirstTransferrableSlotCap(from, to)
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