package binary404.autotech.common.tile.transfer.item;

import binary404.autotech.common.core.util.StringUtil;
import binary404.autotech.common.tile.transfer.network.Network;
import binary404.autotech.common.tile.transfer.network.NetworkFactory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class ItemNetworkFactory implements NetworkFactory {

    private static final Logger LOGGER = LogManager.getLogger(ItemNetworkFactory.class);

    @Override
    public Network create(BlockPos pos) {
        return new ItemNetwork(pos, StringUtil.randomString(new Random(), 8));
    }

    @Override
    public Network create(CompoundNBT tag) {
        ItemNetwork network = new ItemNetwork(BlockPos.fromLong(tag.getLong("origin")), tag.getString("id"));

        LOGGER.debug("Deserialized item network {}", network.getId());

        return network;
    }

}
