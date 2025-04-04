package com.verdantartifice.primalmagick.client.renderers.models;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

/**
 * Definition of registered model layer locations.
 * 
 * @author Daedalus4096
 */
public class ModelLayersPM {
    public static final ModelLayerLocation FLYING_CARPET = new ModelLayerLocation(new ResourceLocation(PrimalMagick.MODID, "flying_carpet"), "main");
    public static final ModelLayerLocation PIXIE_BASIC = new ModelLayerLocation(new ResourceLocation(PrimalMagick.MODID, "pixie_basic"), "main");
    public static final ModelLayerLocation PIXIE_ROYAL = new ModelLayerLocation(new ResourceLocation(PrimalMagick.MODID, "pixie_royal"), "main");
    public static final ModelLayerLocation SPELL_MINE = new ModelLayerLocation(new ResourceLocation(PrimalMagick.MODID, "spell_mine"), "main");
    public static final ModelLayerLocation SPELL_PROJECTILE = new ModelLayerLocation(new ResourceLocation(PrimalMagick.MODID, "spell_projectile"), "main");
    public static final ModelLayerLocation TREEFOLK = new ModelLayerLocation(new ResourceLocation(PrimalMagick.MODID, "treefolk"), "main");
}
