package com.verdantartifice.primalmagick.common.init;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.crafting.WandTransformBlock;
import com.verdantartifice.primalmagick.common.crafting.WandTransformBlockTag;
import com.verdantartifice.primalmagick.common.crafting.WandTransforms;
import com.verdantartifice.primalmagick.common.items.ItemRegistration;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.keys.ResearchStageKey;
import com.verdantartifice.primalmagick.common.research.requirements.OrRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;
import com.verdantartifice.primalmagick.common.tags.BlockTagsForgeExt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.common.Tags;

/**
 * Point of registration for mod recipe types, as well as other crafting related things.
 * 
 * @author Daedalus4096
 */
public class InitRecipes {
    public static void initWandTransforms() {
        WandTransforms.register(new WandTransformBlockTag(Tags.Blocks.BOOKSHELVES, new ItemStack(ItemRegistration.GRIMOIRE.get()), new OrRequirement(new ResearchRequirement(new ResearchEntryKey(ResearchEntries.GOT_DREAM)), new ResearchRequirement(new ResearchStageKey(ResearchEntries.FIRST_STEPS, 1)))));
        WandTransforms.register(new WandTransformBlock(Blocks.CRAFTING_TABLE, new ItemStack(BlocksPM.ARCANE_WORKBENCH.get()), new ResearchRequirement(new ResearchStageKey(ResearchEntries.FIRST_STEPS, 1))));
        WandTransforms.register(new WandTransformBlockTag(BlockTagsForgeExt.FURNACES, new ItemStack(BlocksPM.ESSENCE_FURNACE.get()), new ResearchRequirement(new ResearchStageKey(ResearchEntries.BASIC_ALCHEMY, 1))));
    }
    
    public static void initCompostables() {
        ComposterBlock.COMPOSTABLES.put(ItemRegistration.MOONWOOD_SAPLING.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ItemRegistration.MOONWOOD_LEAVES.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ItemRegistration.SUNWOOD_SAPLING.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ItemRegistration.SUNWOOD_LEAVES.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ItemRegistration.HALLOWOOD_SAPLING.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ItemRegistration.HALLOWOOD_LEAVES.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ItemRegistration.HYDROMELON_SEEDS.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ItemRegistration.HYDROMELON.get(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(ItemRegistration.BLOOD_ROSE.get(), 0.85F);
    }
}
