package com.verdantartifice.primalmagick.client.compat.jei;

import java.util.List;

import com.verdantartifice.primalmagick.common.crafting.IArcaneRecipe;
import com.verdantartifice.primalmagick.common.crafting.IConcoctingRecipe;
import com.verdantartifice.primalmagick.common.crafting.IDissolutionRecipe;
import com.verdantartifice.primalmagick.common.crafting.IRitualRecipe;
import com.verdantartifice.primalmagick.common.crafting.IRunecarvingRecipe;
import com.verdantartifice.primalmagick.common.crafting.RecipeTypesPM;

import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

/**
 * Helper class to fetch which recipes belong to each recipe category.
 * 
 * @author Daedalus4096
 */
public class CategoryRecipes {
    private final RecipeManager recipeManager;
    
    public CategoryRecipes() {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null) {
            throw new IllegalStateException("Minecraft instance not initialized");
        }
        ClientLevel level = mc.level;
        if (level == null) {
            throw new IllegalStateException("Client level instance not initialized");
        }
        this.recipeManager = level.getRecipeManager();
        if (this.recipeManager == null) {
            throw new IllegalStateException("Recipe manager instance not initialized");
        }
    }
    
    public List<IArcaneRecipe> getArcaneRecipes(IRecipeCategory<IArcaneRecipe> category) {
        CategoryRecipeValidatorPM<IArcaneRecipe> validator = new CategoryRecipeValidatorPM<>(category, 9, true);
        return getValidHandledRecipes(this.recipeManager, RecipeTypesPM.ARCANE_CRAFTING, validator);
    }
    
    public List<IConcoctingRecipe> getConcoctingRecipes(IRecipeCategory<IConcoctingRecipe> category) {
        CategoryRecipeValidatorPM<IConcoctingRecipe> validator = new CategoryRecipeValidatorPM<>(category, 9, true);
        return getValidHandledRecipes(this.recipeManager, RecipeTypesPM.CONCOCTING, validator);
    }
    
    public List<IRunecarvingRecipe> getRunecarvingRecipes(IRecipeCategory<IRunecarvingRecipe> category) {
        CategoryRecipeValidatorPM<IRunecarvingRecipe> validator = new CategoryRecipeValidatorPM<>(category, 2, true);
        return getValidHandledRecipes(this.recipeManager, RecipeTypesPM.RUNECARVING, validator);
    }
    
    public List<IDissolutionRecipe> getDissolutionRecipes(IRecipeCategory<IDissolutionRecipe> category) {
        CategoryRecipeValidatorPM<IDissolutionRecipe> validator = new CategoryRecipeValidatorPM<>(category, 1, true);
        return getValidHandledRecipes(this.recipeManager, RecipeTypesPM.DISSOLUTION, validator);
    }
    
    public List<IRitualRecipe> getRitualRecipes(IRecipeCategory<IRitualRecipe> category) {
        CategoryRecipeValidatorPM<IRitualRecipe> validator = new CategoryRecipeValidatorPM<>(category, 100, true);  // TODO Fix max inputs for JEI rituals
        return getValidHandledRecipes(this.recipeManager, RecipeTypesPM.RITUAL, validator);
    }
    
    private static <C extends Container, T extends Recipe<C>> List<T> getValidHandledRecipes(RecipeManager recipeManager, RecipeType<T> recipeType, CategoryRecipeValidatorPM<T> validator) {
        return recipeManager.getAllRecipesFor(recipeType).stream().filter(r -> validator.isRecipeValid(r) && validator.isRecipeHandled(r)).toList();
    }
}
