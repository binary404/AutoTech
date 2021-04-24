package binary404.autotech.client.gui;

import binary404.autotech.client.gui.core.ModularUserInterface;
import binary404.autotech.common.tile.core.TileCore;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

public class TileEntityUIFactory extends UIFactory<TileCore> {

    public static final TileEntityUIFactory INSTANCE = new TileEntityUIFactory();

    private TileEntityUIFactory() {
    }

    @Override
    @SuppressWarnings("unchecked")
    protected ModularUserInterface createUITemplate(TileCore holder, PlayerEntity entityPlayer) {
        return holder.createUI(entityPlayer);
    }

    @Override
    protected TileCore readHolderFromSyncData(PacketBuffer syncData) {
        return (TileCore) Minecraft.getInstance().world.getTileEntity(syncData.readBlockPos());
    }

    @Override
    protected void writeHolderToSyncData(PacketBuffer syncData, TileCore holder) {
        syncData.writeBlockPos(holder.getPos());
    }

}
