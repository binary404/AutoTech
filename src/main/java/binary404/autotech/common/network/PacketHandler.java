package binary404.autotech.common.network;

import binary404.autotech.AutoTech;
import binary404.autotech.common.network.pipe.FluidPipeMessage;
import binary404.autotech.common.network.pipe.ItemTransportMessage;
import binary404.autotech.common.tile.transfer.fluid.FluidPipe;
import binary404.autotech.common.tile.transfer.pipe.item.ItemTransport;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {

    private static final String PROTOCOL = "6";
    public static final SimpleChannel HANDLER = NetworkRegistry.newSimpleChannel(
            AutoTech.key("chan"),
            () -> PROTOCOL,
            PROTOCOL::equals,
            PROTOCOL::equals
    );

    public static void init() {
        int id = 0;

        HANDLER.registerMessage(id++, PacketEnergySuit.class, PacketEnergySuit::encode, PacketEnergySuit::decode, PacketEnergySuit::handle);
        HANDLER.registerMessage(id++, PacketJetPack.class, PacketJetPack::encode, PacketJetPack::decode, PacketJetPack::handle);
        HANDLER.registerMessage(id++, PacketLaser.class, PacketLaser::encode, PacketLaser::decode, PacketLaser::handle);

        HANDLER.registerMessage(id++, PacketUIWidgetUpdate.class, PacketUIWidgetUpdate::encode, PacketUIWidgetUpdate::decode, PacketUIWidgetUpdate::handle);
        HANDLER.registerMessage(id++, PacketUIClientAction.class, PacketUIClientAction::encode, PacketUIClientAction::decode, PacketUIClientAction::handle);
        HANDLER.registerMessage(id++, PacketUIOpen.class, PacketUIOpen::encode, PacketUIOpen::decode, PacketUIOpen::handle);

        HANDLER.registerMessage(id++, FluidPipeMessage.class, FluidPipeMessage::encode, FluidPipeMessage::decode, FluidPipeMessage::handle);
        HANDLER.registerMessage(id++, ItemTransportMessage.class, ItemTransportMessage::encode, ItemTransportMessage::decode, ItemTransportMessage::handle);
    }

    public static void sendToNearby(World world, BlockPos pos, Object toSend) {
        if (world instanceof ServerWorld) {
            ServerWorld ws = (ServerWorld) world;

            ws.getChunkProvider().chunkManager.getTrackingPlayers(new ChunkPos(pos), false)
                    .filter(p -> p.getDistanceSq(pos.getX(), pos.getY(), pos.getZ()) < 64 * 64)
                    .forEach(p -> HANDLER.send(PacketDistributor.PLAYER.with(() -> p), toSend));
        }
    }

    public static void sendToNearby(World world, Entity e, Object toSend) {
        sendToNearby(world, e.getPosition(), toSend);
    }

    public static void sendTo(ServerPlayerEntity playerMP, Object toSend) {
        HANDLER.sendTo(toSend, playerMP.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendNonLocal(ServerPlayerEntity playerMP, Object toSend) {
        if (playerMP.server.isDedicatedServer() || !playerMP.getGameProfile().getName().equals(playerMP.server.getServerOwner())) {
            sendTo(playerMP, toSend);
        }
    }

    public static void sendInArea(World world, BlockPos pos, int radius, Object message) {
        HANDLER.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(
                pos.getX(),
                pos.getY(),
                pos.getZ(),
                radius,
                world.getDimensionKey()
        )), message);
    }

    public static void sendToServer(Object msg) {
        HANDLER.sendToServer(msg);
    }

}
