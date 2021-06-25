package binary404.autotech.client.gui;

import binary404.autotech.client.gui.core.IUIHolder;

public class BlankUIHolder implements IUIHolder {

    @Override
    public boolean isRemote() {
        return false;
    }

    @Override
    public void markAsDirty() {

    }
}
