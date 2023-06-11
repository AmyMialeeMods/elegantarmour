package xyz.amymialee.elegantarmour.util;

public enum ElegantState {
    DEFAULT("options.elegantarmour.default", "options.elegantarmour.small.default"),
    HIDE("options.elegantarmour.hide", "options.elegantarmour.small.hide"),
    SHOW("options.elegantarmour.show", "options.elegantarmour.small.show");

    private final String translationKey;
    private final String smallKey;

    ElegantState(String translationKey, String smallKey) {
        this.translationKey = translationKey;
        this.smallKey = smallKey;
    }

    public String getTranslationKey() {
        return this.translationKey;
    }

    public String getSmallKey() {
        return this.smallKey;
    }
}