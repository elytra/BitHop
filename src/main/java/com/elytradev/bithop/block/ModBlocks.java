package com.elytradev.bithop.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {

    public static final BlockBitHop BITHOP = new BlockBitHop(Material.IRON, "bithop");
    public static final BlockFluxHop FLUXHOP = new BlockFluxHop(Material.IRON, "fluxhop");
    public static final BlockScrewHop SCREWHOP = new BlockScrewHop(Material.IRON, "screwhop");

    public static IBlockBase[] allBlocks = {
             BITHOP, FLUXHOP, SCREWHOP
    };

    public static void register(IForgeRegistry<Block> registry) {
        for (int i = 0; i < allBlocks.length; i++) {
            IBlockBase block = allBlocks[i];
            registry.register(block.toBlock());
        }

        GameRegistry.registerTileEntity(BITHOP.getTileEntityClass(), BITHOP.getRegistryName().toString());
        GameRegistry.registerTileEntity(FLUXHOP.getTileEntityClass(), FLUXHOP.getRegistryName().toString());
        GameRegistry.registerTileEntity(SCREWHOP.getTileEntityClass(), SCREWHOP.getRegistryName().toString());

    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry) {
        for (int i = 0; i < allBlocks.length; i++) {
            IBlockBase block = allBlocks[i];
            registry.register(block.createItemBlock());
        }

    }

    public static void registerModels() {
        for (int i = 0; i < allBlocks.length; i++) {
            IBlockBase block = allBlocks[i];
            block.registerItemModel(Item.getItemFromBlock(block.toBlock()));
        }
    }
}
