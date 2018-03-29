package com.elytradev.bithop;

import com.elytradev.bithop.util.BitHopConfig;
import com.elytradev.concrete.inventory.IContainerInventoryHolder;
import com.elytradev.concrete.inventory.gui.client.ConcreteGui;
import com.elytradev.bithop.block.ModBlocks;
import com.elytradev.bithop.client.BitHopTab;
import com.elytradev.bithop.item.ModItems;
import com.elytradev.bithop.proxy.CommonProxy;
import com.elytradev.bithop.util.BitHopRecipes;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;


@Mod(modid = BitHop.modId, name = BitHop.name, version = BitHop.version)
public class BitHop {
    public static final String modId = "bithop";
    public static final String name  = "BitHop";
    public static final String version = "@VERSION@";
    public static BitHopConfig config;
    public static boolean hasBrass;

    @Mod.Instance(modId)
    public static BitHop instance;

    public static final BitHopTab creativeTab = new BitHopTab();

    @SidedProxy(serverSide = "com.elytradev.bithop.proxy.CommonProxy", clientSide = "com.elytradev.bithop.proxy.ClientProxy")
    public static CommonProxy proxy;


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        BitHopLog.info(name + " is loading!");
        MinecraftForge.EVENT_BUS.register(BitHopRecipes.class);
        config = BitHopConfig.createConfig(event);

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new IGuiHandler() {
            // public static final int BOILER = 0;

            @Nullable
            @Override
            public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
                switch (ID) {
                    // case BOILER:
                        // return new BoilerContainer(
                        //         player.inventory, ((IContainerInventoryHolder)world.getTileEntity(new BlockPos(x,y,z))).getContainerInventory(),
                        //         (TileEntityBoilerController)world.getTileEntity(new BlockPos(x,y,z)));
                    default:
                        return null;
                }

            }

            @Nullable
            @Override
            @SideOnly(Side.CLIENT)
            public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
                switch (ID) {
                    // case BOILER:
                    //     BoilerContainer boilerContainer = new BoilerContainer(
                    //             player.inventory, ((IContainerInventoryHolder)world.getTileEntity(new BlockPos(x,y,z))).getContainerInventory(),
                    //             (TileEntityBoilerController)world.getTileEntity(new BlockPos(x,y,z)));
                    //     return new ConcreteGui(boilerContainer);
                    default:
                        return null;
                }

            }
        });
        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        //MinecraftForge.EVENT_BUS.register(new SoundRegisterListener());
        //MinecraftForge.EVENT_BUS.register(LightHandler.class);
        ModItems.registerOreDict(); // register oredict
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }

    @Mod.EventBusSubscriber
    public static class RegistrationHandler {
        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            ModItems.register(event.getRegistry());
            ModBlocks.registerItemBlocks(event.getRegistry());
        }

        @SubscribeEvent
        public static void registerModels(ModelRegistryEvent event) {
            ModItems.registerModels();
            ModBlocks.registerModels();
        }

        @SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event) {
            ModBlocks.register(event.getRegistry());
        }
    }
}
