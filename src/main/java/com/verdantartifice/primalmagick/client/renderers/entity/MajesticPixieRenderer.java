package com.verdantartifice.primalmagick.client.renderers.entity;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.renderers.entity.model.PixieModel;
import com.verdantartifice.primalmagick.client.renderers.models.ModelLayersPM;
import com.verdantartifice.primalmagick.common.entities.companions.pixies.AbstractPixieEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

/**
 * Entity renderer for a majestic pixie.
 * 
 * @author Daedalus4096
 */
public class MajesticPixieRenderer extends AbstractPixieRenderer {
    private static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagick.MODID, "textures/entity/pixie/majestic_pixie.png");
    
    public MajesticPixieRenderer(EntityRendererProvider.Context context) {
        super(context, new PixieModel(context.bakeLayer(ModelLayersPM.PIXIE_ROYAL)));
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractPixieEntity entity) {
        return TEXTURE;
    }
}
