package com.elytradev.bithop.util;

import com.elytradev.bithop.BitHopLog;
import com.elytradev.bithop.block.ModBlocks;
import com.elytradev.bithop.item.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;

public class BitHopRecipes {

    @SubscribeEvent
    public static void onRegisterRecipes(RegistryEvent.Register<IRecipe> event) {

        IForgeRegistry<IRecipe> r = event.getRegistry();

        // Crafting bench recipes

        // recipe(r, new ShapedOreRecipe(new ResourceLocation("bithop:blocks"), new ItemStack(ModBlocks.VALVE, 1),
        //         " u ", "ibi", " u ",
        //         'b', new ItemStack(ModBlocks.BOILER),
        //         'i', new ItemStack(Items.IRON_INGOT),
        //         'u', new ItemStack(Items.BUCKET)
        // ));

    }

    public static <T extends IRecipe> T recipe(IForgeRegistry<IRecipe> registry, T t) {
        t.setRegistryName(new ResourceLocation(t.getRecipeOutput().getItem().getRegistryName()+"_"+t.getRecipeOutput().getItemDamage()));
        registry.register(t);
        return t;
    }

}
