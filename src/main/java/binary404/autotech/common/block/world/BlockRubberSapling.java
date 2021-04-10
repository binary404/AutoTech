package binary404.autotech.common.block.world;

import binary404.autotech.common.world.tree.RubberTree;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockRubberSapling extends SaplingBlock {

    public BlockRubberSapling() {
        super(new RubberTree(), Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0).sound(SoundType.PLANT));
    }

}
