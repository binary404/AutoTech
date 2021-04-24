package binary404.autotech.common.block.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.IStringSerializable;

public class BlockAutoTechOre extends OreBlock {

    public static EnumProperty<OreType> ORE_TYPE = EnumProperty.create("ore_type", OreType.class);

    public BlockAutoTechOre(Properties properties) {
        super(properties);
        this.setDefaultState(this.getDefaultState().with(ORE_TYPE, OreType.STONE));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(ORE_TYPE);
    }

    public static enum OreType implements IStringSerializable {
        STONE("stone"),
        ANDESITE("andesite"),
        GRANITE("granite"),
        DIORITE("diorite");

        String id;

        OreType(String id) {
            this.id = id;
        }


        @Override
        public String getString() {
            return this.id;
        }
    }

}
