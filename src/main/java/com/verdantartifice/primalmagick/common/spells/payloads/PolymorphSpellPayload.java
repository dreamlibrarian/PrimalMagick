package com.verdantartifice.primalmagick.common.spells.payloads;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.verdantartifice.primalmagick.common.misc.EntitySwapper;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.spells.SpellManager;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Definition of a polymorph spell.  Temporarily replaces the target living, non-player, non-boss
 * entity with a neutral wolf.  The length of the replacement scales with the payload's duration
 * property.  NBT data of the original entity is preserved for the swap back.  Has no effect on
 * blocks.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagick.common.misc.EntitySwapper}
 */
public class PolymorphSpellPayload extends AbstractSpellPayload {
    public static final String TYPE = "polymorph";
    protected static final CompoundResearchKey RESEARCH = CompoundResearchKey.from(SimpleResearchKey.parse("SPELL_PAYLOAD_POLYMORPH"));

    public PolymorphSpellPayload() {
        super();
    }
    
    public PolymorphSpellPayload(int duration) {
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
            if (SpellManager.canPolymorph(entityTarget.getEntity().getType())) {
                // Create and enqueue an entity swapper for the target entity
                UUID entityId = entityTarget.getEntity().getUUID();
                CompoundTag originalData = entityTarget.getEntity().saveWithoutId(new CompoundTag());
                int ticks = 20 * this.getDurationSeconds(spell, spellSource);
                EntitySwapper.enqueue(world, new EntitySwapper(entityId, EntityType.WOLF, originalData, Optional.of(Integer.valueOf(ticks)), 0));
            }
        }
    }
    
    @Override
    public Source getSource() {
        return Source.MOON;
    }

    @Override
    public int getBaseManaCost() {
        return 5 * this.getPropertyValue("duration");
    }

    @Override
    public void playSounds(Level world, BlockPos origin) {
        world.playSound(null, origin, SoundEvents.WOLF_AMBIENT, SoundSource.PLAYERS, 1.0F, 1.0F + (float)(world.random.nextGaussian() * 0.05D));
    }

    @Override
    protected String getPayloadType() {
        return TYPE;
    }
    
    protected int getDurationSeconds(SpellPackage spell, ItemStack spellSource) {
        return 6 * this.getModdedPropertyValue("duration", spell, spellSource);
    }

    @Override
    public Component getDetailTooltip(SpellPackage spell, ItemStack spellSource) {
        return new TranslatableComponent("primalmagick.spell.payload.detail_tooltip." + this.getPayloadType(), DECIMAL_FORMATTER.format(this.getDurationSeconds(spell, spellSource)));
    }
}
