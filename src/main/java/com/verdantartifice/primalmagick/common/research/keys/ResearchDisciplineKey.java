package com.verdantartifice.primalmagick.common.research.keys;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.mutable.MutableBoolean;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagick.common.research.requirements.RequirementCategory;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;

public class ResearchDisciplineKey extends AbstractResearchKey<ResearchDisciplineKey> {
    public static final Codec<ResearchDisciplineKey> CODEC = ResourceKey.codec(RegistryKeysPM.RESEARCH_DISCIPLINES).fieldOf("rootKey").xmap(ResearchDisciplineKey::new, key -> key.rootKey).codec();
    
    protected final ResourceKey<ResearchDiscipline> rootKey;
    
    public ResearchDisciplineKey(ResourceKey<ResearchDiscipline> rootKey) {
        this.rootKey = rootKey;
    }
    
    public ResourceKey<ResearchDiscipline> getRootKey() {
        return this.rootKey;
    }

    @Override
    public String toString() {
        return this.rootKey.location().toString();
    }

    @Override
    public RequirementCategory getRequirementCategory() {
        return RequirementCategory.RESEARCH;
    }

    @Override
    protected ResearchKeyType<ResearchDisciplineKey> getType() {
        return ResearchKeyTypesPM.RESEARCH_DISCIPLINE.get();
    }

    @Override
    public boolean isKnownBy(Player player) {
        if (player == null) {
            return false;
        }
        RegistryAccess registryAccess = player.level().registryAccess();
        Holder.Reference<ResearchDiscipline> discipline = registryAccess.registryOrThrow(RegistryKeysPM.RESEARCH_DISCIPLINES).getHolderOrThrow(this.rootKey);
        MutableBoolean retVal = new MutableBoolean(false);
        discipline.get().unlockRequirementOpt().ifPresentOrElse(req -> {
            // If the discipline does have an unlock requirement, then the discipline is only known if that requirement is met
            retVal.setValue(req.isMetBy(player));
        }, () -> {
            // If the discipline has no unlock requirement, then it's known to the player
            retVal.setTrue();
        });
        return retVal.booleanValue();
    }
    
    @Nonnull
    public static ResearchDisciplineKey fromNetwork(FriendlyByteBuf buf) {
        return (ResearchDisciplineKey)AbstractResearchKey.fromNetwork(buf);
    }
    
    @Nonnull
    static ResearchDisciplineKey fromNetworkInner(FriendlyByteBuf buf) {
        return new ResearchDisciplineKey(buf.readResourceKey(RegistryKeysPM.RESEARCH_DISCIPLINES));
    }

    @Override
    protected void toNetworkInner(FriendlyByteBuf buf) {
        buf.writeResourceKey(this.rootKey);
    }
}
