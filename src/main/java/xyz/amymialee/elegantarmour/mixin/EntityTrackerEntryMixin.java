package xyz.amymialee.elegantarmour.mixin;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.EntityTrackerEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.amymialee.elegantarmour.ElegantArmour;
import xyz.amymialee.elegantarmour.config.ElegantPart;
import xyz.amymialee.elegantarmour.util.IEleganttable;

import java.util.function.Consumer;

@Mixin(EntityTrackerEntry.class)
public class EntityTrackerEntryMixin {
    @Shadow @Final private Entity entity;
    @Shadow @Final private Consumer<Packet<?>> receiver;

    @Inject(method = "syncEntityData", at = @At("TAIL"))
    private void elegantArmour$trackEntity(CallbackInfo ci) {
        if (this.entity.isRemoved()) {
            return;
        }
        if (this.entity instanceof IEleganttable eleganttable) {
            int i = 0;
            for (ElegantPart playerModelPart : eleganttable.getEnabledParts()) {
                i |= playerModelPart.getBitFlag();
            }
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeInt(this.entity.getId());
            buf.writeByte(i);
            this.receiver.accept(ServerPlayNetworking.createS2CPacket(ElegantArmour.elegantS2C, buf));
        }
    }
}