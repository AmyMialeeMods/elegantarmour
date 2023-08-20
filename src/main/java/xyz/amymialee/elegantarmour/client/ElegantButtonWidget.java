package xyz.amymialee.elegantarmour.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import xyz.amymialee.elegantarmour.ElegantArmour;
import xyz.amymialee.elegantarmour.util.IEleganttable;

import java.awt.Color;

public class ElegantButtonWidget extends ButtonWidget {
    public static final Identifier ELEGANT_TEXTURE = ElegantArmour.id("textures/gui/widgets.png");
    private static float cycle = 0f;

    public ElegantButtonWidget(int x, int y, Text message, ButtonWidget.PressAction action) {
        super(x, y, 20, 20, message, action);
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, ELEGANT_TEXTURE);
        IconLocation iconLocation = this.isHovered() ? IconLocation.BOX_HOVER : IconLocation.BOX_DEFAULT;
        this.drawTexture(matrices, this.x, this.y, iconLocation.getU(), iconLocation.getV(), 20, 20);
        if (MinecraftClient.getInstance().player instanceof IEleganttable eleganttable && eleganttable.isElegantEnabled()) {
            cycle = cycle + delta % 160f;
            Color colour = Color.getHSBColor(cycle / 160f, 0.65f, 1.0f);
            RenderSystem.setShaderColor(colour.getRed() / 255f, colour.getGreen() / 255f, colour.getBlue() / 255f, 1.0F);
        } else {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, ELEGANT_TEXTURE);
        this.drawTexture(matrices, this.x, this.y, IconLocation.ICON.getU(), IconLocation.ICON.getV(), 20, 20);
    }

    enum IconLocation {
        BOX_DEFAULT(0, 0),
        BOX_HOVER(0, 20),
        ICON(0, 40);

        private final int u;
        private final int v;

        IconLocation(int u, int v) {
            this.u = u;
            this.v = v;
        }

        public int getU() {
            return this.u;
        }

        public int getV() {
            return this.v;
        }
    }
}