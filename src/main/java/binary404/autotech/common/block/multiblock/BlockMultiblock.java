package binary404.autotech.common.block.multiblock;

import binary404.autotech.client.renders.core.ICubeRenderer;
import binary404.autotech.common.block.BlockTile;

import java.util.ArrayList;
import java.util.List;

public class BlockMultiblock extends BlockTile {

    public static List<BlockMultiblock> multiblocks = new ArrayList<>();

    ICubeRenderer particleRenderer;

    public BlockMultiblock(ICubeRenderer particleRenderer) {
        super();
        this.particleRenderer = particleRenderer;
        multiblocks.add(this);
    }

    public ICubeRenderer getParticleRenderer() {
        return particleRenderer;
    }
}
