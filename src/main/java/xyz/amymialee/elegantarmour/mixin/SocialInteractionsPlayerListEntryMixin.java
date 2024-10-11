package xyz.amymialee.elegantarmour.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.multiplayer.SocialInteractionsPlayerListEntry;
import net.minecraft.client.gui.screen.multiplayer.SocialInteractionsScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.amymialee.elegantarmour.ElegantArmour;
import xyz.amymialee.elegantarmour.ElegantArmourConfig;
import xyz.amymialee.elegantarmour.client.ElegantMenuWidget;
import xyz.amymialee.elegantarmour.client.ElegantOptionsScreen;
import xyz.amymialee.elegantarmour.util.ElegantPlayerData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

@Mixin(SocialInteractionsPlayerListEntry.class)
public class SocialInteractionsPlayerListEntryMixin {
    @Shadow @Final @Mutable private List<ClickableWidget> buttons;
    @Unique private ButtonWidget elegantButton;
    @Unique private ButtonWidget defaultButton;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void elegantArmour$button(MinecraftClient client, SocialInteractionsScreen parent, UUID uuid, String name, Supplier<Identifier> skinTexture, boolean reportable, CallbackInfo ci) {
        if (this.buttons.isEmpty()) this.buttons = new ArrayList<>();
        ElegantPlayerData data = ElegantArmourConfig.getOrCreate(uuid, name);
        PlayerEntity player;
        if (client.world != null) {
            player = client.world.getPlayerByUuid(uuid);
        } else {
            player = null;
        }
        this.elegantButton = new ElegantMenuWidget(0, 0, Text.translatable("options.elegantCustomisation"), button -> client.setScreen(new ElegantOptionsScreen(parent, player, data)), player != null && ElegantArmour.ARMOUR.get(player).hasMod);
        this.elegantButton.setTooltip(Tooltip.of(Text.translatable("options.elegantCustomisation")));
        this.elegantButton.setTooltipDelay(10);
        this.elegantButton.active = true;
        this.buttons.add(this.elegantButton);
        if (player == client.player) {
            this.defaultButton = new ElegantMenuWidget(0, 0, Text.translatable("options.elegantDefault"), button -> client.setScreen(new ElegantOptionsScreen(parent, null, ElegantArmourConfig.defaultSettings)), false);
            this.defaultButton.setTooltip(Tooltip.of(Text.translatable("options.elegantDefault")));
            this.defaultButton.setTooltipDelay(10);
            this.defaultButton.active = true;
            this.buttons.add(this.defaultButton);
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void elegantArmour$renderButton(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta, CallbackInfo ci) {
        if (this.elegantButton != null) {
            this.elegantButton.setX(x + (entryWidth - 60 - 12));
            this.elegantButton.setY(y + (entryHeight - 20) / 2);
            this.elegantButton.render(context, mouseX, mouseY, tickDelta);
        }
        if (this.defaultButton != null) {
            this.defaultButton.setX(x + (entryWidth - 30 - 6));
            this.defaultButton.setY(y + (entryHeight - 20) / 2);
            this.defaultButton.render(context, mouseX, mouseY, tickDelta);
        }
    }
}