package com.verdantartifice.primalmagic.client.gui.grimoire.pages;

import com.verdantartifice.primalmagic.client.gui.grimoire.GrimoireScreen;
import com.verdantartifice.primalmagic.client.gui.grimoire.widgets.AttunementButton;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Grimoire page showing the list of discovered attunements.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class AttunementIndexPage extends AbstractPage {
    public static final String TOPIC = "attunements";

    protected boolean firstPage;

    public AttunementIndexPage() {
        this(false);
    }
    
    public AttunementIndexPage(boolean first) {
        this.firstPage = first;
    }
    
    @Override
    public void render(int side, int x, int y, int mouseX, int mouseY) {
        // Just render the title; buttons have already been added
        if (this.isFirstPage() && side == 0) {
            this.renderTitle(side, x, y, mouseX, mouseY);
        }
    }

    public boolean isFirstPage() {
        return this.firstPage;
    }
    
    @Override
    protected String getTitleTranslationKey() {
        return "primalmagic.grimoire.attunement_header";
    }

    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        // Add a button to the screen for each discovered source
        for (Source source : Source.SORTED_SOURCES) {
            if (source.isDiscovered(Minecraft.getInstance().player)) {
                String text = new TranslationTextComponent(source.getNameTranslationKey()).getFormattedText();
                screen.addWidgetToScreen(new AttunementButton(x + 12 + (side * 140), y, text, screen, source));
                y += 12;
            }
        }
    }

}
