package xyz.amymialee.elegantarmour.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.option.SkinOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import xyz.amymialee.elegantarmour.client.ElegantOptionsScreen;

@Mixin(OptionsScreen.class)
public class OptionsScreenMixin extends Screen {
    @Shadow @Final private GameOptions settings;

    protected OptionsScreenMixin(Text title) {
        super(title);
    }

    @WrapOperation(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/option/OptionsScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;", ordinal = 4))
    private <T extends Element & Drawable & Selectable> T elegantArmour$thinSkinOptions(OptionsScreen screen, ButtonWidget widget, Operation<T> original) {
        if (this.client == null) {
            return original.call(screen, widget);
        }
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 155 + 130, this.height / 6 + 48 - 6, 20, 20, Text.translatable("options.elegantCustomisation"), button -> this.client.setScreen(new ElegantOptionsScreen(this, this.settings))));
        return original.call(screen, new ButtonWidget(this.width / 2 - 155, this.height / 6 + 48 - 6, 130, 20, Text.translatable("options.skinCustomisation"), button -> this.client.setScreen(new SkinOptionsScreen(this, this.settings))));
    }
}