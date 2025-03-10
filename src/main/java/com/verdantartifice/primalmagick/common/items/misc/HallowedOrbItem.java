package com.verdantartifice.primalmagick.common.items.misc;

import java.util.List;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

/**
 * Item definition for a hallowed orb.  A hallowed orb unlocks the Hallowed source when scanned on an
 * analysis table or with an arcanometer.
 * 
 * @author Daedalus4096
 */
public class HallowedOrbItem extends Item {
    public HallowedOrbItem() {
        super(new Item.Properties().tab(PrimalMagick.ITEM_GROUP).rarity(Rarity.RARE));
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(new TranslatableComponent("tooltip.primalmagick.hallowed_orb.1").withStyle(ChatFormatting.WHITE, ChatFormatting.ITALIC));
        tooltip.add(new TranslatableComponent("tooltip.primalmagick.hallowed_orb.2").withStyle(ChatFormatting.WHITE, ChatFormatting.ITALIC));
    }
}
