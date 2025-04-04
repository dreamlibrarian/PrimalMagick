package com.verdantartifice.primalmagick.client.gui.grimoire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.RuneEnchantmentButton;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * Grimoire page showing the list of discovered rune enchantments.
 * 
 * @author Daedalus4096
 */
public class RuneEnchantmentIndexPage extends AbstractPage {
    public static final String TOPIC = "rune_enchantments";

    protected List<Enchantment> contents = new ArrayList<>();
    protected boolean firstPage;

    public RuneEnchantmentIndexPage() {
        this(false);
    }
    
    public RuneEnchantmentIndexPage(boolean first) {
        this.firstPage = first;
    }
    
    @Override
    public void render(PoseStack matrixStack, int side, int x, int y, int mouseX, int mouseY) {
        // Just render the title; buttons have already been added
        if (this.isFirstPage() && side == 0) {
            this.renderTitle(matrixStack, side, x, y, mouseX, mouseY, null);
        }
    }
    
    @Nonnull
    public List<Enchantment> getEnchantments() {
        return Collections.unmodifiableList(this.contents);
    }
    
    public boolean addEnchantment(Enchantment enchant) {
        return this.contents.add(enchant);
    }

    public boolean isFirstPage() {
        return this.firstPage;
    }
    
    @Override
    protected String getTitleTranslationKey() {
        return "primalmagick.grimoire.rune_enchantment_header";
    }

    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        // Add a button to the screen for each enchantment in the page's contents
        for (Enchantment enchant : this.getEnchantments()) {
            Component text = new TranslatableComponent(enchant.getDescriptionId());
            screen.addWidgetToScreen(new RuneEnchantmentButton(x + 12 + (side * 140), y, text, screen, enchant));
            y += 12;
        }
    }
}
