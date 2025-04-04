package com.verdantartifice.primalmagick.common.items.tools;

import java.util.function.Consumer;

import com.verdantartifice.primalmagick.client.renderers.itemstack.HexiumTridentISTER;
import com.verdantartifice.primalmagick.common.entities.projectiles.AbstractTridentEntity;
import com.verdantartifice.primalmagick.common.entities.projectiles.HexiumTridentEntity;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.IItemRenderProperties;

/**
 * Definition for a trident made of the magickal metal hexium.
 * 
 * @author Daedalus4096
 */
public class HexiumTridentItem extends AbstractTieredTridentItem {
    protected IItemRenderProperties renderProps;
    
    public HexiumTridentItem(Item.Properties properties) {
        super(ItemTierPM.HEXIUM, properties);
    }

    @Override
    protected AbstractTridentEntity getThrownEntity(Level world, LivingEntity thrower, ItemStack stack) {
        return new HexiumTridentEntity(world, thrower, stack);
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(this.getRenderProperties());
    }
    
    public IItemRenderProperties getRenderProperties() {
        if (this.renderProps == null) {
            this.renderProps = new IItemRenderProperties() {
                final BlockEntityWithoutLevelRenderer renderer = new HexiumTridentISTER();

                @Override
                public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                    return renderer;
                }
            };
        }
        return this.renderProps;
    }
}
