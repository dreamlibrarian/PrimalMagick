package com.verdantartifice.primalmagick.client.events;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.config.KeyBindings;
import com.verdantartifice.primalmagick.client.gui.AnalysisTableScreen;
import com.verdantartifice.primalmagick.client.gui.ArcaneWorkbenchScreen;
import com.verdantartifice.primalmagick.client.gui.CalcinatorScreen;
import com.verdantartifice.primalmagick.client.gui.ConcocterScreen;
import com.verdantartifice.primalmagick.client.gui.DissolutionChamberScreen;
import com.verdantartifice.primalmagick.client.gui.EssenceTransmuterScreen;
import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.client.gui.HoneyExtractorScreen;
import com.verdantartifice.primalmagick.client.gui.ResearchTableScreen;
import com.verdantartifice.primalmagick.client.gui.RunecarvingTableScreen;
import com.verdantartifice.primalmagick.client.gui.RunescribingAltarBasicScreen;
import com.verdantartifice.primalmagick.client.gui.RunescribingAltarEnchantedScreen;
import com.verdantartifice.primalmagick.client.gui.RunescribingAltarForbiddenScreen;
import com.verdantartifice.primalmagick.client.gui.RunescribingAltarHeavenlyScreen;
import com.verdantartifice.primalmagick.client.gui.SpellcraftingAltarScreen;
import com.verdantartifice.primalmagick.client.gui.WandAssemblyTableScreen;
import com.verdantartifice.primalmagick.client.gui.WandChargerScreen;
import com.verdantartifice.primalmagick.client.gui.WandInscriptionTableScreen;
import com.verdantartifice.primalmagick.client.renderers.tile.AutoChargerTER;
import com.verdantartifice.primalmagick.client.renderers.tile.ManaFontTER;
import com.verdantartifice.primalmagick.client.renderers.tile.OfferingPedestalTER;
import com.verdantartifice.primalmagick.client.renderers.tile.RitualAltarTER;
import com.verdantartifice.primalmagick.client.renderers.tile.RitualBellTER;
import com.verdantartifice.primalmagick.client.renderers.tile.RitualLecternTER;
import com.verdantartifice.primalmagick.client.renderers.tile.RunescribingAltarTER;
import com.verdantartifice.primalmagick.client.renderers.tile.SanguineCrucibleTER;
import com.verdantartifice.primalmagick.client.renderers.tile.WandChargerTER;
import com.verdantartifice.primalmagick.client.tooltips.ClientAffinityTooltipComponent;
import com.verdantartifice.primalmagick.common.affinities.AffinityTooltipComponent;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.containers.ContainersPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.entities.FlyingCarpetItem;
import com.verdantartifice.primalmagick.common.items.misc.ArcanometerItem;
import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagick.common.util.RayTraceUtils;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Respond to client-only Forge mod lifecycle events for setup.
 * 
 * @author Daedalus4096
 */
