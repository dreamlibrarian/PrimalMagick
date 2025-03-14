package com.verdantartifice.primalmagick.common.network.packets.fx;

import java.util.function.Supplier;

import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

/**
 * Packet sent from the server to trigger a spell trail particle effect on the client.
 * 
 * @author Daedalus4096
 */
public class SpellTrailPacket implements IMessageToClient {
    protected double x;
    protected double y;
    protected double z;
    protected int color;
    
    public SpellTrailPacket() {}
    
    public SpellTrailPacket(double x, double y, double z, int color) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.color = color;
    }
    
    public SpellTrailPacket(Vec3 pos, int color) {
        this(pos.x, pos.y, pos.z, color);
    }
    
    public static void encode(SpellTrailPacket message, FriendlyByteBuf buf) {
        buf.writeDouble(message.x);
        buf.writeDouble(message.y);
        buf.writeDouble(message.z);
        buf.writeInt(message.color);
    }
    
    public static SpellTrailPacket decode(FriendlyByteBuf buf) {
        SpellTrailPacket message = new SpellTrailPacket();
        message.x = buf.readDouble();
        message.y = buf.readDouble();
        message.z = buf.readDouble();
        message.color = buf.readInt();
        return message;
    }
    
    public static class Handler {
        public static void onMessage(SpellTrailPacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                FxDispatcher.INSTANCE.spellTrail(message.x, message.y, message.z, message.color);
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
