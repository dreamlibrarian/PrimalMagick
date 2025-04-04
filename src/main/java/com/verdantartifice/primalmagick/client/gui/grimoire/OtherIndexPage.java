package com.verdantartifice.primalmagick.client.gui.grimoire;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.AttunementIndexButton;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.DisciplineButton;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.RecipeIndexButton;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.RuneEnchantmentIndexButton;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.StatisticsButton;
import com.verdantartifice.primalmagick.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagick.common.research.ResearchDisciplines;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

/**
 * Grimoire page showing a list of topics not directly tied to research entries.
 * 
 * @author Daedalus4096
 */
public class OtherIndexPage extends AbstractPage {
    @Override
    public void render(PoseStack matrixStack, int side, int x, int y, int mouseX, int mouseY) {
        // Just render the title; buttons have already been added
        this.renderTitle(matrixStack, side, x, y, mouseX, mouseY, null);
    }

    @Override
    protected String getTitleTranslationKey() {
        return "primalmagick.grimoire.other_header";
    }

    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        Minecraft mc = Minecraft.getInstance();
        Component text;
        
        // Make room for page title
        y += 24;
        
        // Add attunements button if attunements are unlocked
        if (ResearchManager.isResearchComplete(mc.player, SimpleResearchKey.parse("ATTUNEMENTS"))) {
            text = new TranslatableComponent("primalmagick.grimoire.attunement_header");
            screen.addWidgetToScreen(new AttunementIndexButton(x + 12 + (side * 140), y, text, screen));
            y += 12;
        }
        
        // Add recipes button
        ResearchEntry firstSteps = ResearchEntries.getEntry(SimpleResearchKey.FIRST_STEPS);
        if (firstSteps != null && (firstSteps.isComplete(mc.player) || firstSteps.isInProgress(mc.player))) {
            text = new TranslatableComponent("primalmagick.grimoire.recipe_index_header");
            screen.addWidgetToScreen(new RecipeIndexButton(x + 12 + (side * 140), y, text, screen));
            y += 12;
        }
        
        // Add rune enchantments button if rune enchantments are unlocked
        if (ResearchManager.isResearchComplete(mc.player, SimpleResearchKey.parse("UNLOCK_RUNE_ENCHANTMENTS"))) {
            text = new TranslatableComponent("primalmagick.grimoire.rune_enchantment_header");
            screen.addWidgetToScreen(new RuneEnchantmentIndexButton(x + 12 + (side * 140), y, text, screen));
            y += 12;
        }
        
        // Add scans button if at least one scan is unlocked
        ResearchDiscipline scans = ResearchDisciplines.getDiscipline("SCANS");
        if (scans != null && scans.getUnlockResearchKey().isKnownBy(Minecraft.getInstance().player)) {
            text = new TranslatableComponent(scans.getNameTranslationKey());
            screen.addWidgetToScreen(new DisciplineButton(x + 12 + (side * 140), y, text, screen, scans));
            y += 12;
        }
        
        // Add statistics button
        text = new TranslatableComponent("primalmagick.grimoire.stats_header");
        screen.addWidgetToScreen(new StatisticsButton(x + 12 + (side * 140), y, text, screen));
        y += 12;
    }
}
