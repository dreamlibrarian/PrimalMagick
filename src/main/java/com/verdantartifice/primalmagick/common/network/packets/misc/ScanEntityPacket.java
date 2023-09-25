package com.verdantartifice.primalmagick.common.network.packets.misc;

import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.research.ResearchManager;

import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Packet sent to trigger a server-side scan of a given entity.  Used by the arcanometer for
 * scanning entities in the world.
 * 
 * @author Daedalus4096
 */
public class ScanEntityPacket implements IMessageToServer {
    protected static final Logger LOGGER = LogManager.getLogger();
    
    protected EntityType<?> type;
    
    public ScanEntityPacket() {
        this.type = null;
    }
    
    public ScanEntityPacket(EntityType<?> type) {
        this.type = type;
    }
    
    public static void encode(ScanEntityPacket message, FriendlyByteBuf buf) {
        buf.writeUtf(ForgeRegistries.ENTITY_TYPES.getKey(message.type).toString());
    }
    
    public static ScanEntityPacket decode(FriendlyByteBuf buf) {
        ScanEntityPacket message = new ScanEntityPacket();
        message.type = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(buf.readUtf()));
        return message;
    }
    
    public static class Handler {
        public static void onMessage(ScanEntityPacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                if (message.type != null) {
                    ServerPlayer player = ctx.get().getSender();
                    ResearchManager.isScannedAsync(message.type, player).thenAccept(isScanned -> {
                        if (isScanned) {
                            player.displayClientMessage(Component.translatable("event.primalmagick.scan.repeat").withStyle(ChatFormatting.RED), true);
                        } else if (ResearchManager.setScanned(message.type, player)) {
                            player.displayClientMessage(Component.translatable("event.primalmagick.scan.success").withStyle(ChatFormatting.GREEN), true);
                        } else {
                            player.displayClientMessage(Component.translatable("event.primalmagick.scan.fail").withStyle(ChatFormatting.RED), true);
                        }
                    }).exceptionally(e -> {
                        LOGGER.error("Failed to scan entity type " + message.type.toString(), e);
                        return null;
                    });
                }
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
