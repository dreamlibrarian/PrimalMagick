package com.verdantartifice.primalmagick.client.gui;

import java.util.List;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.util.GuiUtils;
import com.verdantartifice.primalmagick.common.crafting.IRunecarvingRecipe;
import com.verdantartifice.primalmagick.common.menus.RunecarvingTableMenu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

/**
 * GUI screen for runecarving table block.
 * 
 * @author Daedalus4096
 */
public class RunecarvingTableScreen extends AbstractContainerScreen<RunecarvingTableMenu> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagick.MODID, "textures/gui/runecarving_table.png");
    
    protected float sliderProgress;
    protected boolean clickedOnSroll;
    
    /**
     * The index of the first recipe to display.
     * The number of recipes displayed at any time is 12 (4 recipes per row, and 3 rows). If the player scrolled down one
     * row, this value would be 4 (representing the index of the first slot on the second row).
     */
    protected int recipeIndexOffset;
    protected boolean hasItemsInInputSlot;

    public RunecarvingTableScreen(RunecarvingTableMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        screenContainer.setInventoryUpdateListener(this::onInventoryUpdate);
    }
    
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        int i = this.leftPos;
        int j = this.topPos;
        guiGraphics.blit(TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight);
        int k = (int)(41.0F * this.sliderProgress);
        guiGraphics.blit(TEXTURE, i + 119, j + 15 + k, 176 + (this.canScroll() ? 0 : 12), 0, 12, 15);
        int l = this.leftPos + 52;
        int i1 = this.topPos + 14;
        int j1 = this.recipeIndexOffset + 12;
        this.drawRecipesBackground(guiGraphics, mouseX, mouseY, l, i1, j1);
        this.drawRecipesItems(guiGraphics, mouseX, mouseY, l, i1, j1);
    }
    
    protected void drawRecipesBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, int left, int top, int recipeIndexOffsetMax) {
        for (int i = this.recipeIndexOffset; i < recipeIndexOffsetMax && i < this.menu.getRecipeListSize(); ++i) {
            int j = i - this.recipeIndexOffset;
            int k = left + j % 4 * 16;
            int l = j / 4;
            int i1 = top + l * 18 + 2;
            int j1 = this.imageHeight;
            if (i == this.menu.getSelectedRecipe()) {
                j1 += 18;
            } else if (mouseX >= k && mouseY >= i1 && mouseX < k + 16 && mouseY < i1 + 18) {
                j1 += 36;
            }
            guiGraphics.blit(TEXTURE, k, i1 - 1, 0, j1, 16, 18);
        }
    }
    
    protected void drawRecipesItems(GuiGraphics guiGraphics, int mouseX, int mouseY, int left, int top, int recipeIndexOffsetMax) {
        List<IRunecarvingRecipe> list = this.menu.getRecipeList();
        for (int i = this.recipeIndexOffset; i < recipeIndexOffsetMax && i < this.menu.getRecipeListSize(); ++i) {
            int j = i - this.recipeIndexOffset;
            int k = left + j % 4 * 16;
            int l = j / 4;
            int i1 = top + l * 18 + 2;
            ItemStack output = list.get(i).getResultItem(this.minecraft.level.registryAccess());
            guiGraphics.renderItem(output, k, i1);
            if (mouseX >= k && mouseX < k + 16 && mouseY >= i1 && mouseY < i1 + 18) {
                GuiUtils.renderItemTooltip(guiGraphics, output, mouseX, mouseY);
            }
        }
    }
    
    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        this.clickedOnSroll = false;
        if (this.hasItemsInInputSlot) {
            int i = this.leftPos + 52;
            int j = this.topPos + 14;
            int k = this.recipeIndexOffset + 12;

            for(int l = this.recipeIndexOffset; l < k; ++l) {
                int i1 = l - this.recipeIndexOffset;
                double d0 = p_mouseClicked_1_ - (double)(i + i1 % 4 * 16);
                double d1 = p_mouseClicked_3_ - (double)(j + i1 / 4 * 18);
                if (d0 >= 0.0D && d1 >= 0.0D && d0 < 16.0D && d1 < 18.0D && this.menu.clickMenuButton(this.minecraft.player, l)) {
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
                    this.minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, l);
                    return true;
                }
            }

            i = this.leftPos + 119;
            j = this.topPos + 9;
            if (p_mouseClicked_1_ >= (double)i && p_mouseClicked_1_ < (double)(i + 12) && p_mouseClicked_3_ >= (double)j && p_mouseClicked_3_ < (double)(j + 54)) {
                this.clickedOnSroll = true;
            }
        }
        return super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
    }
    
    @Override
    public boolean mouseDragged(double p_mouseDragged_1_, double p_mouseDragged_3_, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) {
        if (this.clickedOnSroll && this.canScroll()) {
            int i = this.topPos + 14;
            int j = i + 54;
            this.sliderProgress = ((float)p_mouseDragged_3_ - (float)i - 7.5F) / ((float)(j - i) - 15.0F);
            this.sliderProgress = Mth.clamp(this.sliderProgress, 0.0F, 1.0F);
            this.recipeIndexOffset = (int)((double)(this.sliderProgress * (float)this.getHiddenRows()) + 0.5D) * 4;
            return true;
        } else {
            return super.mouseDragged(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_);
        }
    }
    
    @Override
    public boolean mouseScrolled(double p_mouseScrolled_1_, double p_mouseScrolled_3_, double p_mouseScrolled_5_) {
        if (this.canScroll()) {
            int i = this.getHiddenRows();
            this.sliderProgress = (float)((double)this.sliderProgress - p_mouseScrolled_5_ / (double)i);
            this.sliderProgress = Mth.clamp(this.sliderProgress, 0.0F, 1.0F);
            this.recipeIndexOffset = (int)((double)(this.sliderProgress * (float)i) + 0.5D) * 4;
        }
        return true;
    }
    
    protected boolean canScroll() {
        return this.hasItemsInInputSlot && this.menu.getRecipeListSize() > 12;
    }
    
    protected int getHiddenRows() {
        return (this.menu.getRecipeListSize() + 4 - 1) / 4 - 3;
    }
    
    /**
     * Called every time this screen's container is changed (is marked as dirty).
     */
    protected void onInventoryUpdate() {
        this.hasItemsInInputSlot = this.menu.hasItemsInInputSlot();
        if (!this.hasItemsInInputSlot) {
            this.sliderProgress = 0.0F;
            this.recipeIndexOffset = 0;
        }
    }
}
