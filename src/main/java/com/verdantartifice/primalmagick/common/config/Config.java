package com.verdantartifice.primalmagick.common.config;

import java.nio.file.Path;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

/**
 * Definition of common and client configuration files for the mod.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber
public class Config {
    protected static final String CATEGORY_WORLDGEN = "worldgen";
    protected static final String CATEGORY_MISC = "misc";
    
    protected static ForgeConfigSpec COMMON_CONFIG_SPEC;
    protected static ForgeConfigSpec CLIENT_CONFIG_SPEC;
    protected static boolean IS_REGISTERED = false;
    
    public static ForgeConfigSpec.BooleanValue SHOW_AFFINITIES;
    public static ForgeConfigSpec.BooleanValue SHOW_UNSCANNED_AFFINITIES;
    public static ForgeConfigSpec.IntValue SHRINE_AVERAGE_DISTANCE_CHUNKS;
    public static ForgeConfigSpec.IntValue SHRINE_MINIMUM_DISTANCE_CHUNKS;
    
    static {
        buildCommonConfigSpec();
        buildClientConfigSpec();
    }
    
    protected static void buildCommonConfigSpec() {
        // Define the common config file spec
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        
        builder.comment("Worldgen settings").push(CATEGORY_WORLDGEN);
        SHRINE_AVERAGE_DISTANCE_CHUNKS = builder.comment("Average distance apart in chunks between shrine spawn attempts").defineInRange("shrineAverageDistanceChunks", 20, 1, 1000000);
        SHRINE_MINIMUM_DISTANCE_CHUNKS = builder.comment("Minimum distance apart in chunks between shrine spawn attempts; must be less than average").defineInRange("shrineMinimumDistanceChunks", 10, 1, 1000000);
        builder.pop();
        
        builder.comment("Misc settings").push(CATEGORY_MISC);
        SHOW_UNSCANNED_AFFINITIES = builder.comment("Show affinities of blocks and items even without scanning them").define("showUnscannedAffinities", false);
        builder.pop();
        
        COMMON_CONFIG_SPEC = builder.build();
    }
    
    protected static void buildClientConfigSpec() {
        // Define the client-only config file spec
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        
        builder.comment("Misc settings").push(CATEGORY_MISC);
        SHOW_AFFINITIES = builder.comment("Item affinities are hidden by default and pressing shift reveals them.", "Setting this to 'true' will reverse this behavior.").define("showAffinities", false);
        builder.pop();
        
        CLIENT_CONFIG_SPEC = builder.build();
    }
    
    protected static void loadConfig(ForgeConfigSpec spec, Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();
        configData.load();
        spec.setConfig(configData);
    }
    
    public static void register() {
        if (IS_REGISTERED) {
            // Only allow registration once
            throw new IllegalStateException("Primal Magick config spec is already registered!");
        }
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG_SPEC);
        loadConfig(Config.COMMON_CONFIG_SPEC, FMLPaths.CONFIGDIR.get().resolve("primalmagick-common.toml"));
        loadConfig(Config.CLIENT_CONFIG_SPEC, FMLPaths.CONFIGDIR.get().resolve("primalmagick-client.toml"));
        IS_REGISTERED = true;
    }
}
