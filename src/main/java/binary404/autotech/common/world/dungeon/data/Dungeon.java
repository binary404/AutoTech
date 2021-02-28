package binary404.autotech.common.world.dungeon.data;

import binary404.autotech.common.block.BlockPortal;
import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.network.PacketDungeonTick;
import binary404.autotech.common.network.PacketHandler;
import binary404.autotech.common.world.dungeon.DungeonSpawner;
import binary404.autotech.common.world.dungeon.ModDimensions;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.UUID;
import java.util.function.Consumer;

public class Dungeon implements INBTSerializable<CompoundNBT> {

    public static final int REGION_SIZE = 1 << 11;

    public static final PortalPlacer PORTAL_PLACER = new PortalPlacer((pos, random, facing) -> ModBlocks.portal.getDefaultState().with(BlockPortal.AXIS, facing.getAxis()), (pos, random, facing) -> {
        Block[] blocks = {
                Blocks.BLACKSTONE, Blocks.BLACKSTONE, Blocks.POLISHED_BLACKSTONE,
                Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS
        };

        return blocks[random.nextInt(blocks.length)].getDefaultState();
    });

    public UUID playerId;
    public MutableBoundingBox box;
    public int level;
    public int sTickLeft = 6000;
    public int ticksLeft = this.sTickLeft;

    public BlockPos start;
    public Direction facing;
    public boolean won;

    public DungeonSpawner spawner = new DungeonSpawner(this);
    public boolean finished = false;
    public int timer = 20 * 60;

    protected Dungeon() {

    }

    public Dungeon(UUID playerId, MutableBoundingBox box, int level) {
        this.playerId = playerId;
        this.box = box;
        this.level = level;
    }

    public UUID getPlayerId() {
        return this.playerId;
    }

    public boolean isComplete() {
        return this.ticksLeft <= 0 || this.finished;
    }

    public void tick(ServerWorld world) {
        if (this.finished)
            return;

        this.runIfPresent(world.getServer(), player -> {
            if (player.world.getDimensionKey() == ModDimensions.DUNGEON_KEY) {
                this.ticksLeft--;
            }

            this.syncTicksLeft(world.getServer());
        });

        if (this.ticksLeft <= 0) {
            if (this.won) {
                this.runIfPresent(world.getServer(), player -> {
                    this.teleportToStart(world, player);
                });
            } else {
                this.runIfPresent(world.getServer(), player -> {
                    player.sendMessage(new StringTextComponent("Dungeon Failed!").mergeStyle(TextFormatting.RED), this.playerId);
                    player.inventory.func_234564_a_(stack -> true, -1, player.container.func_234641_j_());
                    player.openContainer.detectAndSendChanges();
                    player.container.onCraftMatrixChanged(player.inventory);
                    player.updateHeldItem();

                    DamageSource source = new DamageSource("dungeonCollapse").setDamageBypassesArmor().setDamageAllowedInCreativeMode();
                    player.attackEntityFrom(source, Integer.MAX_VALUE);

                    this.finished = true;
                });
            }
        } else {
            this.runIfPresent(world.getServer(), player -> {
                if (this.ticksLeft + 20 < this.sTickLeft && player.world.getDimensionKey() != ModDimensions.DUNGEON_KEY) {
                    if (player.world.getDimensionKey() == World.OVERWORLD) {
                        this.finished = true;
                    } else {
                        this.ticksLeft = 1;
                    }
                } else {
                    this.spawner.tick(player);
                }
            });
        }

        this.timer--;
    }

    public boolean runIfPresent(MinecraftServer server, Consumer<ServerPlayerEntity> action) {
        if (server == null)
            return false;
        ServerPlayerEntity player = server.getPlayerList().getPlayerByUUID(this.playerId);
        if (player == null)
            return false;
        action.accept(player);
        return true;
    }

    public void syncTicksLeft(MinecraftServer server) {
        PacketHandler.runIfPresent(server, this.playerId, player -> {
           PacketHandler.sendTo(player, new PacketDungeonTick(this.ticksLeft));
        });
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putUniqueId("PlayerId", this.playerId);
        nbt.put("Box", this.box.toNBTTagIntArray());
        nbt.putInt("Level", this.level);
        nbt.putInt("StartTicksLeft", this.sTickLeft);
        nbt.putInt("TicksLeft", this.ticksLeft);
        nbt.putBoolean("Won", this.won);

        if (this.start != null) {
            CompoundNBT startNBT = new CompoundNBT();
            startNBT.putInt("x", this.start.getX());
            startNBT.putInt("y", this.start.getY());
            startNBT.putInt("z", this.start.getZ());
            nbt.put("Start", startNBT);
        }

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.playerId = nbt.getUniqueId("PlayerId");
        this.box = new MutableBoundingBox(nbt.getIntArray("Box"));
        this.level = nbt.getInt("Level");
        this.sTickLeft = nbt.getInt("StartTicksLeft");
        this.ticksLeft = nbt.getInt("TicksLeft");
        this.won = nbt.getBoolean("Won");

        if (nbt.contains("Start", Constants.NBT.TAG_COMPOUND)) {
            CompoundNBT startNbt = nbt.getCompound("Start");
            this.start = new BlockPos(startNbt.getInt("x"), startNbt.getInt("y"), startNbt.getInt("z"));
        }
    }

    public static Dungeon fromNBT(CompoundNBT nbt) {
        Dungeon dungeon = new Dungeon();
        dungeon.deserializeNBT(nbt);
        return dungeon;
    }

    public void teleportToStart(ServerWorld world, ServerPlayerEntity player) {
        if (this.start == null) {
            player.teleport(world, this.box.minX + this.box.getXSize() / 2.0F, 256, this.box.minZ + this.box.getZSize() / 2.0F, player.rotationYaw, player.rotationPitch);
            return;
        }

        player.teleport(world, this.start.getX() + 0.5D, this.start.getY() + 0.2D, this.start.getZ() + 0.5D, this.facing == null ? world.getRandom().nextFloat() * 360.0F : this.facing.rotateY().getHorizontalAngle(), 0.0F);

        player.setOnGround(true);
    }

    public void start(ServerWorld world, ServerPlayerEntity player, ChunkPos chunkPos) {
        loop:
        for (int x = -48; x < 48; x++) {
            for (int z = -48; z < 48; z++) {
                for (int y = 0; y < 48; y++) {
                    BlockPos pos = chunkPos.asBlockPos().add(x, 128 + y, z);
                    if (world.getBlockState(pos).getBlock() != Blocks.CRIMSON_PRESSURE_PLATE)
                        continue;
                    world.setBlockState(pos, Blocks.AIR.getDefaultState());

                    this.start = pos;

                    for (Direction direction : Direction.Plane.HORIZONTAL) {
                        int count = 1;
                        while (world.getBlockState(pos.offset(direction, count)).getBlock() == Blocks.WARPED_PRESSURE_PLATE) {
                            world.setBlockState(pos.offset(direction, count), Blocks.AIR.getDefaultState());
                            count++;
                        }

                        if (count != 1) {
                            this.facing = direction;
                            PORTAL_PLACER.place(world, pos, this.facing, count, count + 1);
                            break loop;
                        }
                    }
                }
            }
        }

        this.teleportToStart(world, player);
        player.func_242279_ag();
    }
}
