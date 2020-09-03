package binary404.autotech.common.tile.machine;

import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.tile.core.TileCore;
import binary404.autotech.common.tile.core.TileMachine;
import net.minecraft.tileentity.TileEntityType;

public class TileCompactor extends TileMachine {



    public TileCompactor() {
        this(Tier.LV);
    }

    public TileCompactor(Tier tier) {
        super(null, tier);
        this.inv.set(2);
    }

    @Override
    protected boolean canStart() {
        return super.canStart();
    }
}
