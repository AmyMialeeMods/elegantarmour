package xyz.amymialee.elegantarmour.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import xyz.amymialee.elegantarmour.ElegantArmourClient;
import xyz.amymialee.elegantarmour.ElegantArmourConfig;
import xyz.amymialee.elegantarmour.util.ElegantIcons;
import xyz.amymialee.elegantarmour.util.ElegantPlayerData;
import xyz.amymialee.elegantarmour.util.ElegantState;

import java.util.Locale;
import java.util.function.Consumer;

import static net.minecraft.client.gui.screen.ingame.InventoryScreen.drawEntity;

public class ElegantOptionsScreen extends Screen {
    private static ArmorStandEntity backupEntity;
    private final ElegantPlayerData data;
    private final PlayerEntity player;
    private final Screen parent;
    private int x;
    private int y;

    public ElegantOptionsScreen(Screen parent, PlayerEntity player, @NotNull ElegantPlayerData data) {
        super(Text.literal(data.getPlayerName()));
        this.parent = parent;
        this.player = player;
        this.data = data;
    }

    @Override
    public void close() {
        if (this.client != null && this.parent != null) {
            this.client.setScreen(this.parent);
        }
    }

    @Override
    protected void init() {
        this.x = (this.width / 2) - (ElegantIcons.BACKGROUND.getWidth() / 2);
        this.y = (this.height / 2) - (ElegantIcons.BACKGROUND.getHeight() / 2);
        boolean isClientPlayer = false;
        if (this.client != null && this.client.player != null) {
            if (this.client.player == this.player) {
                isClientPlayer = true;
            }
        }
        this.addButtons(isClientPlayer, this.y + 19, ElegantIcons.HELMET, "options.elegantarmour.head");
        this.addButtons(isClientPlayer, this.y + 19 + 18, ElegantIcons.CHESTPLATE, "options.elegantarmour.chest");
        this.addButtons(isClientPlayer, this.y + 19 + 2 * 18, ElegantIcons.LEGGINGS, "options.elegantarmour.legs");
        this.addButtons(isClientPlayer, this.y + 19 + 3 * 18, ElegantIcons.BOOTS, "options.elegantarmour.feet");
        this.addButtons(isClientPlayer, this.y + 19 + 4 * 18, ElegantIcons.ELYTRA, "options.elegantarmour.elytra");
        this.addButtons(isClientPlayer, this.y + 19 + 5 * 18, ElegantIcons.SMALL, "options.elegantarmour.small", true);
    }

    private void addButtons(boolean isClientPlayer, int y, ElegantIcons icon, String key, boolean inverted) {
        Text message = Text.translatable(key);
        ElegantDisplayWidget displayWidget = this.addDrawableChild(new ElegantDisplayWidget(this.x + 75, y, this.data.getState(icon.ordinal() - 6), isClientPlayer, message, icon, inverted));
        Consumer<ElegantState> stateConsumer = state -> {
            this.data.setState(icon.ordinal() - 6, state);
            displayWidget.setValue(state);
        };
        this.addDrawableChild(new ElegantButtonWidget(this.x + 75 + 18, y, message, this.data, ElegantState.DEFAULT, stateConsumer, icon, Text.translatable(ElegantState.DEFAULT.getTranslationKey())));
        this.addDrawableChild(new ElegantButtonWidget(this.x + 75 + 18 + ElegantIcons.OPTION_DEFAULT.getWidth(), y, message, this.data, ElegantState.SHOW, stateConsumer, icon, Text.translatable(icon == ElegantIcons.SMALL ? ElegantState.SHOW.getSmallKey() : ElegantState.SHOW.getTranslationKey())));
        this.addDrawableChild(new ElegantButtonWidget(this.x + 75 + 18 + ElegantIcons.OPTION_DEFAULT.getWidth() * 2, y, message, this.data, ElegantState.HIDE, stateConsumer, icon, Text.translatable(icon == ElegantIcons.SMALL ? ElegantState.HIDE.getSmallKey() : ElegantState.HIDE.getTranslationKey())));
    }

    private void addButtons(boolean isClientPlayer, int y, ElegantIcons icon, String key) {
        addButtons(isClientPlayer, y, icon, key, false);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        context.drawTexture(ElegantIcons.ELEGANT_TEXTURE, this.x, this.y, 0, 0, ElegantIcons.BACKGROUND.getWidth(), ElegantIcons.BACKGROUND.getHeight());
        if (this.player != null) {
            drawEntity(context, this.x + 39, this.y + 118, 46, (float) (this.x + 39) - mouseX, (float) (this.y + 46) - mouseY, this.player);
        } else if (this.client != null && this.client.world != null && this.client.player != null) {
            if (backupEntity == null) {
                backupEntity = new ArmorStandEntity(this.client.world, this.client.player.getX(), this.client.player.getY(), this.client.player.getZ());
                if (this.shouldShow(0)) backupEntity.equipStack(EquipmentSlot.HEAD, Items.IRON_HELMET.getDefaultStack());
                if (this.shouldShow(1)) {
                    backupEntity.equipStack(EquipmentSlot.CHEST, Items.IRON_CHESTPLATE.getDefaultStack());
                } else if (this.shouldShow(4)) {
                    backupEntity.equipStack(EquipmentSlot.CHEST, Items.ELYTRA.getDefaultStack());
                }
                if (this.shouldShow(2)) backupEntity.equipStack(EquipmentSlot.LEGS, Items.IRON_LEGGINGS.getDefaultStack());
                if (this.shouldShow(3)) backupEntity.equipStack(EquipmentSlot.FEET, Items.IRON_BOOTS.getDefaultStack());
                backupEntity.setHideBasePlate(true);
                backupEntity.setShowArms(true);
            }
            drawEntity(context, this.x + 39, this.y + 118, 46, (float) (this.x + 39) - mouseX, (float) (this.y + 46) - mouseY, backupEntity);
        }
        Text name = this.data == ElegantArmourConfig.defaultSettings ? Text.translatable("options.elegantDefault") : Text.literal(this.data.getPlayerName()).append(" ").append(Text.translatable("options.elegantCustomisation"));
        context.drawText(this.textRenderer, name, this.width / 2 - this.textRenderer.getWidth(name) / 2, this.y + 7, 4210752, false);
        super.render(context, mouseX, mouseY, delta);
    }

