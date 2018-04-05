package com.elytradev.bithop.container;

import com.elytradev.bithop.BitHop;
import com.elytradev.bithop.tile.TileEntityFluxHop;
import com.elytradev.concrete.inventory.gui.ConcreteContainer;
import com.elytradev.concrete.inventory.gui.widget.WBar;
import com.elytradev.concrete.inventory.gui.widget.WItemSlot;
import com.elytradev.concrete.inventory.gui.widget.WPanel;
import com.elytradev.concrete.inventory.gui.widget.WPlainPanel;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class FluxHopContainer extends ConcreteContainer {

    private ResourceLocation energyBG = new ResourceLocation(BitHop.modId,"textures/gui/energy_bg.png");
    private ResourceLocation energyFG = new ResourceLocation(BitHop.modId,"textures/gui/energy_fg.png");

    public FluxHopContainer(IInventory player, IInventory container, TileEntityFluxHop fluxHop) {
        super(player,container);
        WPlainPanel panel = new WPlainPanel();
        setRootPanel(panel);
        WItemSlot slotsBitHop = WItemSlot.of(container, 0, 3, 1);
        WPanel playerInv = this.createPlayerInventoryPanel();
        WBar energyBar = new WBar(energyBG, energyFG, container, 0, 1, WBar.Direction.UP).withTooltip("%d/%d RF");
        panel.add(slotsBitHop, 72, 23);
        panel.add(playerInv, 0, 54);
        panel.add(energyBar, 36, 12, 18, 36);

    }
}
