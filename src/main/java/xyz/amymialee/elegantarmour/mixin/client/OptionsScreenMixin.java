package xyz.amymialee.elegantarmour.mixin.client;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.amymialee.elegantarmour.client.ElegantOptionsScreen;

@Mixin(OptionsScreen.class)
public class OptionsScreenMixin extends Screen {
    @Shadow @Final private GameOptions settings;

    protected OptionsScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void elegantArmour$init(CallbackInfo ci) {
        if (this.client == null) {
            return;
        }

        this.addDrawableChild(new ButtonWidget(this.width / 2 - 155 - 30, this.height / 6 + 48 - 6, 20, 20, Text.translatable("options.elegantCustomisation"), button -> this.client.setScreen(new ElegantOptionsScreen(this, this.settings))));
    }
}