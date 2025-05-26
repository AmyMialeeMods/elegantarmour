package xyz.amymialee.elegantarmour.networking;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import org.jetbrains.annotations.NotNull;
import xyz.amymialee.elegantarmour.ElegantArmour;
import xyz.amymialee.elegantarmour.cca.ArmourComponent;

public record ClientUpdatePayload(byte head, byte chest, byte legs, byte feet, byte elytra, byte smallArmour) implements CustomPayload {
    public static final Id<ClientUpdatePayload> ID = new Id<>(ElegantArmour.id("client_update"));
    public static final PacketCodec<RegistryByteBuf, ClientUpdatePayload> CODEC = PacketCodec.tuple(PacketCodecs.BYTE, ClientUpdatePayload::head, PacketCodecs.BYTE, ClientUpdatePayload::chest, PacketCodecs.BYTE, ClientUpdatePayload::legs, PacketCodecs.BYTE, ClientUpdatePayload::feet, PacketCodecs.BYTE, ClientUpdatePayload::elytra, PacketCodecs.BYTE, ClientUpdatePayload::smallArmour, ClientUpdatePayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<ClientUpdatePayload> {
        @Override
        public void receive(@NotNull ClientUpdatePayload payload, ServerPlayNetworking.@NotNull Context context) {
            var component = ArmourComponent.KEY.get(context.player());
            component.data.setFromBytes(payload.head, payload.chest, payload.legs, payload.feet, payload.elytra, payload.smallArmour);
            component.hasMod = true;
            component.sync();
        }
    }
}