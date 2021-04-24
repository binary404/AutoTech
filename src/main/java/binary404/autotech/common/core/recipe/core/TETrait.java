package binary404.autotech.common.core.recipe.core;

import binary404.autotech.common.tile.core.TileCore;
import net.minecraft.nbt.CompoundNBT;

public abstract class TETrait {

    protected TileCore tileCore;

    public TETrait(TileCore tileCore) {
        this.tileCore = tileCore;
        tileCore.addTileEntityTrait(this);
    }

    public TileCore getTileCore() {
        return tileCore;
    }

    public abstract String getName();

    public abstract int getNetworkID();

    public void update() {

    }

    public CompoundNBT serializeNBT() {
        return new CompoundNBT();
    }

    public void deserializeNBT(CompoundNBT compound) {

    }

    protected static final class TraitNetworkIds {
        public static final int TRAIT_ID_WORKABLE = 1;
    }
}
