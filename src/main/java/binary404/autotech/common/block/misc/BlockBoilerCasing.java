package binary404.autotech.common.block.misc;

import com.sun.org.apache.xpath.internal.operations.Bool;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraftforge.common.ToolType;

public class BlockBoilerCasing extends Block {

    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public BlockBoilerCasing() {
        super(Properties.create(Material.IRON).hardnessAndResistance(5.0F, 10.0F).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(2).setRequiresTool().setLightLevel(state -> state.get(ACTIVE) ? 15 : 0));
        setDefaultState(getDefaultState().with(ACTIVE, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
    }


}
