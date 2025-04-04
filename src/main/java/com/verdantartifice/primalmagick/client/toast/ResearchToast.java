package com.verdantartifice.primalmagick.client.toast;

import java.awt.Color;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

/**
 * GUI element for the toast that shows when you complete a research entry.
 * 
 * @author Daedalus4096
 */
public class ResearchToast implements Toast {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagick.MODID, "textures/gui/hud.png");
    
    protected ResearchEntry entry;
    
    public ResearchToast(ResearchEntry entry) {
        this.entry = entry;
    }
    
    @Override
    public Visibility render(PoseStack matrixStack, ToastComponent toastGui, long delta) {
        Minecraft mc = toastGui.getMinecraft();
        
        // Render the toast background
        RenderSystem.setShaderTexture(0, TEXTURE);
        toastGui.blit(matrixStack, 0, 0, 0, 224, 160, 32);
        
        // Render the toast title text
        Component titleText = new TranslatableComponent("primalmagick.toast.title");
        mc.font.draw(matrixStack, titleText, 6, 7, 0x551A8B);
        
        // Render the description of the completed research
        Component descText = new TranslatableComponent(this.entry.getNameTranslationKey());
        float width = mc.font.width(descText.getString());
        if (width > 148.0F) {
            // Scale down the research description to make it fit, if needed
            float scale = (148.0F / width);
            matrixStack.pushPose();
            matrixStack.translate(6.0F, 18.0F, 0.0F);
            matrixStack.scale(scale, scale, scale);
            mc.font.draw(matrixStack, descText, 0, 0, Color.BLACK.getRGB());
            matrixStack.popPose();
        } else {
            mc.font.draw(matrixStack, descText, 6, 18, Color.BLACK.getRGB());
        }
        
        // If the toast has been open long enough, hide it
        return (delta >= 5000L) ? Visibility.HIDE : Visibility.SHOW;
    }

}
