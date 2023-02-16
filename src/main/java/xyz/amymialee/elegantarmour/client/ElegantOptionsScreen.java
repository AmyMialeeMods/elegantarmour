package xyz.amymialee.elegantarmour.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import xyz.amymialee.elegantarmour.config.ElegantClientConfig;
import xyz.amymialee.elegantarmour.config.ElegantClientSettings;
import xyz.amymialee.elegantarmour.config.ElegantPart;
import xyz.amymialee.elegantarmour.util.IEleganttable;

import java.util.EnumSet;
import java.util.Set;

public class ElegantOptionsScreen extends GameOptionsScreen {
    public static final Set<ElegantPart> ENABLED_ELEGANT_PARTS = EnumSet.noneOf(ElegantPart.class);

    public ElegantOptionsScreen(Screen parent, GameOptions gameOptions) {
        super(parent, gameOptions, Text.translatable("options.elegantCustomisation.title"));
    }

    @Override
    protected void init() {
        int i = 0;

        for (ElegantPart elegantPart : ElegantPart.values()) {
            if (elegantPart.getId() == 7) continue;
            this.addDrawableChild(CyclingButtonWidget.onOffBuilder(isElegantPartEnabled(elegantPart)).build(
                    this.width / 2 - 155 + i % 2 * 160 + (elegantPart.getId() == 6 ? 80 : 0), this.height / 6 + 24 * (i >> 1), 150, 20, elegantPart.getOptionName(),
                    (button, enabled) -> toggleElegantPart(elegantPart, enabled)));
            i++;
        }
        i += 1;
        for (ElegantClientSettings elegantSetting : ElegantClientSettings.values()) {
            this.addDrawableChild(CyclingButtonWidget.onOffBuilder(ElegantClientSettings.isElegantPartEnabled(elegantSetting)).build(
                    this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), 150, 20, elegantSetting.getOptionName(),
                    (button, enabled) -> ElegantClientSettings.toggleElegantPart(elegantSetting, enabled)));
            i++;
        }

        if (i % 2 == 1) {
            i++;
        }

        if (this.client == null) {
            return;
        }

        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height / 6 + 24 * (i >> 1), 200, 20, ScreenTexts.DONE, button -> this.client.setScreen(this.parent)));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 20, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
    }

    public static void setElegantPart(ElegantPart part, boolean enabled) {
        if (enabled) {
            ENABLED_ELEGANT_PARTS.add(part);
        } else {
            ENABLED_ELEGANT_PARTS.remove(part);
        }
        ElegantClientConfig.saveConfig();
        for (ElegantPart eachPart : ElegantPart.values()) {
            if (MinecraftClient.getInstance().player instanceof IEleganttable eleganttable) {
                eleganttable.setElegantPart(eachPart, isElegantPartEnabled(eachPart));
            }
        }
    }

    public static boolean isElegantPartEnabled(ElegantPart part) {
        return ENABLED_ELEGANT_PARTS.contains(part);
    }

    public static void toggleElegantPart(ElegantPart part, boolean enabled) {
        setElegantPart(part, enabled);
        MinecraftClient.getInstance().options.sendClientSettings();
    }
}