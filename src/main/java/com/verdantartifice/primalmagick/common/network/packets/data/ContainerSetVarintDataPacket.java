package com.verdantartifice.primalmagick.common.network.packets.data;

import java.util.function.Supplier;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkEvent;

/**
 * Packet to sync a menu data slot value from the server to the client as a varint, to allow for
 * larger data values than vanilla.
 * 
 * @author Daedalus4096
 */
public class ContainerSetVarintDataPacket implements IMessageToClient {
    private final int containerId;
    private final int slotId;
    private final int dataValue;

    public ContainerSetVarintDataPacket(int containerId, int slotId, int dataValue) {
        this.containerId = containerId;
        this.slotId = slotId;
        this.dataValue = dataValue;
    }
    
    public static void encode(ContainerSetVarintDataPacket message, FriendlyByteBuf buf) {
        buf.writeVarInt(message.containerId);
        buf.writeVarInt(message.slotId);
        buf.writeVarInt(message.dataValue);
    }
    
    public static ContainerSetVarintDataPacket decode(FriendlyByteBuf buf) {
        return new ContainerSetVarintDataPacket(buf.readVarInt(), buf.readVarInt(), buf.readVarInt());
    }
    
    public static class Handler {
        public static void onMessage(ContainerSetVarintDataPacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                Player player = (FMLEnvironment.dist == Dist.CLIENT) ? ClientUtils.getCurrentPlayer() : null;
                if (player != null && player.containerMenu != null && player.containerMenu.containerId == message.containerId) {
                    player.containerMenu.setData(message.slotId, message.dataValue);
                }
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
