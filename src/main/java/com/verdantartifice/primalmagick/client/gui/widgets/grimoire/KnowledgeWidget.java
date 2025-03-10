package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import java.awt.Color;
import java.util.Collections;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.util.GuiUtils;
import com.verdantartifice.primalmagick.common.research.Knowledge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

/**
 * Display widget for showing required knowledge (e.g. observations).
 * 
 * @author Daedalus4096
 */
public class KnowledgeWidget extends AbstractWidget {
    protected static final ResourceLocation GRIMOIRE_TEXTURE = new ResourceLocation(PrimalMagick.MODID, "textures/gui/grimoire.png");

    protected Knowledge knowledge;
    protected boolean isComplete;
    
    public KnowledgeWidget(Knowledge knowledge, int x, int y, boolean isComplete) {
        super(x, y, 16, 16, TextComponent.EMPTY);
        this.knowledge = knowledge;
        this.isComplete = isComplete;
    }
    
    @Override
    public void renderButton(PoseStack matrixStack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft mc = Minecraft.getInstance();
        
        matrixStack.pushPose();
        
        // Draw knowledge type icon
        RenderSystem.setShaderTexture(0, this.knowledge.getType().getIconLocation());
        matrixStack.translate(this.x, this.y, 0.0F);
        matrixStack.scale(0.0625F, 0.0625F, 0.0625F);
        this.blit(matrixStack, 0, 0, 0, 0, 255, 255);
        
        matrixStack.popPose();
        
        // Draw amount str
        Component amountText = new TextComponent(Integer.toString(this.knowledge.getAmount()));
        int width = mc.font.width(amountText.getString());
        matrixStack.pushPose();
        matrixStack.translate(this.x + 16 - width / 2, this.y + 12, 5.0F);
        matrixStack.scale(0.5F, 0.5F, 0.5F);
        mc.font.drawShadow(matrixStack, amountText, 0.0F, 0.0F, this.isComplete ? Color.WHITE.getRGB() : Color.RED.getRGB());
        matrixStack.popPose();
        
        if (this.isComplete) {
            // Render completion checkmark if appropriate
            matrixStack.pushPose();
            matrixStack.translate(this.x + 8, this.y, 100.0F);
            RenderSystem.setShaderTexture(0, GRIMOIRE_TEXTURE);
            this.blit(matrixStack, 0, 0, 159, 207, 10, 10);
            matrixStack.popPose();
        }
        
        if (this.isHoveredOrFocused()) {
            // Render tooltip
            Component knowledgeText = new TranslatableComponent(this.knowledge.getType().getNameTranslationKey());
            GuiUtils.renderCustomTooltip(matrixStack, Collections.singletonList(knowledgeText), this.x, this.y);
        }
    }
    
    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }

    @Override
    public void updateNarration(NarrationElementOutput output) {
    }
}
