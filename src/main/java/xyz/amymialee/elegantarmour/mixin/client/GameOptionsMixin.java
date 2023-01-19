package xyz.amymialee.elegantarmour.mixin.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.network.PacketByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.amymialee.elegantarmour.ElegantArmour;
import xyz.amymialee.elegantarmour.config.ElegantPart;
import xyz.amymialee.elegantarmour.util.IEleganttable;

@Mixin(GameOptions.class)
public class GameOptionsMixin {
    @Shadow protected MinecraftClient client;

    @Inject(method = "sendClientSettings", at = @At("TAIL"))
    private void elegantArmour$sendSettings(CallbackInfo ci) {
        if (this.client.player instanceof IEleganttable eleganttable) {
            int i = 0;
            for (ElegantPart playerModelPart : eleganttable.getEnabledParts()) {
                i |= playerModelPart.getBitFlag();
            }
            i |= ElegantPart.CLIENT_ACTIVE.getBitFlag();
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeByte(i);
            ClientPlayNetworking.send(ElegantArmour.elegantC2S, buf);
        }
    }
}