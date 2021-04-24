package binary404.autotech.client.gui.core;

import java.awt.*;

public interface ISizeProvider {

    int getScreenWidth();

    int getScreenHeight();

    int getWidth();

    int getHeight();

    default int getGuiLeft() {
        return (getScreenWidth() - getWidth()) / 2;
    }

    default int getGuiTop() {
        return (getScreenHeight() - getHeight()) / 2;
    }

    default Rectangle toScreenCoords(Rectangle widgetRect) {
        return new Rectangle(getGuiLeft() + widgetRect.x, getGuiTop() + widgetRect.y, widgetRect.width, widgetRect.height);
    }

}
