package org.omegat.swing.extra;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicLookAndFeel;

public class ExtraLocalesLookAndFeel extends BasicLookAndFeel {

    protected final LookAndFeel originalLookAndFeel;

    public ExtraLocalesLookAndFeel(LookAndFeel originalLaf) {
        originalLookAndFeel = originalLaf;
    }

    @Override
    public UIDefaults getDefaults() {
        return ExtraLocales.setDefaults(originalLookAndFeel.getDefaults());
    }

    @Override
    public String getName() {
        return "ExtraLocales";
    }

    @Override
    public String getID() {
        return "ExtraLocales";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public boolean isNativeLookAndFeel() {
        return originalLookAndFeel.isNativeLookAndFeel();
    }

    @Override
    public boolean isSupportedLookAndFeel() {
        return originalLookAndFeel.isSupportedLookAndFeel();
    }

    @Override
    public LayoutStyle getLayoutStyle() {
        return originalLookAndFeel.getLayoutStyle();
    }

    @Override
    public Icon getDisabledIcon(JComponent component, Icon icon) {
        return originalLookAndFeel.getDisabledIcon(component, icon);
    }

    @Override
    public Icon getDisabledSelectedIcon(JComponent component, Icon icon) {
        return originalLookAndFeel.getDisabledSelectedIcon(component, icon);
    }

    @Override
    public boolean getSupportsWindowDecorations() {
        return originalLookAndFeel.getSupportsWindowDecorations();
    }

    @Override
    public void provideErrorFeedback(Component component) {
        originalLookAndFeel.provideErrorFeedback(component);
    }

    @Override
    public void initialize() {
        originalLookAndFeel.initialize();
    }

    @Override
    public void uninitialize() {
        originalLookAndFeel.uninitialize();
    }
}