    private boolean shouldShow(int index) {
        ElegantState state = this.data.getState(index);
        ElegantState configState = ElegantArmourConfig.getState(index);
        if (configState == ElegantState.HIDE) {
            return false;
        } else if (configState == ElegantState.DEFAULT) {
            return state != ElegantState.HIDE && (state != ElegantState.DEFAULT || ElegantArmourConfig.getState(index) != ElegantState.HIDE);
        }
        return true;
    }

    private static class ElegantDisplayWidget extends ClickableWidget {
        private ElegantState value;
        private final ElegantIcons icon;
        private final boolean isClientPlayer;
        private final boolean inverted;

        public ElegantDisplayWidget(int x, int y, ElegantState value, boolean isClientPlayer, Text message, ElegantIcons icon, boolean inverted) {
            super(x, y, 18, 18, message);
            this.value = value;
            this.icon = icon;
            this.isClientPlayer = isClientPlayer;
            this.inverted = inverted;
            this.setTooltip(Tooltip.of(Text.translatable("options.elegantarmour." + icon.name().toLowerCase(Locale.ROOT))));
        }

        public void setValue(ElegantState value) {
            this.value = value;
            if (this.isClientPlayer) ElegantArmourClient.syncC2S();
            ElegantArmourConfig.saveConfig();
        }

        @Override
        public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            var icon = this.isMouseOver(mouseX, mouseY) ? ElegantIcons.BOX_HOVER : ElegantIcons.BOX_DEFAULT;
            context.drawTexture(ElegantIcons.ELEGANT_TEXTURE, this.getX(), this.getY(), icon.getU(), icon.getV(), icon.getWidth(), icon.getHeight());
            if (this.value == (this.inverted ? ElegantState.SHOW : ElegantState.HIDE)) {
                RenderSystem.setShaderColor(0.2f, 0.2f, 0.2f, 1.0f);
            } else if (this.value == ElegantState.DEFAULT) {
                RenderSystem.setShaderColor(0.65f, 0.65f, 0.65f, 1.0f);
            }
            context.drawTexture(ElegantIcons.ELEGANT_TEXTURE, this.getX(), this.getY(), this.icon.getU(), this.icon.getV(), this.icon.getWidth(), this.icon.getHeight());
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        }

        @Override
        protected void appendClickableNarrations(NarrationMessageBuilder builder) {}
    }

    private static class ElegantButtonWidget extends PressableWidget {
        private final ElegantPlayerData data;
        private final ElegantState value;
        private final Consumer<ElegantState> callback;
        private final ElegantIcons icon;
        private final Text text;

        ElegantButtonWidget(int x, int y, Text message, ElegantPlayerData data, ElegantState value, Consumer<ElegantState> callback, ElegantIcons icon, Text text) {
            super(x, y, ElegantIcons.OPTION_DEFAULT.getWidth(), ElegantIcons.OPTION_DEFAULT.getHeight(), message);
            this.data = data;
            this.value = value;
            this.callback = callback;
            this.icon = icon;
            this.text = text;
        }

        @Override
        public void onPress() {
            boolean selected = this.data.getState(this.icon.ordinal() - 6) == this.value;
            if (!selected) {
                this.callback.accept(this.value);
                backupEntity = null;
            }
        }

        @Override
        public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            boolean selected = this.data.getState(this.icon.ordinal() - 6) == this.value;
            ElegantIcons icon = selected ? ElegantIcons.OPTION_SELECTED : this.isMouseOver(mouseX, mouseY) ? ElegantIcons.OPTION_HOVER : ElegantIcons.OPTION_DEFAULT;
            context.drawTexture(ElegantIcons.ELEGANT_TEXTURE, this.getX(), this.getY(), icon.getU(), icon.getV(), icon.getWidth(), icon.getHeight());
            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
            context.drawTextWrapped(MinecraftClient.getInstance().textRenderer, this.text, this.getX() + ElegantIcons.OPTION_DEFAULT.getWidth() / 2 - textRenderer.getWidth(this.text) / 2, this.getY() + textRenderer.getWrappedLinesHeight(this.text, ElegantIcons.OPTION_DEFAULT.getWidth()) / 2 + 1, ElegantIcons.OPTION_DEFAULT.getWidth(), (6839882 & 16711422) >> 1);
        }

        @Override
        protected void appendClickableNarrations(NarrationMessageBuilder builder) {}
    }
}
