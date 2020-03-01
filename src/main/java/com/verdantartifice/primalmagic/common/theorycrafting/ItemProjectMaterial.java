package com.verdantartifice.primalmagic.common.theorycrafting;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.common.util.InventoryUtils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

/**
 * Definition of a project material that requires a specific item stack, which may or may not be
 * consumed as part of the project.
 * 
 * @author Daedalus4096
 */
public class ItemProjectMaterial extends AbstractProjectMaterial {
    public static final String TYPE = "item";
    
    protected ItemStack stack;
    protected boolean consumed;
    
    public ItemProjectMaterial() {
        super();
        this.stack = ItemStack.EMPTY;
        this.consumed = false;
    }
    
    public ItemProjectMaterial(@Nonnull ItemStack stack, boolean consumed) {
        super();
        this.stack = stack;
        this.consumed = consumed;
    }
    
    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = super.serializeNBT();
        tag.put("Stack", this.stack.write(new CompoundNBT()));
        tag.putBoolean("Consumed", this.consumed);
        return tag;
    }
    
    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        this.stack = ItemStack.read(nbt.getCompound("Stack"));
        this.consumed = nbt.getBoolean("Consumed");
    }

    @Override
    protected String getMaterialType() {
        return TYPE;
    }

    @Override
    public boolean isSatisfied(PlayerEntity player) {
        // The material is satisfied if the given player is carrying this material's item
        return InventoryUtils.isPlayerCarrying(player, this.stack);
    }

    @Override
    public boolean consume(PlayerEntity player) {
        // Remove this material's item from the player's inventory if it's supposed to be consumed
        if (this.consumed) {
            return InventoryUtils.consumeItem(player, this.stack);
        } else {
            return true;
        }
    }
    
    @Override
    public boolean isConsumed() {
        return this.consumed;
    }
    
    @Override
    public AbstractProjectMaterial copy() {
        ItemProjectMaterial material = new ItemProjectMaterial();
        material.stack = this.stack.copy();
        material.consumed = this.consumed;
        material.selected = this.selected;
        return material;
    }
}
