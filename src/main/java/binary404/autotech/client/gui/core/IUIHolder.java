package binary404.autotech.client.gui.core;

public interface IUIHolder extends IDirtyNotifiable {

    boolean isRemote();

    void markAsDirty();

}
