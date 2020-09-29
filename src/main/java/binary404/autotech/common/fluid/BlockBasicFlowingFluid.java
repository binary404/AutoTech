package binary404.autotech.common.fluid;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;

import java.util.function.Supplier;

public class BlockBasicFlowingFluid extends FlowingFluidBlock {

    public BlockBasicFlowingFluid(Supplier<? extends FlowingFluid> fluidSupplier) {
        super(fluidSupplier, AbstractBlock.Properties.create(Material.WATER)
                .doesNotBlockMovement()
                .setLightLevel(state -> 0)
                .hardnessAndResistance(100.0F)
                .noDrops());
    }
}