@SuppressWarnings("deprecation")
@Mod.EventBusSubscriber(modid=PrimalMagick.MODID, value=Dist.CLIENT, bus=Mod.EventBusSubscriber.Bus.MOD)
public class ClientModLifecycleEvents {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        registerKeybinds();
        registerScreens();
        registerTERs();
        registerItemProperties(event);
        setRenderLayers();
        registerTooltipComponentFactories();
    }

    private static void registerKeybinds() {
        KeyBindings.init();
    }
    
    private static void registerScreens() {
        // Register screen factories for each container
        MenuScreens.register(ContainersPM.GRIMOIRE.get(), GrimoireScreen::new);
        MenuScreens.register(ContainersPM.ARCANE_WORKBENCH.get(), ArcaneWorkbenchScreen::new);
        MenuScreens.register(ContainersPM.WAND_ASSEMBLY_TABLE.get(), WandAssemblyTableScreen::new);
        MenuScreens.register(ContainersPM.ANALYSIS_TABLE.get(), AnalysisTableScreen::new);
        MenuScreens.register(ContainersPM.CALCINATOR.get(), CalcinatorScreen::new);
        MenuScreens.register(ContainersPM.WAND_INSCRIPTION_TABLE.get(), WandInscriptionTableScreen::new);
        MenuScreens.register(ContainersPM.SPELLCRAFTING_ALTAR.get(), SpellcraftingAltarScreen::new);
        MenuScreens.register(ContainersPM.WAND_CHARGER.get(), WandChargerScreen::new);
        MenuScreens.register(ContainersPM.RESEARCH_TABLE.get(), ResearchTableScreen::new);
        MenuScreens.register(ContainersPM.RUNESCRIBING_ALTAR_BASIC.get(), RunescribingAltarBasicScreen::new);
        MenuScreens.register(ContainersPM.RUNESCRIBING_ALTAR_ENCHANTED.get(), RunescribingAltarEnchantedScreen::new);
        MenuScreens.register(ContainersPM.RUNESCRIBING_ALTAR_FORBIDDEN.get(), RunescribingAltarForbiddenScreen::new);
        MenuScreens.register(ContainersPM.RUNESCRIBING_ALTAR_HEAVENLY.get(), RunescribingAltarHeavenlyScreen::new);
        MenuScreens.register(ContainersPM.RUNECARVING_TABLE.get(), RunecarvingTableScreen::new);
        MenuScreens.register(ContainersPM.HONEY_EXTRACTOR.get(), HoneyExtractorScreen::new);
        MenuScreens.register(ContainersPM.CONCOCTER.get(), ConcocterScreen::new);
        MenuScreens.register(ContainersPM.ESSENCE_TRANSMUTER.get(), EssenceTransmuterScreen::new);
        MenuScreens.register(ContainersPM.DISSOLUTION_CHAMBER.get(), DissolutionChamberScreen::new);
    }
    
    private static void registerTERs() {
        // Register tile entity renderers for those tile entities that need them
        BlockEntityRenderers.register(TileEntityTypesPM.ANCIENT_MANA_FONT.get(), ManaFontTER::new);
        BlockEntityRenderers.register(TileEntityTypesPM.ARTIFICIAL_MANA_FONT.get(), ManaFontTER::new);
        BlockEntityRenderers.register(TileEntityTypesPM.WAND_CHARGER.get(), WandChargerTER::new);
        BlockEntityRenderers.register(TileEntityTypesPM.RITUAL_ALTAR.get(), RitualAltarTER::new);
        BlockEntityRenderers.register(TileEntityTypesPM.OFFERING_PEDESTAL.get(), OfferingPedestalTER::new);
        BlockEntityRenderers.register(TileEntityTypesPM.RITUAL_LECTERN.get(), RitualLecternTER::new);
        BlockEntityRenderers.register(TileEntityTypesPM.RITUAL_BELL.get(), RitualBellTER::new);
        BlockEntityRenderers.register(TileEntityTypesPM.RUNESCRIBING_ALTAR.get(), RunescribingAltarTER::new);
        BlockEntityRenderers.register(TileEntityTypesPM.SANGUINE_CRUCIBLE.get(), SanguineCrucibleTER::new);
        BlockEntityRenderers.register(TileEntityTypesPM.AUTO_CHARGER.get(), AutoChargerTER::new);
    }
    
    private static void registerItemProperties(FMLClientSetupEvent event) {
        // Register properties for items on the main thread in a thread-safe fashion
        event.enqueueWork(() -> {
            ItemProperties.register(ItemsPM.ARCANOMETER.get(), ArcanometerItem.SCAN_STATE_PROPERTY, new ItemPropertyFunction() {
                protected float scanState = 0;

                @Override
                public float call(ItemStack stack, ClientLevel world, LivingEntity entity, int seed) {
                    if (entity == null || !(entity instanceof Player)) {
                        return 0.0F;
                    } else {
                        // If the currently moused-over block/item has not yet been scanned, raise the antennae
                        if (ArcanometerItem.isMouseOverScannable(RayTraceUtils.getMouseOver(world), world, (Player)entity)) {
                            this.incrementScanState();
                        } else {
                            this.decrementScanState();
                        }
                        return scanState;
                    }
                }
                
                protected void incrementScanState() {
                    this.scanState = Math.min(4.0F, this.scanState + 0.25F);
                }
                
                protected void decrementScanState() {
                    this.scanState = Math.max(0.0F, this.scanState - 0.25F);
                }
            });
            
            ItemProperties.register(ItemsPM.FLYING_CARPET.get(), FlyingCarpetItem.COLOR_PROPERTY, (ItemStack stack, ClientLevel world, LivingEntity entity, int unknown) -> {
                DyeColor color = null;
                if (stack != null && stack.getItem() instanceof FlyingCarpetItem) {
                    color = ((FlyingCarpetItem)stack.getItem()).getDyeColor(stack);
                }
                if (color == null) {
                    // Default to white if no dye color is applied
                    color = DyeColor.WHITE;
                }
                return ((float)color.getId() / 16.0F);
            });
            
            ItemPropertyFunction castProperty = (ItemStack stack, ClientLevel world, LivingEntity entity, int seed) -> {
                if (entity == null) {
                    return 0.0F;
                } else {
                    boolean inMain = entity.getMainHandItem() == stack;
                    boolean inOff = entity.getOffhandItem() == stack;
                    if (entity.getMainHandItem().getItem() instanceof FishingRodItem) {
                        inOff = false;
                    }
                    return (inMain || inOff) && entity instanceof Player && ((Player)entity).fishing != null ? 1.0F : 0.0F;
                }
            };
            ItemProperties.register(ItemsPM.PRIMALITE_FISHING_ROD.get(), new ResourceLocation("cast"), castProperty);
            ItemProperties.register(ItemsPM.HEXIUM_FISHING_ROD.get(), new ResourceLocation("cast"), castProperty);
            ItemProperties.register(ItemsPM.HALLOWSTEEL_FISHING_ROD.get(), new ResourceLocation("cast"), castProperty);
            ItemProperties.register(ItemsPM.PRIMAL_FISHING_ROD.get(), new ResourceLocation("cast"), castProperty);
            
            ItemPropertyFunction handActiveProperty = (ItemStack stack, ClientLevel world, LivingEntity entity, int seed) -> {
                return entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
            };
            ItemProperties.register(ItemsPM.PRIMALITE_TRIDENT.get(), new ResourceLocation("throwing"), handActiveProperty);
            ItemProperties.register(ItemsPM.HEXIUM_TRIDENT.get(), new ResourceLocation("throwing"), handActiveProperty);
            ItemProperties.register(ItemsPM.HALLOWSTEEL_TRIDENT.get(), new ResourceLocation("throwing"), handActiveProperty);
            ItemProperties.register(ItemsPM.FORBIDDEN_TRIDENT.get(), new ResourceLocation("throwing"), handActiveProperty);
            ItemProperties.register(ItemsPM.PRIMALITE_SHIELD.get(), new ResourceLocation("blocking"), handActiveProperty);
            ItemProperties.register(ItemsPM.HEXIUM_SHIELD.get(), new ResourceLocation("blocking"), handActiveProperty);
            ItemProperties.register(ItemsPM.HALLOWSTEEL_SHIELD.get(), new ResourceLocation("blocking"), handActiveProperty);
            ItemProperties.register(ItemsPM.SACRED_SHIELD.get(), new ResourceLocation("blocking"), handActiveProperty);
            
            ItemPropertyFunction pullProperty = (ItemStack stack, ClientLevel world, LivingEntity entity, int seed) -> {
                if (entity == null) {
                    return 0.0F;
                } else {
                    return entity.getUseItem() != stack ? 0.0F : (float)(stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F;
                }
            };
            ItemProperties.register(ItemsPM.PRIMALITE_BOW.get(), new ResourceLocation("pull"), pullProperty);
            ItemProperties.register(ItemsPM.PRIMALITE_BOW.get(), new ResourceLocation("pulling"), handActiveProperty);
            ItemProperties.register(ItemsPM.HEXIUM_BOW.get(), new ResourceLocation("pull"), pullProperty);
            ItemProperties.register(ItemsPM.HEXIUM_BOW.get(), new ResourceLocation("pulling"), handActiveProperty);
            ItemProperties.register(ItemsPM.HALLOWSTEEL_BOW.get(), new ResourceLocation("pull"), pullProperty);
            ItemProperties.register(ItemsPM.HALLOWSTEEL_BOW.get(), new ResourceLocation("pulling"), handActiveProperty);
            ItemProperties.register(ItemsPM.FORBIDDEN_BOW.get(), new ResourceLocation("pull"), pullProperty);
            ItemProperties.register(ItemsPM.FORBIDDEN_BOW.get(), new ResourceLocation("pulling"), handActiveProperty);
        });
    }
    
    private static void setRenderLayers() {
        // Set the render layers for any blocks that don't use the default
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.MOONWOOD_SAPLING.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.MOONWOOD_LEAVES.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.MOONWOOD_LOG.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.MOONWOOD_PILLAR.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.MOONWOOD_PLANKS.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.MOONWOOD_SLAB.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.MOONWOOD_STAIRS.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.MOONWOOD_WOOD.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STRIPPED_MOONWOOD_LOG.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STRIPPED_MOONWOOD_WOOD.get(), RenderType.translucent());

        ItemBlockRenderTypes.setRenderLayer(BlocksPM.SUNWOOD_SAPLING.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.SUNWOOD_LEAVES.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.SUNWOOD_LOG.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.SUNWOOD_PILLAR.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.SUNWOOD_PLANKS.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.SUNWOOD_SLAB.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.SUNWOOD_STAIRS.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.SUNWOOD_WOOD.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STRIPPED_SUNWOOD_LOG.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STRIPPED_SUNWOOD_WOOD.get(), RenderType.translucent());
        
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.HALLOWOOD_SAPLING.get(), RenderType.cutout());
        
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.SALT_TRAIL.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.SUNLAMP.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.SPIRIT_LANTERN.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.BLOODLETTER.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.CONCOCTER.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.CELESTIAL_HARP.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.HONEY_EXTRACTOR.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.RUNECARVING_TABLE.get(), RenderType.cutout());

        ItemBlockRenderTypes.setRenderLayer(BlocksPM.SKYGLASS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_BLACK.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_BLUE.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_BROWN.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_CYAN.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_GRAY.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_GREEN.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_LIGHT_BLUE.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_LIGHT_GRAY.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_LIME.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_MAGENTA.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_ORANGE.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PINK.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PURPLE.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_RED.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_WHITE.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_YELLOW.get(), RenderType.translucent());
        
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.SKYGLASS_PANE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_BLACK.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_BLUE.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_BROWN.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_CYAN.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_GRAY.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_GREEN.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_LIGHT_BLUE.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_LIGHT_GRAY.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_LIME.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_MAGENTA.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_ORANGE.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_PINK.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_PURPLE.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_RED.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_WHITE.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_YELLOW.get(), RenderType.translucent());
    }

    private static void registerTooltipComponentFactories() {
        MinecraftForgeClient.registerTooltipComponentFactory(AffinityTooltipComponent.class, ClientAffinityTooltipComponent::new);
    }
}
