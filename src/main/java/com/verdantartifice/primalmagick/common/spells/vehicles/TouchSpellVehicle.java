package com.verdantartifice.primalmagick.common.spells.vehicles;

import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.ForgeMod;

/**
 * Definition of a touch spell vehicle.  This vehicle selects the closest target within the caster's
 * reach along their line of sight.
 * 
 * @author Daedalus4096
 */
public class TouchSpellVehicle extends AbstractRaycastSpellVehicle {
    public static final String TYPE = "touch";
    protected static final CompoundResearchKey RESEARCH = CompoundResearchKey.from(SimpleResearchKey.parse("BASIC_SORCERY"));
    
    public static CompoundResearchKey getResearch() {
        return RESEARCH;
    }

    @Override
    protected String getVehicleType() {
        return TYPE;
    }
    
    @Override
    protected double getReachDistance(LivingEntity caster) {
        return caster.getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue();
    }

    @Override
    public boolean isIndirect() {
        return false;
    }
}
