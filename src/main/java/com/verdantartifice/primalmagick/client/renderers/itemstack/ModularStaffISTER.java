package com.verdantartifice.primalmagick.client.renderers.itemstack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.verdantartifice.primalmagick.common.items.wands.ModularStaffItem;
import com.verdantartifice.primalmagick.common.wands.WandCap;
import com.verdantartifice.primalmagick.common.wands.WandCore;
import com.verdantartifice.primalmagick.common.wands.WandGem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;

/**
 * Custom item stack renderer for a modular staff.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagick.common.items.wands.ModularStaffItem}
 */
public class ModularStaffISTER extends BlockEntityWithoutLevelRenderer {
    public ModularStaffISTER() {
        super(Minecraft.getInstance() == null ? null : Minecraft.getInstance().getBlockEntityRenderDispatcher(), 
                Minecraft.getInstance() == null ? null : Minecraft.getInstance().getEntityModels());
    }
    
    @Override
    public void renderByItem(ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (itemStack.getItem() instanceof ModularStaffItem) {
            Minecraft mc = Minecraft.getInstance();
            ItemRenderer itemRenderer = mc.getItemRenderer();
            
            // Get the staff components so we can extract their model resource locations
            ModularStaffItem wand = (ModularStaffItem)itemStack.getItem();
            WandCore core = wand.getWandCore(itemStack);
            WandCap cap = wand.getWandCap(itemStack);
            WandGem gem = wand.getWandGem(itemStack);
            
            VertexConsumer builder = ItemRenderer.getFoilBufferDirect(buffer, RenderType.solid(), false, itemStack.hasFoil());
            if (core != null) {
                // Render the staff core
                BakedModel model = mc.getModelManager().getModel(core.getStaffModelResourceLocation());
                itemRenderer.renderModelLists(model, itemStack, combinedLight, combinedOverlay, matrixStack, builder);
            }
            if (cap != null) {
                // Render the staff cap
                BakedModel model = mc.getModelManager().getModel(cap.getStaffModelResourceLocation());
                itemRenderer.renderModelLists(model, itemStack, combinedLight, combinedOverlay, matrixStack, builder);
            }
            if (gem != null) {
                // Render the staff gem
                BakedModel model = mc.getModelManager().getModel(gem.getModelResourceLocation());
                itemRenderer.renderModelLists(model, itemStack, combinedLight, combinedOverlay, matrixStack, builder);
            }
        }
    }
}
