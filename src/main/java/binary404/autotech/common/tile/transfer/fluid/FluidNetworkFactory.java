package binary404.autotech.common.tile.transfer.fluid;

import binary404.autotech.common.core.util.StringUtil;
import binary404.autotech.common.tile.transfer.network.Network;
import binary404.autotech.common.tile.transfer.network.NetworkFactory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class FluidNetworkFactory implements NetworkFactory {

    private static final Logger LOGGER = LogManager.getLogger(FluidNetworkFactory.class);

    private final FluidPipeType pipeType;

    public FluidNetworkFactory(FluidPipeType pipeType) {
        this.pipeType = pipeType;
    }

    @Override
    public Network create(BlockPos pos) {
        return new FluidNetwork(pos, StringUtil.randomString(new Random(), 8), pipeType);
    }

    @Override
    public Network create(CompoundNBT tag) {
        FluidNetwork network = new FluidNetwork(BlockPos.fromLong(tag.getLong("origin")), tag.getString("id"), pipeType);

        if (tag.contains("tank")) {
            network.getFluidTank().readFromNBT(tag.getCompound("tank"));
        }

        LOGGER.debug("Deserialized fluid network {} of type {}", network.getId(), network.getType().toString());

        return network;
    }

}
