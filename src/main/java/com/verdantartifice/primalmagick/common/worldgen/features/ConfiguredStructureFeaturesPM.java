package com.verdantartifice.primalmagick.common.worldgen.features;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;

/**
 * Definition of configures mod structures.
 * 
 * @author Daedalus4096
 */
public class ConfiguredStructureFeaturesPM {
    public static final ConfiguredStructureFeature<?, ?> CONFIGURED_EARTH_SHRINE = StructureFeaturesPM.SHRINE.get().configured(new ShrineConfig(ShrineStructure.Type.EARTH));
    public static final ConfiguredStructureFeature<?, ?> CONFIGURED_SEA_SHRINE = StructureFeaturesPM.SHRINE.get().configured(new ShrineConfig(ShrineStructure.Type.SEA));
    public static final ConfiguredStructureFeature<?, ?> CONFIGURED_SKY_SHRINE = StructureFeaturesPM.SHRINE.get().configured(new ShrineConfig(ShrineStructure.Type.SKY));
    public static final ConfiguredStructureFeature<?, ?> CONFIGURED_SUN_SHRINE = StructureFeaturesPM.SHRINE.get().configured(new ShrineConfig(ShrineStructure.Type.SUN));
    public static final ConfiguredStructureFeature<?, ?> CONFIGURED_MOON_SHRINE = StructureFeaturesPM.SHRINE.get().configured(new ShrineConfig(ShrineStructure.Type.MOON));
    
    public static void registerConfiguredStructures() {
        Registry<ConfiguredStructureFeature<?, ?>> registry = BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE;
        registerConfiguredStructure(registry, "configured_earth_shrine", CONFIGURED_EARTH_SHRINE);
        registerConfiguredStructure(registry, "configured_sea_shrine", CONFIGURED_SEA_SHRINE);
        registerConfiguredStructure(registry, "configured_sky_shrine", CONFIGURED_SKY_SHRINE);
        registerConfiguredStructure(registry, "configured_sun_shrine", CONFIGURED_SUN_SHRINE);
        registerConfiguredStructure(registry, "configured_moon_shrine", CONFIGURED_MOON_SHRINE);
    }
    
    protected static void registerConfiguredStructure(Registry<ConfiguredStructureFeature<?, ?>> registry, String key, ConfiguredStructureFeature<?, ?> value) {
        Registry.register(registry, new ResourceLocation(PrimalMagick.MODID, key), value);
    }
}
