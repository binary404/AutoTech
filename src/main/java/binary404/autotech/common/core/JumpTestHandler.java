package binary404.autotech.common.core;

import binary404.autotech.AutoTech;
import binary404.autotech.client.fx.LaserRenderHandler;
import binary404.autotech.client.fx.LaserRenderHelper;
import binary404.autotech.client.gui.core.Texture;
import binary404.autotech.client.util.ModSounds;
import binary404.autotech.common.item.ItemEnergySuit;
import binary404.autotech.common.item.ModItems;
import binary404.autotech.common.network.PacketHandler;
import binary404.autotech.common.network.PacketEnergySuit;
import binary404.autotech.common.network.PacketJetPack;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = AutoTech.modid, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class JumpTestHandler {

    public static float charge = 0;
    public static int setting = 0;

    @SubscribeEvent
    public static void renderCharge(RenderGameOverlayEvent.Pre event) {
        if (Minecraft.getInstance().player.inventory.armorItemInSlot(1).getItem() == ModItems.energy_leggings) {
            MatrixStack stack = event.getMatrixStack();
            if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
                stack.push();
                stack.push();
                stack.scale(2.1F, 2.1F, 2.1F);
                stack.translate(0, 0, 10);
                Texture.ENERGY_HUD.draw(stack, 1, 1);
                stack.pop();
                stack.push();
                stack.translate(0.3, -0.1, 0);
                Minecraft.getInstance().fontRenderer.drawString(stack, String.valueOf((int) charge), 4, 6, 0xFFFFFF);
                stack.translate(0, 0.7, 0);
                Minecraft.getInstance().fontRenderer.drawString(stack, String.valueOf(setting), 4, 15, 0xFFFFFF);
                stack.pop();
                stack.pop();
            }
        }
    }

    @SubscribeEvent
    public static void jump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            if (setting > 0 && ((PlayerEntity) event.getEntityLiving()).inventory.armorItemInSlot(1).getItem() == ModItems.energy_leggings) {
                if (((ItemEnergySuit) ModItems.energy_leggings).getEnergyStored(Minecraft.getInstance().player.inventory.armorItemInSlot(1)) >= setting * 10 * 10) {
                    event.getEntityLiving().setMotion(event.getEntityLiving().getMotion().x, setting * 0.56, event.getEntityLiving().getMotion().z);
                    event.getEntityLiving().playSound(ModSounds.piston, 6.0F, 1.0F);
                    PacketHandler.sendToServer(new PacketEnergySuit(setting * 10));
                }
            }
        }
    }

    @SubscribeEvent
    public static void keyboardEvent(InputEvent.KeyInputEvent event) {
        if (Minecraft.getInstance().player != null) {
            if (Minecraft.getInstance().player.inventory.armorItemInSlot(1).getItem() == ModItems.energy_leggings) {
                if (super_jump_level.isPressed()) {
                    if (setting <= 1) {
                        setting++;
                    } else {
                        setting = 0;
                    }
                }
                if (super_jump.isKeyDown() && charge <= 30) {
                    charge += 0.3;
                    return;
                }
                if (charge > 0 && super_jump.isPressed()) {
                    PlayerEntity playerEntity = Minecraft.getInstance().player;
                    if (playerEntity.onGround) {
                        if (((ItemEnergySuit) ModItems.energy_leggings).getEnergyStored(playerEntity.inventory.armorItemInSlot(1)) >= charge * 10) {
                            playerEntity.setMotion(playerEntity.getMotion().x, charge * 0.1, playerEntity.getMotion().z);
                            playerEntity.playSound(ModSounds.piston, 6.0F, 1.0F);
                            PacketHandler.sendToServer(new PacketEnergySuit((int) charge));
                            charge = 0;
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void tick(TickEvent.ClientTickEvent event) {
        boolean f = Minecraft.getInstance().gameSettings.keyBindForward.isKeyDown();
        boolean b = Minecraft.getInstance().gameSettings.keyBindBack.isKeyDown();
        boolean l = Minecraft.getInstance().gameSettings.keyBindLeft.isKeyDown();
        boolean r = Minecraft.getInstance().gameSettings.keyBindRight.isKeyDown();
        boolean j = Minecraft.getInstance().gameSettings.keyBindJump.isKeyDown();
        boolean s = Minecraft.getInstance().gameSettings.keyBindSneak.isKeyDown();

        if (f || b || l || r || j || s) {
            PacketHandler.sendToServer(new PacketJetPack(f, b, l, r, j, s));
        }
    }

    @SubscribeEvent
    public static void fallDamage(LivingDamageEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity && event.getSource().damageType.equals("fall")) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            ItemStack boots = player.inventory.armorItemInSlot(0);
            if (((ItemEnergySuit) ModItems.energy_boots).getEnergyStored(boots) >= event.getAmount() * 100) {
                ((ItemEnergySuit) ModItems.energy_boots).extractEnergy(boots, (int) (event.getAmount() * 100), false);
                event.setAmount(0F);
                event.setCanceled(true);
            }
        }
    }

    public static final KeyBinding super_jump = new KeyBinding("autotech.super_jump", KeyConflictContext.UNIVERSAL, InputMappings.getInputByCode(GLFW.GLFW_KEY_G, 0), "autotech.keybindings");
    public static final KeyBinding super_jump_level = new KeyBinding("autotech.super_jump_level", KeyConflictContext.UNIVERSAL, InputMappings.getInputByCode(GLFW.GLFW_KEY_H, 0), "autotech.keybindings");

    public static void init() {
        ClientRegistry.registerKeyBinding(super_jump_level);
        ClientRegistry.registerKeyBinding(super_jump);
    }

}
