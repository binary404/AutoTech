// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.12.2 or 1.15.2 (same format for both) for entity models animated with GeckoLib
// Paste this class into your mod and follow the documentation for GeckoLib to use animations. You can find the documentation here: https://github.com/bernie-g/geckolib
// Blockbench plugin created by Gecko
package binary404.autotech.client.models;

import binary404.autotech.common.entity.EarthElemental;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib.animation.model.AnimatedEntityModel;
import software.bernie.geckolib.animation.render.AnimatedModelRenderer;

public class ModelEarthElemental extends AnimatedEntityModel<EarthElemental> {

    private final AnimatedModelRenderer base;
	private final AnimatedModelRenderer body;
	private final AnimatedModelRenderer body_2;
	private final AnimatedModelRenderer body_1;
	private final AnimatedModelRenderer shell_1;
	private final AnimatedModelRenderer left;
	private final AnimatedModelRenderer right;
	private final AnimatedModelRenderer front;
	private final AnimatedModelRenderer back;
	private final AnimatedModelRenderer head;

    public ModelEarthElemental()
    {
        textureWidth = 16;
    textureHeight = 16;
    base = new AnimatedModelRenderer(this);
		base.setRotationPoint(0.0F, 24.0F, 0.0F);
		
		base.setModelRendererName("base");
		this.registerModelRenderer(base);

		body = new AnimatedModelRenderer(this);
		body.setRotationPoint(0.0F, 0.0F, 0.0F);
		base.addChild(body);
		
		body.setModelRendererName("body");
		this.registerModelRenderer(body);

		body_2 = new AnimatedModelRenderer(this);
		body_2.setRotationPoint(0.0F, 0.0F, 0.0F);
		body.addChild(body_2);
		body_2.setTextureOffset(0, 0).addBox(-2.0F, -11.0F, -2.0F, 4.0F, 7.0F, 4.0F, 0.0F, false);
		body_2.setModelRendererName("body_2");
		this.registerModelRenderer(body_2);

		body_1 = new AnimatedModelRenderer(this);
		body_1.setRotationPoint(0.0F, 0.0F, 0.0F);
		body.addChild(body_1);
		body_1.setTextureOffset(0, 0).addBox(-3.0F, -11.0F, -3.0F, 6.0F, 10.0F, 6.0F, 0.0F, false);
		body_1.setModelRendererName("body_1");
		this.registerModelRenderer(body_1);

		shell_1 = new AnimatedModelRenderer(this);
		shell_1.setRotationPoint(0.0F, 0.0F, -3.0F);
		body.addChild(shell_1);
		
		shell_1.setModelRendererName("shell_1");
		this.registerModelRenderer(shell_1);

		left = new AnimatedModelRenderer(this);
		left.setRotationPoint(0.0F, 0.0F, 0.0F);
		shell_1.addChild(left);
		setRotationAngle(left, -1.5708F, 0.0F, 0.0F);
		left.setTextureOffset(0, 0).addBox(-6.0F, -1.0F, -12.0F, 12.0F, 1.0F, 12.0F, 0.0F, false);
		left.setTextureOffset(0, 0).addBox(-2.0F, 0.0F, -8.0F, 4.0F, 1.0F, 4.0F, 0.0F, false);
		left.setModelRendererName("left");
		this.registerModelRenderer(left);

		right = new AnimatedModelRenderer(this);
		right.setRotationPoint(0.0F, 0.0F, 6.0F);
		shell_1.addChild(right);
		setRotationAngle(right, 1.5708F, 0.0F, 0.0F);
		right.setTextureOffset(-32, 0).addBox(-6.0F, -1.0F, 0.0F, 12.0F, 1.0F, 12.0F, 0.0F, false);
		right.setTextureOffset(0, 0).addBox(-2.0F, 0.0F, 4.0F, 4.0F, 1.0F, 4.0F, 0.0F, false);
		right.setModelRendererName("right");
		this.registerModelRenderer(right);

		front = new AnimatedModelRenderer(this);
		front.setRotationPoint(3.0F, 0.0F, 3.0F);
		shell_1.addChild(front);
		setRotationAngle(front, 0.0F, 0.0F, -1.5708F);
		front.setTextureOffset(0, 0).addBox(0.0F, -1.0F, -6.0F, 12.0F, 1.0F, 12.0F, 0.0F, false);
		front.setTextureOffset(0, 0).addBox(4.0F, 0.0F, -2.0F, 4.0F, 1.0F, 4.0F, 0.0F, false);
		front.setModelRendererName("front");
		this.registerModelRenderer(front);

		back = new AnimatedModelRenderer(this);
		back.setRotationPoint(-3.0F, 0.0F, 3.0F);
		shell_1.addChild(back);
		setRotationAngle(back, 0.0F, 0.0F, 1.5708F);
		back.setTextureOffset(0, 0).addBox(-12.0F, -1.0F, -6.0F, 12.0F, 1.0F, 12.0F, 0.0F, false);
		back.setTextureOffset(0, 6).addBox(-8.0F, 0.0F, -2.0F, 4.0F, 1.0F, 4.0F, 0.0F, false);
		back.setModelRendererName("back");
		this.registerModelRenderer(back);

		head = new AnimatedModelRenderer(this);
		head.setRotationPoint(0.0F, -8.0F, 0.0F);
		body.addChild(head);
		head.setTextureOffset(0, 0).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, 0.0F, false);
		head.setModelRendererName("head");
		this.registerModelRenderer(head);

    this.rootBones.add(base);
  }


    @Override
    public ResourceLocation getAnimationFileLocation()
    {
        return new ResourceLocation("autotech", "animations/earth_elemental.json");
    }
}