package com.verdantartifice.primalmagick.common.research;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

public class IconDefinition {
    public static final Codec<IconDefinition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("isItem").forGetter(IconDefinition::isItem),
            Codec.BOOL.fieldOf("isTag").forGetter(IconDefinition::isTag),
            ResourceLocation.CODEC.fieldOf("location").forGetter(IconDefinition::getLocation),
            Codec.STRING.optionalFieldOf("tooltipOverride").forGetter(def -> def.tooltipOverrideOpt)
        ).apply(instance, IconDefinition::new));
    
    private final boolean isItem;
    private final boolean isTag;
    private final ResourceLocation location;
    private final Optional<String> tooltipOverrideOpt;
    
    private IconDefinition(boolean isItem, boolean isTag, ResourceLocation loc, Optional<String> tooltipOverrideOpt) {
        this.isItem = isItem;
        this.isTag = isTag;
        this.location = loc;
        this.tooltipOverrideOpt = tooltipOverrideOpt;
    }
    
    public static IconDefinition of(ItemLike item) {
        return new IconDefinition(true, false, ForgeRegistries.ITEMS.getKey(item.asItem()), Optional.empty());
    }
    
    public static IconDefinition of(TagKey<Item> tagKey) {
        return new IconDefinition(false, true, tagKey.location(), Optional.empty());
    }
    
    public static IconDefinition of(ResourceLocation loc) {
        return of(loc, null);
    }
    
    public static IconDefinition of(ResourceLocation loc, String tooltipKey) {
        return new IconDefinition(false, false, Preconditions.checkNotNull(loc), Optional.ofNullable(tooltipKey));
    }
    
    public boolean isItem() {
        return this.isItem;
    }
    
    public boolean isTag() {
        return this.isTag;
    }
    
    public ResourceLocation getLocation() {
        return this.location;
    }
    
    @Nullable
    public Item asItem() {
        return this.isItem ? ForgeRegistries.ITEMS.getValue(this.location) : null;
    }
    
    @Nullable
    public TagKey<Item> asTagKey() {
        return this.isTag ? TagKey.create(Registries.ITEM, this.location) : null;
    }
    
    public List<Component> getTooltipLines() {
        if (this.tooltipOverrideOpt.isPresent()) {
            return ImmutableList.of(Component.translatable(this.tooltipOverrideOpt.get()));
        } else if (this.isItem) {
            return ImmutableList.of(this.asItem().getDescription());
        } else if (this.isTag) {
            return ForgeRegistries.ITEMS.tags().getTag(this.asTagKey()).stream().map(Item::getDescription).toList();
        } else {
            return ImmutableList.of();
        }
    }
    
    @Nullable
    public static IconDefinition fromNetwork(FriendlyByteBuf buf) {
        return new IconDefinition(buf.readBoolean(), buf.readBoolean(), buf.readResourceLocation(), buf.readOptional(b -> b.readUtf()));
    }
    
    public static void toNetwork(FriendlyByteBuf buf, @Nullable IconDefinition icon) {
        buf.writeBoolean(icon.isItem);
        buf.writeBoolean(icon.isTag);
        buf.writeResourceLocation(icon.location);
        buf.writeOptional(icon.tooltipOverrideOpt, (b, s) -> buf.writeUtf(s));
    }
}
