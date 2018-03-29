package com.elytradev.bithop.proxy;

//import com.elytradev.bithop.client.RenderDistiller;
//import com.elytradev.opaline.tile.TileEntityDistiller;
import com.elytradev.bithop.BitHop;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {
        //ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDistiller.class, new RenderDistiller());
    }

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(new ResourceLocation(BitHop.modId, id), "inventory"));
    }
}
