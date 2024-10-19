package com.verdantartifice.primalmagick.common.network;

import com.verdantartifice.primalmagick.common.network.packets.config.AcknowledgementPacket;
import com.verdantartifice.primalmagick.common.network.packets.config.UpdateAffinitiesConfigPacket;
import com.verdantartifice.primalmagick.common.network.packets.config.UpdateLinguisticsGridsConfigPacket;
import com.verdantartifice.primalmagick.common.network.packets.data.ContainerSetVarintDataPacket;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncArcaneRecipeBookPacket;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncAttunementsPacket;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncCompanionsPacket;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncCooldownsPacket;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncKnowledgePacket;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncLinguisticsPacket;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncProgressPacket;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncStatsPacket;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncWardPacket;
import com.verdantartifice.primalmagick.common.network.packets.data.TileToClientPacket;
import com.verdantartifice.primalmagick.common.network.packets.data.UpdateAffinitiesPacket;
import com.verdantartifice.primalmagick.common.network.packets.data.UpdateLinguisticsGridsPacket;
import com.verdantartifice.primalmagick.common.network.packets.fx.ManaSparklePacket;
import com.verdantartifice.primalmagick.common.network.packets.fx.OfferingChannelPacket;
import com.verdantartifice.primalmagick.common.network.packets.fx.PlayClientSoundPacket;
import com.verdantartifice.primalmagick.common.network.packets.fx.PotionExplosionPacket;
import com.verdantartifice.primalmagick.common.network.packets.fx.PropMarkerPacket;
import com.verdantartifice.primalmagick.common.network.packets.fx.RemovePropMarkerPacket;
import com.verdantartifice.primalmagick.common.network.packets.fx.SpellBoltPacket;
import com.verdantartifice.primalmagick.common.network.packets.fx.SpellImpactPacket;
import com.verdantartifice.primalmagick.common.network.packets.fx.SpellTrailPacket;
import com.verdantartifice.primalmagick.common.network.packets.fx.SpellcraftingRunePacket;
import com.verdantartifice.primalmagick.common.network.packets.fx.TeleportArrivalPacket;
import com.verdantartifice.primalmagick.common.network.packets.fx.WandPoofPacket;
import com.verdantartifice.primalmagick.common.network.packets.misc.OpenEnchantedBookScreenPacket;
import com.verdantartifice.primalmagick.common.network.packets.misc.OpenGrimoireScreenPacket;
import com.verdantartifice.primalmagick.common.network.packets.misc.OpenStaticBookScreenPacket;
import com.verdantartifice.primalmagick.common.network.packets.recipe_book.PlaceGhostArcaneRecipePacket;
import commonnetwork.api.Network;

