package binary404.autotech.common.block.world;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;

import java.util.List;
import java.util.Random;

public class BlockRubberWood extends RotatedPillarBlock {

    public static final BooleanProperty NATURAL = BooleanProperty.create("natural");

    public BlockRubberWood() {
        super(Properties.create(Material.WOOD, (state) -> {
            return state.get(AXIS) == Direction.Axis.Y ? MaterialColor.WOOD : MaterialColor.SAND;
        }).hardnessAndResistance(2.0F).sound(SoundType.WOOD));
        this.setDefaultState(this.getDefaultState().with(AXIS, Direction.Axis.Y).with(NATURAL, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(NATURAL);
    }
}
