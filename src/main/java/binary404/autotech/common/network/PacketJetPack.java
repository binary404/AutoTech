package binary404.autotech.common.network;

import binary404.autotech.client.util.ModSounds;
import binary404.autotech.common.item.ItemEnergySuit;
import binary404.autotech.common.item.ModItems;
import com.sun.javafx.geom.Vec3d;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketJetPack {

    private boolean forward, back, left, right, jump, sneak;

    public PacketJetPack(boolean f, boolean b, boolean l, boolean r, boolean j, boolean s) {
        this.forward = f;
        this.back = b;
        this.left = l;
        this.right = r;
        this.jump = j;
        this.sneak = s;
    }

    public static void encode(PacketJetPack msg, PacketBuffer buffer) {
        buffer.writeBoolean(msg.forward);
        buffer.writeBoolean(msg.back);
        buffer.writeBoolean(msg.left);
        buffer.writeBoolean(msg.right);
        buffer.writeBoolean(msg.jump);
        buffer.writeBoolean(msg.sneak);
    }

    public static PacketJetPack decode(PacketBuffer buffer) {
        return new PacketJetPack(buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean());
    }

    public static void handle(PacketJetPack msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player != null) {
                if (player.inventory.armorItemInSlot(2).getItem() == ModItems.jetpack) {
                    if (((ItemEnergySuit) ModItems.jetpack).getEnergyStored(player.inventory.armorItemInSlot(2)) >= 50) {
                        ((ItemEnergySuit) ModItems.jetpack).extractEnergy(player.inventory.armorItemInSlot(2), 50, false);
                        if (msg.jump) {
                            player.setMotion(player.getMotion().x, 1.2, player.getMotion().z);
                            player.connection.sendPacket(new SEntityVelocityPacket(player));
                        }

                        if (!player.onGround) {
                            float yaw = player.rotationYaw;
                            float pitch = 0.0F;

                            float f = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
                            float f1 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
                            float f2 = -MathHelper.cos(-pitch * 0.017453292F);
                            float f3 = MathHelper.sin(-pitch * 0.017453292F);
                            Vector3d vForward = new Vector3d((double) (f1 * f2), (double) f3, (double) (f * f2));

                            Vector3d vLeft = new Vector3d(vForward.z, vForward.y, -vForward.x);

                            double mX = 0.0D;
                            double mZ = 0.0D;

                            if (msg.forward) {
                                mX += vForward.x * 1.2;
                                mZ += vForward.z * 1.2;
                            }

                            if (msg.back) {
                                mX += vForward.x * -1.2;
                                mZ += vForward.z * -1.2;
                            }

                            if (msg.left) {
                                mX += vLeft.x * 1.2;
                                mZ += vLeft.z * 1.2;
                            }

                            if (msg.right) {
                                mX += vLeft.x * -1.2;
                                mZ += vLeft.z * -1.2;
                            }

                            player.setMotion(mX, player.getMotion().y, mZ);
                            player.connection.sendPacket(new SEntityVelocityPacket(player));
                        }
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
