package com.elytradev.bithop.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {

    public static final BlockFluxHop FLUXHOP = new BlockFluxHop(Material.IRON, "fluxhop");
    public static final BlockScrewHop SCREWHOP = new BlockScrewHop(Material.IRON, "screwhop");
    public static final BlockPullHop PULLHOP = new BlockPullHop(Material.IRON, "pullhop");
    public static final BlockStickHop STICKHOP = new BlockStickHop(Material.IRON, "stickhop");

    public static BlockTileEntity[] allBlocks = {
            BlockBitHop.INSTANCE, FLUXHOP, SCREWHOP, PULLHOP, STICKHOP
    };

    public static void register(IForgeRegistry<Block> registry) {
        for (BlockTileEntity block : allBlocks) {
            registry.register(block.toBlock());
            GameRegistry.registerTileEntity(block.getTileEntityClass(), block.getRegistryName().toString());
        }
    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry) {
        for (IBlockBase block : allBlocks) {
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
