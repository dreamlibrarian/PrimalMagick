package com.verdantartifice.primalmagick.client.gui.recipe_book;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.client.recipe_book.ArcaneRecipeBookCategories;
import com.verdantartifice.primalmagick.client.recipe_book.ArcaneSearchRegistry;
import com.verdantartifice.primalmagick.client.recipe_book.ClientArcaneRecipeBook;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.containers.AbstractArcaneRecipeBookMenu;
import com.verdantartifice.primalmagick.common.crafting.recipe_book.ArcaneRecipeBookType;
import com.verdantartifice.primalmagick.common.crafting.recipe_book.StackedNbtContents;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.recipe_book.ChangeArcaneRecipeBookSettingsPacket;
import com.verdantartifice.primalmagick.common.network.packets.recipe_book.PlaceArcaneRecipePacket;
import com.verdantartifice.primalmagick.common.network.packets.recipe_book.SeenArcaneRecipePacket;

import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.ChatFormatting;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StateSwitchingButton;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.recipebook.GhostRecipe;
import net.minecraft.client.gui.screens.recipebook.RecipeShownListener;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.searchtree.SearchRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.recipebook.PlaceRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

/**
 * Screen component for the arcane recipe book.
 * 
 * @author Daedalus4096
 */
public class ArcaneRecipeBookComponent extends GuiComponent implements Widget, GuiEventListener, NarratableEntry, RecipeShownListener, PlaceRecipe<Ingredient> {
    protected static final ResourceLocation RECIPE_BOOK_LOCATION = new ResourceLocation("textures/gui/recipe_book.png");
    protected static final Component SEARCH_HINT = (new TranslatableComponent("gui.recipebook.search_hint")).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY);
    public static final int IMAGE_WIDTH = 147;
    public static final int IMAGE_HEIGHT = 166;
    private static final int OFFSET_X_POSITION = 86;
    private static final Component ONLY_CRAFTABLES_TOOLTIP = new TranslatableComponent("gui.recipebook.toggleRecipes.craftable");
    private static final Component ALL_RECIPES_TOOLTIP = new TranslatableComponent("gui.recipebook.toggleRecipes.all");
    
    protected int xOffset;
    protected int width;
    protected int height;
    protected final GhostRecipe ghostRecipe = new GhostRecipe();
    protected final List<ArcaneRecipeBookTabButton> tabButtons = new ArrayList<>();
    @Nullable
    protected ArcaneRecipeBookTabButton selectedTab;
    protected StateSwitchingButton filterButton;
    protected AbstractArcaneRecipeBookMenu<?> menu;
    protected Minecraft mc;
    @Nullable
    protected EditBox searchBox;
    protected String lastSearch = "";
    protected ClientRecipeBook vanillaBook;
    protected ClientArcaneRecipeBook arcaneBook;
    protected final ArcaneRecipeBookPage recipeBookPage = new ArcaneRecipeBookPage();
    protected final StackedNbtContents stackedContents = new StackedNbtContents();
    protected int timesInventoryChanged;
    protected boolean ignoreTextInput;
    protected boolean visible;
    protected boolean widthTooNarrow;
    protected boolean useFurnaceStyle;

    public void init(int width, int height, Minecraft mc, boolean tooNarrow, boolean useFurnaceStyle, AbstractArcaneRecipeBookMenu<?> menu) {
        this.mc = mc;
        this.width = width;
        this.height = height;
        this.menu = menu;
        this.widthTooNarrow = tooNarrow;
        this.useFurnaceStyle = useFurnaceStyle;
        mc.player.containerMenu = menu;
        this.vanillaBook = mc.player.getRecipeBook();
        
        this.arcaneBook = new ClientArcaneRecipeBook(PrimalMagickCapabilities.getArcaneRecipeBook(mc.player).orElseThrow(() -> new IllegalArgumentException("No arcane recipe book for player")).get());
        this.arcaneBook.setupCollections(this.mc.level.getRecipeManager().getRecipes());
        this.arcaneBook.getCollections().forEach(collection -> {
            collection.updateKnownRecipes(this.vanillaBook, this.arcaneBook.getData());
        });
        
        this.visible = this.isVisibleAccordingToBookData();
        if (this.visible) {
            this.initVisuals();
        }
        mc.keyboardHandler.setSendRepeatsToGui(true);
    }
    
    public void initVisuals() {
        this.xOffset = this.widthTooNarrow ? 0 : OFFSET_X_POSITION;
        int xPos = (this.width - 147) / 2 - this.xOffset;
        int yPos = (this.height - 166) / 2;
        this.stackedContents.clear();
        this.mc.player.getInventory().fillStackedContents(this.stackedContents);
        this.menu.fillCraftSlotsStackedContents(this.stackedContents);
        String s = this.searchBox != null ? this.searchBox.getValue() : "";
        this.searchBox = new EditBox(this.mc.font, xPos + 25, yPos + 14, 80, 9 + 5, new TranslatableComponent("itemGroup.search"));
        this.searchBox.setMaxLength(50);
        this.searchBox.setBordered(false);
        this.searchBox.setVisible(true);
        this.searchBox.setTextColor(0xFFFFFF);
        this.searchBox.setValue(s);
        this.recipeBookPage.init(this.mc, xPos, yPos, this.arcaneBook.getData());
        this.recipeBookPage.addListener(this);
        this.filterButton = new StateSwitchingButton(xPos + 110, yPos + 12, 26, 16, this.arcaneBook.getData().isFiltering(this.menu.getRecipeBookType()));
        this.initFilterButtonTextures();
        this.tabButtons.clear();

        for (ArcaneRecipeBookCategories category : this.menu.getRecipeBookCategories()) {
            this.tabButtons.add(new ArcaneRecipeBookTabButton(category));
        }
        
        if (this.selectedTab != null) {
            this.selectedTab = this.tabButtons.stream().filter(tab -> {
                return tab.getCategory().equals(this.selectedTab.getCategory());
            }).findFirst().orElse(null);
        }
        if (this.selectedTab == null) {
            this.selectedTab = this.tabButtons.get(0);
        }
        
        this.selectedTab.setStateTriggered(true);
        this.updateCollections(false);
        this.updateTabs();
    }

    @Override
    public boolean changeFocus(boolean focus) {
        return false;
    }
    
    protected void initFilterButtonTextures() {
        if (this.useFurnaceStyle) {
            this.filterButton.initTextureValues(152, 182, 28, 18, RECIPE_BOOK_LOCATION);
        } else {
            this.filterButton.initTextureValues(152, 41, 28, 18, RECIPE_BOOK_LOCATION);
        }
    }
    
    public void removed() {
        this.mc.keyboardHandler.setSendRepeatsToGui(false);
    }
    
    public int updateScreenPosition(int width, int imageWidth) {
        int retVal;
        if (this.isVisible() && !this.widthTooNarrow) {
            retVal = 177 + (width - imageWidth - 200) / 2;
        } else {
            retVal = (width - imageWidth) / 2;
        }
        return retVal;
    }
    
    public void toggleVisibility() {
        this.setVisible(!this.isVisible());
    }
    
    public boolean isVisible() {
        return this.visible;
    }
    
    protected boolean isVisibleAccordingToBookData() {
        return this.arcaneBook.getData().isOpen(this.menu.getRecipeBookType());
    }
    
    protected void setVisible(boolean visible) {
        if (visible) {
            this.initVisuals();
        }
        
        this.visible = visible;
        this.arcaneBook.getData().setOpen(this.menu.getRecipeBookType(), visible);
        if (!visible) {
            this.recipeBookPage.setInvisible();
        }
        
        this.sendUpdateSettings();
    }
    
    public void slotClicked(@Nullable Slot slot) {
        if (slot != null && slot.index < this.menu.getSize()) {
            this.ghostRecipe.clear();
            if (this.isVisible()) {
                this.updateStackedContents();
            }
        }
    }
    
    protected void updateCollections(boolean resetPage) {
        ImmutableList.Builder<ArcaneRecipeCollection> builder = ImmutableList.builder();
        builder.addAll(this.arcaneBook.getCollection(this.selectedTab.getCategory()));
        builder.addAll(this.vanillaBook.getCollection(this.selectedTab.getCategory().getVanillaCategory()).stream().map(ArcaneRecipeCollection::new).collect(Collectors.toList()));
        List<ArcaneRecipeCollection> recipeCollections = builder.build();
        recipeCollections.forEach(arc -> {
            arc.canCraft(this.stackedContents, this.menu.getGridWidth(), this.menu.getGridHeight(), this.vanillaBook, this.arcaneBook.getData());
        });
        
        List<ArcaneRecipeCollection> filteredCollections = new ArrayList<>(recipeCollections);
        filteredCollections.removeIf(arc -> {
            return !arc.hasKnownRecipes();
        });
        filteredCollections.removeIf(arc -> {
            return !arc.hasFitting();
        });
        
        String searchStr = this.searchBox.getValue();
        if (!searchStr.isEmpty()) {
            ObjectSet<ArcaneRecipeCollection> vanillaObjectSet = new ObjectLinkedOpenHashSet<>(this.mc.getSearchTree(SearchRegistry.RECIPE_COLLECTIONS)
                    .search(searchStr.toLowerCase(Locale.ROOT)).stream().map(ArcaneRecipeCollection::new).collect(Collectors.toList()));
            ObjectSet<ArcaneRecipeCollection> arcaneObjectSet = new ObjectLinkedOpenHashSet<>(ArcaneSearchRegistry.getInstance().getSearchTree().search(searchStr.toLowerCase(Locale.ROOT)));
            filteredCollections.removeIf(arc -> {
                return !vanillaObjectSet.contains(arc) && !arcaneObjectSet.contains(arc);
            });
        }
        
        if (this.arcaneBook.getData().isFiltering(this.menu.getRecipeBookType())) {
            filteredCollections.removeIf(arc -> {
                return !arc.hasCraftable();
            });
        }
        
        this.recipeBookPage.updateCollections(filteredCollections, resetPage);
    }
    
    protected void updateTabs() {
        int xPos = (this.width - IMAGE_WIDTH) / 2 - this.xOffset - 30;
        int yPos = (this.height - IMAGE_HEIGHT) / 2 + 3;
        int tabCount = 0;
        
        for (ArcaneRecipeBookTabButton tab : this.tabButtons) {
            ArcaneRecipeBookCategories category = tab.getCategory();
            if (category != ArcaneRecipeBookCategories.CRAFTING_SEARCH) {
                if (tab.updateVisibility(this.vanillaBook, this.arcaneBook)) {
                    tab.setPosition(xPos, yPos + 27 * tabCount++);
                    tab.startAnimation(this.mc, this.vanillaBook, this.arcaneBook);
                }
            } else {
                tab.visible = true;
                tab.setPosition(xPos, yPos + 27 * tabCount++);
            }
        }
    }
    
    public void tick() {
        boolean flag = this.isVisibleAccordingToBookData();
        if (this.isVisible() != flag) {
            this.setVisible(flag);
        }
        
        if (this.isVisible()) {
            if (this.timesInventoryChanged != this.mc.player.getInventory().getTimesChanged()) {
                this.updateStackedContents();
                this.timesInventoryChanged = this.mc.player.getInventory().getTimesChanged();
            }
            this.searchBox.tick();
        }
    }
    
    protected void updateStackedContents() {
        this.stackedContents.clear();
        this.mc.player.getInventory().fillStackedContents(this.stackedContents);
        this.menu.fillCraftSlotsStackedContents(this.stackedContents);
        this.updateCollections(false);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        if (this.isVisible()) {
            poseStack.pushPose();
            poseStack.translate(0.0D, 0.0D, 100.0D);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, RECIPE_BOOK_LOCATION);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            int xPos = (this.width - 147) / 2 - this.xOffset;
            int yPos = (this.height - 166) / 2;
            this.blit(poseStack, xPos, yPos, 1, 1, IMAGE_WIDTH, IMAGE_HEIGHT);
            
            if (!this.searchBox.isFocused() && this.searchBox.getValue().isEmpty()) {
                drawString(poseStack, this.mc.font, SEARCH_HINT, xPos + 25, yPos + 14, -1);
            } else {
                this.searchBox.render(poseStack, mouseX, mouseY, partialTicks);
            }
            
            for (ArcaneRecipeBookTabButton tab : this.tabButtons) {
                tab.render(poseStack, mouseX, mouseY, partialTicks);
            }
            
            this.filterButton.render(poseStack, mouseX, mouseY, partialTicks);
            this.recipeBookPage.render(poseStack, xPos, yPos, mouseX, mouseY, partialTicks);
            poseStack.popPose();
        }
    }
    
    public void renderTooltip(PoseStack poseStack, int parentLeft, int parentTop, int mouseX, int mouseY) {
        if (this.isVisible()) {
            this.recipeBookPage.renderTooltip(poseStack, mouseX, mouseY);
            if (this.filterButton.isHoveredOrFocused() && this.mc.screen != null) {
                this.mc.screen.renderTooltip(poseStack, this.getFilterButtonTooltip(), mouseX, mouseY);
            }
            this.renderGhostRecipeTooltip(poseStack, parentLeft, parentTop, mouseX, mouseY);
        }
    }
    
    protected Component getFilterButtonTooltip() {
        return this.filterButton.isStateTriggered() ? this.getRecipeFilterName() : ALL_RECIPES_TOOLTIP;
    }
    
    protected Component getRecipeFilterName() {
        return ONLY_CRAFTABLES_TOOLTIP;
    }
    
    protected void renderGhostRecipeTooltip(PoseStack poseStack, int parentLeft, int parentTop, int mouseX, int mouseY) {
        ItemStack stack = null;
        for (int index = 0; index < this.ghostRecipe.size(); index++) {
            GhostRecipe.GhostIngredient ghostIngredient = this.ghostRecipe.get(index);
            int xPos = ghostIngredient.getX() + parentLeft;
            int yPos = ghostIngredient.getY() + parentTop;
            if (mouseX >= xPos && mouseY >= yPos && mouseX < xPos + 16 && mouseY < yPos + 16) {
                stack = ghostIngredient.getItem();
            }
        }
        if (stack != null && this.mc.screen != null) {
            this.mc.screen.renderComponentTooltip(poseStack, this.mc.screen.getTooltipFromItem(stack), mouseX, mouseY);
        }
    }
    
    public void renderGhostRecipe(PoseStack poseStack, int parentLeft, int parentTop, boolean largeSlot, float partialTicks) {
        this.ghostRecipe.render(poseStack, this.mc, parentLeft, parentTop, largeSlot, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int buttonIndex) {
        if (this.isVisible() && !this.mc.player.isSpectator()) {
            if (this.recipeBookPage.mouseClicked(mouseX, mouseY, buttonIndex, (this.width - IMAGE_WIDTH) / 2 - this.xOffset, (this.height - IMAGE_HEIGHT) / 2, IMAGE_WIDTH, IMAGE_HEIGHT)) {
                Recipe<?> recipe = this.recipeBookPage.getLastClickedRecipe();
                ArcaneRecipeCollection collection = this.recipeBookPage.getLastClickedRecipeCollection();
                if (recipe != null && collection != null) {
                    if (!collection.isCraftable(recipe) && this.ghostRecipe.getRecipe() == recipe) {
                        return false;
                    }
                    this.ghostRecipe.clear();
                    PacketHandler.sendToServer(new PlaceArcaneRecipePacket(this.mc.player.containerMenu.containerId, recipe, Screen.hasShiftDown()));
                    if (!this.isOffsetNextToMainGUI()) {
                        this.setVisible(false);
                    }
                }
                return true;
            } else if (this.searchBox.mouseClicked(mouseX, mouseY, buttonIndex)) {
                return true;
            } else if (this.filterButton.mouseClicked(mouseX, mouseY, buttonIndex)) {
                this.filterButton.setStateTriggered(this.toggleFiltering());
                this.sendUpdateSettings();
                this.updateCollections(false);
                return true;
            } else {
                for (ArcaneRecipeBookTabButton tab : this.tabButtons) {
                    if (tab.mouseClicked(mouseX, mouseY, buttonIndex)) {
                        if (this.selectedTab != tab) {
                            if (this.selectedTab != null) {
                                this.selectedTab.setStateTriggered(false);
                            }
                            this.selectedTab = tab;
                            this.selectedTab.setStateTriggered(true);
                            this.updateCollections(true);
                        }
                        return true;
                    }
                }
                return false;
            }
        } else {
            return false;
        }
    }
    
    protected boolean toggleFiltering() {
        ArcaneRecipeBookType type = this.menu.getRecipeBookType();
        boolean newValue = !this.arcaneBook.getData().isFiltering(type);
        this.arcaneBook.getData().setFiltering(type, newValue);
        return newValue;
    }
    
    public boolean hasClickedOutside(double mouseX, double mouseY, int parentLeft, int parentTop, int parentWidth, int parentHeight, int buttonIndex) {
        if (!this.isVisible()) {
            return true;
        } else {
            boolean flag = mouseX < (double)parentLeft || mouseY < (double)parentTop || mouseX >= (double)(parentLeft + parentWidth) || mouseY >= (double)(parentTop + parentHeight);
            boolean flag1 = (double)(parentLeft - IMAGE_WIDTH) < mouseX && mouseX < (double)parentLeft && (double)parentTop < mouseY && mouseY < (double)(parentTop + parentHeight);
            return flag && !flag1 && !this.selectedTab.isHoveredOrFocused();
        }
    }

    @Override
    public boolean keyPressed(int p_94745_, int p_94746_, int p_94747_) {
        this.ignoreTextInput = false;
        if (this.isVisible() && !this.mc.player.isSpectator()) {
            if (p_94745_ == 256 && !this.isOffsetNextToMainGUI()) {
                this.setVisible(false);
                return true;
            } else if (this.searchBox.keyPressed(p_94745_, p_94746_, p_94747_)) {
                this.checkSearchStringUpdate();
                return true;
            } else if (this.searchBox.isFocused() && this.searchBox.isVisible() && p_94745_ != 256) {
                return true;
            } else if (this.mc.options.keyChat.matches(p_94745_, p_94746_) && !this.searchBox.isFocused()) {
                this.ignoreTextInput = true;
                this.searchBox.setFocus(true);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean keyReleased(int p_94750_, int p_94751_, int p_94752_) {
        this.ignoreTextInput = false;
        return GuiEventListener.super.keyReleased(p_94750_, p_94751_, p_94752_);
    }

    @Override
    public boolean charTyped(char p_94732_, int p_94733_) {
        if (this.ignoreTextInput) {
            return false;
        } else if (this.isVisible() && !this.mc.player.isSpectator()) {
            if (this.searchBox.charTyped(p_94732_, p_94733_)) {
                this.checkSearchStringUpdate();
                return true;
            } else {
                return GuiEventListener.super.charTyped(p_94732_, p_94733_);
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean isMouseOver(double p_94748_, double p_94749_) {
        return false;
    }
    
    protected void checkSearchStringUpdate() {
        String str = this.searchBox.getValue().toLowerCase(Locale.ROOT);
        if (!str.equals(this.lastSearch)) {
            this.updateCollections(false);
            this.lastSearch = str;
        }
    }
    
    protected boolean isOffsetNextToMainGUI() {
        return this.xOffset == OFFSET_X_POSITION;
    }
    
    public void recipesUpdated() {
        this.updateTabs();
        if (this.isVisible()) {
            this.updateCollections(false);
        }
    }

    @Override
    public void recipesShown(List<Recipe<?>> recipes) {
        for (Recipe<?> recipe : recipes) {
            this.mc.player.removeRecipeHighlight(recipe);
            if (this.arcaneBook.getData().willHighlight(recipe)) {
                this.arcaneBook.getData().removeHighlight(recipe);
                PacketHandler.sendToServer(new SeenArcaneRecipePacket(recipe));
            }
        }
    }
    
    public void setupGhostRecipe(Recipe<?> recipe, List<Slot> slots) {
        ItemStack stack = recipe.getResultItem();
        this.ghostRecipe.setRecipe(recipe);
        this.ghostRecipe.addIngredient(Ingredient.of(stack), (slots.get(0)).x, (slots.get(0)).y);
        this.placeRecipe(this.menu.getGridWidth(), this.menu.getGridHeight(), this.menu.getResultSlotIndex(), recipe, recipe.getIngredients().iterator(), 0);
    }

    @Override
    public void addItemToSlot(Iterator<Ingredient> iterator, int slotIndex, int count, int p_135418_, int p_135419_) {
        Ingredient ingredient = iterator.next();
        if (!ingredient.isEmpty()) {
            Slot slot = this.menu.slots.get(slotIndex);
            this.ghostRecipe.addIngredient(ingredient, slot.x, slot.y);
        }
    }
    
    protected void sendUpdateSettings() {
        ArcaneRecipeBookType type = this.menu.getRecipeBookType();
        boolean open = this.arcaneBook.getData().getBookSettings().isOpen(type);
        boolean filtering = this.arcaneBook.getData().getBookSettings().isFiltering(type);
        PacketHandler.sendToServer(new ChangeArcaneRecipeBookSettingsPacket(type, open, filtering));
    }

    @Override
    public NarrationPriority narrationPriority() {
        return this.visible ? NarratableEntry.NarrationPriority.HOVERED : NarratableEntry.NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(NarrationElementOutput output) {
        List<NarratableEntry> entryList = new ArrayList<>();
        this.recipeBookPage.listButtons(widget -> {
            if (widget.isActive()) {
                entryList.add(widget);
            }
        });
        entryList.add(this.searchBox);
        entryList.add(this.filterButton);
        entryList.addAll(this.tabButtons);
        Screen.NarratableSearchResult result = Screen.findNarratableWidget(entryList, null);
        if (result != null) {
            result.entry.updateNarration(output.nest());
        }
    }
}