public class PacketRegistration {
    public static void registerMessages() {
        Network
                // Client-bound configuration channel packets
                .registerConfigurationPacket(UpdateAffinitiesConfigPacket.type(), UpdateAffinitiesConfigPacket.class, UpdateAffinitiesConfigPacket.STREAM_CODEC, UpdateAffinitiesConfigPacket::onMessage)
                .registerConfigurationPacket(UpdateLinguisticsGridsConfigPacket.type(), UpdateLinguisticsGridsConfigPacket.class, UpdateLinguisticsGridsConfigPacket.STREAM_CODEC, UpdateLinguisticsGridsConfigPacket::onMessage)
                // Server-bound configuration channel packets
                .registerConfigurationPacket(AcknowledgementPacket.type(), AcknowledgementPacket.class, AcknowledgementPacket.STREAM_CODEC, AcknowledgementPacket::onMessage)
                // Client-bound play channel packets
                .registerPacket(SyncKnowledgePacket.type(), SyncKnowledgePacket.class, SyncKnowledgePacket.STREAM_CODEC, SyncKnowledgePacket::onMessage)
                .registerPacket(WandPoofPacket.type(), WandPoofPacket.class, WandPoofPacket.STREAM_CODEC, WandPoofPacket::onMessage)
                .registerPacket(ManaSparklePacket.type(), ManaSparklePacket.class, ManaSparklePacket.STREAM_CODEC, ManaSparklePacket::onMessage)
                .registerPacket(SyncCooldownsPacket.type(), SyncCooldownsPacket.class, SyncCooldownsPacket.STREAM_CODEC, SyncCooldownsPacket::onMessage)
                .registerPacket(SpellTrailPacket.type(), SpellTrailPacket.class, SpellTrailPacket.STREAM_CODEC, SpellTrailPacket::onMessage)
                .registerPacket(SpellImpactPacket.type(), SpellImpactPacket.class, SpellImpactPacket.STREAM_CODEC, SpellImpactPacket::onMessage)
                .registerPacket(TileToClientPacket.type(), TileToClientPacket.class, TileToClientPacket.STREAM_CODEC, TileToClientPacket::onMessage)
                .registerPacket(TeleportArrivalPacket.type(), TeleportArrivalPacket.class, TeleportArrivalPacket.STREAM_CODEC, TeleportArrivalPacket::onMessage)
                .registerPacket(SpellBoltPacket.type(), SpellBoltPacket.class, SpellBoltPacket.STREAM_CODEC, SpellBoltPacket::onMessage)
                .registerPacket(SyncStatsPacket.type(), SyncStatsPacket.class, SyncStatsPacket.STREAM_CODEC, SyncStatsPacket::onMessage)
                .registerPacket(SyncAttunementsPacket.type(), SyncAttunementsPacket.class, SyncAttunementsPacket.STREAM_CODEC, SyncAttunementsPacket::onMessage)
                .registerPacket(PlayClientSoundPacket.type(), PlayClientSoundPacket.class, PlayClientSoundPacket.STREAM_CODEC, PlayClientSoundPacket::onMessage)
                .registerPacket(OfferingChannelPacket.type(), OfferingChannelPacket.class, OfferingChannelPacket.STREAM_CODEC, OfferingChannelPacket::onMessage)
                .registerPacket(PropMarkerPacket.type(), PropMarkerPacket.class, PropMarkerPacket.STREAM_CODEC, PropMarkerPacket::onMessage)
                .registerPacket(RemovePropMarkerPacket.type(), RemovePropMarkerPacket.class, RemovePropMarkerPacket.STREAM_CODEC, RemovePropMarkerPacket::onMessage)
                .registerPacket(SyncCompanionsPacket.type(), SyncCompanionsPacket.class, SyncCompanionsPacket.STREAM_CODEC, SyncCompanionsPacket::onMessage)
                .registerPacket(PotionExplosionPacket.type(), PotionExplosionPacket.class, PotionExplosionPacket.STREAM_CODEC, PotionExplosionPacket::onMessage)
                .registerPacket(UpdateAffinitiesPacket.type(), UpdateAffinitiesPacket.class, UpdateAffinitiesPacket.STREAM_CODEC, UpdateAffinitiesPacket::onMessage)
                .registerPacket(SyncArcaneRecipeBookPacket.type(), SyncArcaneRecipeBookPacket.class, SyncArcaneRecipeBookPacket.STREAM_CODEC, SyncArcaneRecipeBookPacket::onMessage)
                .registerPacket(PlaceGhostArcaneRecipePacket.type(), PlaceGhostArcaneRecipePacket.class, PlaceGhostArcaneRecipePacket.STREAM_CODEC, PlaceGhostArcaneRecipePacket::onMessage)
                .registerPacket(SpellcraftingRunePacket.type(), SpellcraftingRunePacket.class, SpellcraftingRunePacket.STREAM_CODEC, SpellcraftingRunePacket::onMessage)
                .registerPacket(OpenGrimoireScreenPacket.type(), OpenGrimoireScreenPacket.class, OpenGrimoireScreenPacket.STREAM_CODEC, OpenGrimoireScreenPacket::onMessage)
                .registerPacket(SyncWardPacket.type(), SyncWardPacket.class, SyncWardPacket.STREAM_CODEC, SyncWardPacket::onMessage)
                .registerPacket(OpenStaticBookScreenPacket.type(), OpenStaticBookScreenPacket.class, OpenStaticBookScreenPacket.STREAM_CODEC, OpenStaticBookScreenPacket::onMessage)
                .registerPacket(OpenEnchantedBookScreenPacket.type(), OpenEnchantedBookScreenPacket.class, OpenEnchantedBookScreenPacket.STREAM_CODEC, OpenEnchantedBookScreenPacket::onMessage)
                .registerPacket(SyncLinguisticsPacket.type(), SyncLinguisticsPacket.class, SyncLinguisticsPacket.STREAM_CODEC, SyncLinguisticsPacket::onMessage)
                .registerPacket(ContainerSetVarintDataPacket.type(), ContainerSetVarintDataPacket.class, ContainerSetVarintDataPacket.STREAM_CODEC, ContainerSetVarintDataPacket::onMessage)
                .registerPacket(UpdateLinguisticsGridsPacket.type(), UpdateLinguisticsGridsPacket.class, UpdateLinguisticsGridsPacket.STREAM_CODEC, UpdateLinguisticsGridsPacket::onMessage)
                // Server-bound play channel packets
                .registerPacket(SyncProgressPacket.type(), SyncProgressPacket.class, SyncProgressPacket.STREAM_CODEC, SyncProgressPacket::onMessage)
                ;
    }
}
