package com.verdantartifice.primalmagick.common.research.keys;

import java.util.Objects;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.util.ItemUtils;

import net.minecraft.world.item.ItemStack;

public class StackCraftedKey extends AbstractResearchKey {
    public static final Codec<StackCraftedKey> CODEC = ItemStack.CODEC.fieldOf("stack").xmap(StackCraftedKey::new, key -> key.stack).codec();
    private static final String PREFIX = "[#]";
    
    protected final ItemStack stack;
    
    public StackCraftedKey(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            throw new IllegalArgumentException("Item stack may not be null or empty");
        }
        this.stack = stack.copyWithCount(1);    // Preserve the stack NBT but not its count
    }
    
    @Override
    public String toString() {
        return PREFIX + ItemUtils.getHashCode(this.stack);
    }

    @Override
    protected ResearchKeyType<?> getType() {
        return ResearchKeyTypesPM.STACK_CRAFTED.get();
    }

    @Override
    public int hashCode() {
        return Objects.hash(stack);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StackCraftedKey other = (StackCraftedKey) obj;
        return ItemStack.isSameItemSameTags(this.stack, other.stack);
    }
}
