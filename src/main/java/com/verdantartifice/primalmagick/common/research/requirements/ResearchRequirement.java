package com.verdantartifice.primalmagick.common.research.requirements;

import java.util.stream.Stream;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.research.keys.AbstractResearchKey;

import net.minecraft.world.entity.player.Player;

/**
 * Requirement that the player has completed a given research entry.
 * 
 * @author Daedalus4096
 */
public class ResearchRequirement extends AbstractRequirement {
    public static final Codec<ResearchRequirement> CODEC = AbstractResearchKey.CODEC.fieldOf("rootKey").xmap(ResearchRequirement::new, req -> req.rootKey).codec();
    
    protected final AbstractResearchKey<?> rootKey;
    
    public ResearchRequirement(AbstractResearchKey<?> rootKey) {
        this.rootKey = Preconditions.checkNotNull(rootKey);
    }

    @Override
    public boolean isMetBy(Player player) {
        return player == null ? false : this.rootKey.isKnownBy(player);
    }

    @Override
    public void consumeComponents(Player player) {
        // No action needed; research is never consumed
    }

    @Override
    public RequirementCategory getCategory() {
        return this.rootKey.getRequirementCategory();
    }

    @Override
    public Stream<AbstractRequirement> streamByCategory(RequirementCategory category) {
        return category == this.getCategory() ? Stream.of(this) : Stream.empty();
    }

    @Override
    protected RequirementType<?> getType() {
        return RequirementsPM.RESEARCH.get();
    }
}
