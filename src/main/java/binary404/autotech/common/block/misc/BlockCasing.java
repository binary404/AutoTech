package binary404.autotech.common.block.misc;

import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;

public class BlockCasing extends Block {

    public static List<BlockCasing> casings = new ArrayList<>();

    public BlockCasing(Properties properties) {
        super(properties);
        casings.add(this);
    }
}
