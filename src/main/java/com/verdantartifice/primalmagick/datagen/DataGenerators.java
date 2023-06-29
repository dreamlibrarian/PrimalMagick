package com.verdantartifice.primalmagick.datagen;

import java.util.Collections;
import java.util.List;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.datagen.affinities.AffinityProvider;
import com.verdantartifice.primalmagick.datagen.biome_modifiers.BiomeModifierProvider;
import com.verdantartifice.primalmagick.datagen.loot_modifiers.LootModifierProvider;
import com.verdantartifice.primalmagick.datagen.loot_tables.BlockLootTables;
import com.verdantartifice.primalmagick.datagen.loot_tables.EntityLootTables;
import com.verdantartifice.primalmagick.datagen.loot_tables.GameplayLootTables;
import com.verdantartifice.primalmagick.datagen.recipes.Recipes;
import com.verdantartifice.primalmagick.datagen.research.ResearchProvider;
import com.verdantartifice.primalmagick.datagen.runes.RuneEnchantmentProvider;
import com.verdantartifice.primalmagick.datagen.tags.BiomeTagsProviderPM;
import com.verdantartifice.primalmagick.datagen.tags.BlockTagsProviderPM;
import com.verdantartifice.primalmagick.datagen.tags.EntityTypeTagsProviderPM;
import com.verdantartifice.primalmagick.datagen.tags.ItemTagsProviderPM;
import com.verdantartifice.primalmagick.datagen.theorycrafting.ProjectProvider;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handlers for events related to data file generation.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagick.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        // Add all of the mod's data providers to the generator for processing
        DataGenerator generator = event.getGenerator();
        generator.addProvider(event.includeServer(), new Recipes(generator.getPackOutput()));
        generator.addProvider(event.includeServer(), (DataProvider.Factory<LootTableProvider>)(output -> new LootTableProvider(output, Collections.emptySet(), List.of(
                BlockLootTables.getSubProviderEntry(), EntityLootTables.getSubProviderEntry(), GameplayLootTables.getSubProviderEntry()))));
        BlockTagsProviderPM blockTagsProvider = new BlockTagsProviderPM(generator.getPackOutput(), event.getLookupProvider(), event.getExistingFileHelper());
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(), new ItemTagsProviderPM(generator.getPackOutput(), event.getLookupProvider(), blockTagsProvider.contentsGetter(), event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new EntityTypeTagsProviderPM(generator.getPackOutput(), event.getLookupProvider(), event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new BiomeTagsProviderPM(generator.getPackOutput(), event.getLookupProvider(), event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new AffinityProvider(generator.getPackOutput()));
        generator.addProvider(event.includeServer(), new ResearchProvider(generator.getPackOutput()));
        generator.addProvider(event.includeServer(), new ProjectProvider(generator.getPackOutput()));
        generator.addProvider(event.includeServer(), new LootModifierProvider(generator.getPackOutput()));
        generator.addProvider(event.includeServer(), (DataProvider.Factory<BiomeModifierProvider>)(output -> new BiomeModifierProvider(output, event.getLookupProvider())));
        generator.addProvider(event.includeServer(), new RuneEnchantmentProvider(generator.getPackOutput()));
    }
}
