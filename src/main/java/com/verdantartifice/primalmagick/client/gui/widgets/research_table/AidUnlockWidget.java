package com.verdantartifice.primalmagick.client.gui.widgets.research_table;

import java.util.Collections;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.util.GuiUtils;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

/**
 * Display widget for showing that a project was unlocked by a research aid in the research table GUI.
 * 
 * @author Daedalus4096
 */
public class AidUnlockWidget extends AbstractWidget {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagick.MODID, "textures/gui/research_table_overlay.png");
    
    protected Block aidBlock;

    public AidUnlockWidget(int x, int y, @Nonnull Block aidBlock) {
        super(x, y, 8, 8, TextComponent.EMPTY);
        this.aidBlock = aidBlock;
    }
    
    @Override
    public void renderButton(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        // Draw padlock icon
        matrixStack.pushPose();
        RenderSystem.setShaderTexture(0, TEXTURE);
        matrixStack.translate(this.x, this.y, 0.0F);
        this.blit(matrixStack, 0, 0, 198, 0, 8, 8);
        matrixStack.popPose();
    }
    
    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }

    @Override
    public void updateNarration(NarrationElementOutput output) {
    }

    @Override
    public void renderToolTip(PoseStack matrixStack, int mouseX, int mouseY) {
        if (this.aidBlock != null) {
            // Render tooltip
            Component unlockText = new TranslatableComponent("primalmagick.research_table.unlock", this.aidBlock.getName());
            GuiUtils.renderCustomTooltip(matrixStack, Collections.singletonList(unlockText), mouseX, mouseY);
        }
    }
}
