package com.verdantartifice.primalmagick.common.spells.payloads;

import java.util.Map;

import com.verdantartifice.primalmagick.common.effects.EffectsPM;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Definition of a flight spell.  This payload grants the target a short-lived potion effect which
 * grants creative flight.  The length of the effect scales with the duration property of the
 * payload.  Has no effect on blocks.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagick.common.effects.FlyingEffect}
 */
public class FlightSpellPayload extends AbstractSpellPayload {
    public static final String TYPE = "flight";
    protected static final CompoundResearchKey RESEARCH = CompoundResearchKey.from(SimpleResearchKey.parse("SPELL_PAYLOAD_FLIGHT"));

    public FlightSpellPayload() {
        super();
    }
    
    public FlightSpellPayload(int duration) {
        super();
        this.getProperty("duration").setValue(duration);
    }
    
    public static CompoundResearchKey getResearch() {
        return RESEARCH;
    }
    
    @Override
    protected Map<String, SpellProperty> initProperties() {
        Map<String, SpellProperty> propMap = super.initProperties();
        propMap.put("duration", new SpellProperty("duration", "primalmagick.spell.property.duration", 1, 5));
        return propMap;
    }
    
    @Override
    public void execute(HitResult target, Vec3 burstPoint, SpellPackage spell, Level world, LivingEntity caster, ItemStack spellSource, Entity projectileEntity) {
        if (target != null && target.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityTarget = (EntityHitResult)target;
            if (entityTarget.getEntity() instanceof LivingEntity) {
                // Grant the potion effect
                LivingEntity entity = (LivingEntity)entityTarget.getEntity();
                int ticks = 20 * this.getDurationSeconds(spell, spellSource);
                entity.addEffect(new MobEffectInstance(EffectsPM.FLYING.get(), ticks));
            }
        }
    }

    @Override
    public Source getSource() {
        return Source.SKY;
    }

    @Override
    public int getBaseManaCost() {
        return 20 * this.getPropertyValue("duration");
    }

    @Override
    public void playSounds(Level world, BlockPos origin) {
        world.playSound(null, origin, SoundsPM.WINGFLAP.get(), SoundSource.PLAYERS, 1.0F, 1.0F + (float)(world.random.nextGaussian() * 0.05D));
    }

    @Override
    protected String getPayloadType() {
        return TYPE;
    }
    
    protected int getDurationSeconds(SpellPackage spell, ItemStack spellSource) {
        return 3 * this.getModdedPropertyValue("duration", spell, spellSource);
    }

    @Override
    public Component getDetailTooltip(SpellPackage spell, ItemStack spellSource) {
        return new TranslatableComponent("primalmagick.spell.payload.detail_tooltip." + this.getPayloadType(), DECIMAL_FORMATTER.format(this.getDurationSeconds(spell, spellSource)));
    }
}
