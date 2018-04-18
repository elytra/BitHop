package com.elytradev.bithop.container;

import com.elytradev.bithop.tile.TileEntityPullHop;
import com.elytradev.concrete.inventory.gui.ConcreteContainer;
import com.elytradev.concrete.inventory.gui.widget.WItemSlot;
import com.elytradev.concrete.inventory.gui.widget.WPanel;
import com.elytradev.concrete.inventory.gui.widget.WPlainPanel;
import net.minecraft.inventory.IInventory;

public class PullHopContainer extends ConcreteContainer {

    public PullHopContainer(IInventory player, IInventory container, TileEntityPullHop pullHop) {
        super(player,container);
        WPlainPanel panel = new WPlainPanel();
        setRootPanel(panel);
        WItemSlot slotsBitHop = WItemSlot.of(container, 0, 5, 1);
        WPanel playerInv = this.createPlayerInventoryPanel();
        panel.add(slotsBitHop, 36, 23);
        panel.add(playerInv, 0, 54);
    }
}
