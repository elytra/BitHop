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
    public static final BlockPullHop PULLHOP = new BlockPullHop(Material.IRON, "pullhop");
    public static final BlockStickHop STICKHOP = new BlockStickHop(Material.IRON, "stickhop");

    public static IBlockBase[] allBlocks = {
             BITHOP, FLUXHOP, SCREWHOP, PULLHOP, STICKHOP
    };

    public static void register(IForgeRegistry<Block> registry) {
        for (int i = 0; i < allBlocks.length; i++) {
            IBlockBase block = allBlocks[i];
            registry.register(block.toBlock());
        }

        GameRegistry.registerTileEntity(BITHOP.getTileEntityClass(), BITHOP.getRegistryName().toString());
        GameRegistry.registerTileEntity(FLUXHOP.getTileEntityClass(), FLUXHOP.getRegistryName().toString());
        GameRegistry.registerTileEntity(SCREWHOP.getTileEntityClass(), SCREWHOP.getRegistryName().toString());
        GameRegistry.registerTileEntity(PULLHOP.getTileEntityClass(), PULLHOP.getRegistryName().toString());
        GameRegistry.registerTileEntity(STICKHOP.getTileEntityClass(), STICKHOP.getRegistryName().toString());

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
