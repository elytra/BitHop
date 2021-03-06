package com.elytradev.bithop.util;

import com.elytradev.bithop.BitHop;
import com.elytradev.concrete.config.ConcreteConfig;
import com.elytradev.concrete.config.ConfigValue;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class BitHopConfig extends ConcreteConfig {

    public File configFolder;

    @ConfigValue(type = Property.Type.INTEGER, category = "FluxHop", comment = "The RF/t for a FluxHop to transfer. Will transfer 8x this amount every 8 ticks.")
    public static int fluxHopTransfer = 200;

    private BitHopConfig(File configFile) {
        super(configFile, BitHop.modId);
        this.configFolder = configFile.getParentFile();
    }

    public static BitHopConfig createConfig(FMLPreInitializationEvent event) {
        //Move config file if it exists.
        File bitHopFolder = new File(event.getModConfigurationDirectory(), "bithop");
        bitHopFolder.mkdirs();
        if (event.getSuggestedConfigurationFile().exists()) {
            event.getSuggestedConfigurationFile().renameTo(new File(bitHopFolder, "bithop.cfg"));
        }

        BitHopConfig config = new BitHopConfig(new File(bitHopFolder, "bithop.cfg"));
        config.loadConfig();
        return config;
    }
}
