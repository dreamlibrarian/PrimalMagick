package com.verdantartifice.primalmagick.common.tags;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags.IOptionalNamedTag;

public class BlockTagsForgeExt {
    public static final IOptionalNamedTag<Block> BOOKSHELVES = tag("bookshelves");
    
    private static IOptionalNamedTag<Block> tag(String name) {
        return BlockTags.createOptional(new ResourceLocation("forge", name));
    }
}
