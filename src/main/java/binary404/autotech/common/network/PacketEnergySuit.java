package binary404.autotech.common.network;

import binary404.autotech.client.util.ModSounds;
import binary404.autotech.common.item.ItemEnergySuit;
import binary404.autotech.common.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketEnergySuit {

    private int charge;

    public PacketEnergySuit(int charge) {
        this.charge = charge;
    }

    public static void encode(PacketEnergySuit msg, PacketBuffer buffer) {
        buffer.writeInt(msg.charge);
    }

    public static PacketEnergySuit decode(PacketBuffer buffer) {
        int charge = buffer.readInt();
        return new PacketEnergySuit(charge);
    }

    public static void handle(PacketEnergySuit msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player != null) {
                if (((ItemEnergySuit) ModItems.energy_leggings).getEnergyStored(player.inventory.armorItemInSlot(1)) >= (msg.charge * 10))
                    ((ItemEnergySuit) ModItems.energy_leggings).extractEnergy(player.inventory.armorItemInSlot(1), (int) (msg.charge * 10), false);
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
