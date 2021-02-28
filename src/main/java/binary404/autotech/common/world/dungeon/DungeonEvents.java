package binary404.autotech.common.world.dungeon;

import binary404.autotech.client.util.ClientUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DungeonEvents {

    public static boolean MODIFIED_GAMERULE = false;
    public static boolean NATURAL_REGEN_OLD_VALUE = false;

    @SubscribeEvent
    public static void dungeonDeath(LivingDeathEvent event) {
        LivingEntity livingEntity = event.getEntityLiving();

        if (livingEntity.world.isRemote)
            return;

        if (livingEntity.world.getDimensionKey() != ModDimensions.DUNGEON_KEY)
            return;

        if (livingEntity instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) livingEntity;
            player.inventory.func_234564_a_(stack -> true, -1, player.container.func_234641_j_());
            player.openContainer.detectAndSendChanges();
            player.container.onCraftMatrixChanged(player.inventory);
            player.updateHeldItem();
        }
    }

    public static int remainingTicks;

    @SubscribeEvent
    public static void renderTimer(RenderGameOverlayEvent.Pre event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.POTION_ICONS)
            return;

        Minecraft minecraft = Minecraft.getInstance();

        boolean inDungeon = minecraft.world.getDimensionKey() == ModDimensions.DUNGEON_KEY;

        if (minecraft.world == null || (!inDungeon)) {
            return;
        }

        if (remainingTicks == 0)
            return;

        MatrixStack stack = event.getMatrixStack();
        int bottom = minecraft.getMainWindow().getScaledHeight();

        stack.push();
        stack.translate(10, bottom, 0);

        ClientUtil.drawStringWithBorder(stack,
                formatTimeString(),
                18, -12,
                remainingTicks % 10 < 5 ? 0xFF_FF0000 : 0xFF_FFFFFF, 0xFF_000000);

        stack.pop();
    }

    public static String formatTimeString() {
        long seconds = (remainingTicks / 20) % 60;
        long minutes = ((remainingTicks / 20) / 60) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.CLIENT)
            return;

        RegistryKey<World> dimensionKey = event.player.world.getDimensionKey();
        GameRules gameRules = event.player.world.getGameRules();

        if (MODIFIED_GAMERULE && dimensionKey != ModDimensions.DUNGEON_KEY) {
            gameRules.get(GameRules.NATURAL_REGENERATION).set(NATURAL_REGEN_OLD_VALUE, event.player.getServer());
            MODIFIED_GAMERULE = false;
            return;
        }

        if (dimensionKey != ModDimensions.DUNGEON_KEY)
            return;

        if (event.phase == TickEvent.Phase.START) {
            NATURAL_REGEN_OLD_VALUE = gameRules.getBoolean(GameRules.NATURAL_REGENERATION);
            gameRules.get(GameRules.NATURAL_REGENERATION).set(false, event.player.getServer());
            MODIFIED_GAMERULE = true;
        } else if (event.phase == TickEvent.Phase.END) {
            gameRules.get(GameRules.NATURAL_REGENERATION).set(NATURAL_REGEN_OLD_VALUE, event.player.getServer());
            MODIFIED_GAMERULE = false;
        }
    }

}
