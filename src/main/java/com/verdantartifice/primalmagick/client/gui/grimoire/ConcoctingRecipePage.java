package com.verdantartifice.primalmagick.client.gui.grimoire;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.ManaCostSummaryWidget;
import com.verdantartifice.primalmagick.common.crafting.ConcoctingRecipe;

/**
 * Grimoire page showing a concocting recipe.
 * 
 * @author Daedalus4096
 */
public class ConcoctingRecipePage extends AbstractShapelessRecipePage<ConcoctingRecipe> {
    public ConcoctingRecipePage(ConcoctingRecipe recipe) {
        super(recipe);
    }
    
    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        // Add base page widgets
        super.initWidgets(screen, side, x, y);
        
        // Add mana cost summary widget
        int indent = 124;
        int overlayWidth = 52;
        if (!this.recipe.getManaCosts().isEmpty()) {
            screen.addWidgetToScreen(new ManaCostSummaryWidget(this.recipe.getManaCosts(), x + 75 + (side * 140) + (indent / 2) - (overlayWidth / 2), y + 30));
        }
    }

    @Override
    protected String getRecipeTypeTranslationKey() {
        return "primalmagick.grimoire.concocting_recipe_header";
    }
}
