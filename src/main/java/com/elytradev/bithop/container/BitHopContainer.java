package com.elytradev.bithop.container;

import com.elytradev.bithop.tile.TileEntityBitHop;
import com.elytradev.concrete.inventory.gui.ConcreteContainer;
import com.elytradev.concrete.inventory.gui.widget.WItemSlot;
import com.elytradev.concrete.inventory.gui.widget.WPanel;
import com.elytradev.concrete.inventory.gui.widget.WPlainPanel;
import net.minecraft.inventory.IInventory;

public class BitHopContainer extends ConcreteContainer {

    public BitHopContainer(IInventory player, IInventory container, TileEntityBitHop bitHop) {
        super(player,container);
        WPlainPanel panel = new WPlainPanel();
        setRootPanel(panel);
        WItemSlot slotsBitHop = WItemSlot.of(container, 0, 5, 1);
        WPanel playerInv = this.createPlayerInventoryPanel();
        panel.add(slotsBitHop, 36, 23);
        panel.add(playerInv, 0, 54);
    }
}
