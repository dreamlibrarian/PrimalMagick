package com.verdantartifice.primalmagick.common.spells.payloads;

import java.util.Map;

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
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Definition of a solar damage spell.  Does standard damage to the target.  As a secondary effect, sets
 * undead targets on fire.  The length of the fire effect scales with the duration property of the payload.
 * 
 * @author Daedalus4096
 */
public class SolarDamageSpellPayload extends AbstractDamageSpellPayload {
    public static final String TYPE = "solar_damage";
    protected static final CompoundResearchKey RESEARCH = CompoundResearchKey.from(SimpleResearchKey.parse("SPELL_PAYLOAD_SOLAR"));

    public SolarDamageSpellPayload() {
        super();
    }
    
    public SolarDamageSpellPayload(int power, int duration) {
        super(power);
        this.getProperty("duration").setValue(duration);
    }
    
    public static CompoundResearchKey getResearch() {
        return RESEARCH;
    }
    
    @Override
    protected Map<String, SpellProperty> initProperties() {
        Map<String, SpellProperty> propMap = super.initProperties();
        propMap.put("duration", new SpellProperty("duration", "primalmagick.spell.property.duration", 0, 5));
        return propMap;
    }

    @Override
    public Source getSource() {
        return Source.SUN;
    }

    @Override
    public void playSounds(Level world, BlockPos origin) {
        world.playSound(null, origin, SoundsPM.SUNBEAM.get(), SoundSource.PLAYERS, 1.0F, 1.0F + (float)(world.random.nextGaussian() * 0.05D));
    }

    @Override
    protected String getPayloadType() {
        return TYPE;
    }

    @Override
    protected void applySecondaryEffects(HitResult target, Vec3 burstPoint, SpellPackage spell, Level world, LivingEntity caster, ItemStack spellSource) {
        int duration = this.getDurationSeconds(spell, spellSource);
        if (target != null && target.getType() == HitResult.Type.ENTITY && duration > 0) {
            EntityHitResult entityTarget = (EntityHitResult)target;
            if (entityTarget.getEntity() != null && entityTarget.getEntity() instanceof LivingEntity) {
                // Make the entity glow and set it on fire if undead
                LivingEntity entity = (LivingEntity)entityTarget.getEntity();
                entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 20 * duration, 0));
                if (entity.isInvertedHealAndHarm()) {
                    entity.setSecondsOnFire(duration);
                }
            }
        }
    }
    
    @Override
    public int getBaseManaCost() {
        int power = this.getPropertyValue("power");
        int duration = this.getPropertyValue("duration");
        return (1 << Math.max(0, power - 1)) + (duration == 0 ? 0 : (1 << Math.max(0, duration - 1)) >> 1);
    }

    protected int getDurationSeconds(SpellPackage spell, ItemStack spellSource) {
        return 2 * this.getModdedPropertyValue("duration", spell, spellSource);
    }

    @Override
    public Component getDetailTooltip(SpellPackage spell, ItemStack spellSource) {
        return new TranslatableComponent("primalmagick.spell.payload.detail_tooltip." + this.getPayloadType(), DECIMAL_FORMATTER.format(this.getBaseDamage(spell, spellSource)),
                DECIMAL_FORMATTER.format(this.getDurationSeconds(spell, spellSource)));
    }
}
