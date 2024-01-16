package com.verdantartifice.primalmagick.client.gui;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.menus.ScribeStudyVocabularyMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * GUI screen for the study vocabulary mode of the scribe table block.
 * 
 * @author Daedalus4096
 */
public class ScribeStudyVocabularyScreen extends AbstractContainerScreen<ScribeStudyVocabularyMenu> {
    protected static final ResourceLocation TEXTURE = PrimalMagick.resource("textures/gui/scribe_study_vocabulary.png");
    
    public ScribeStudyVocabularyScreen(ScribeStudyVocabularyMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        // Render background texture
        pGuiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }
}
