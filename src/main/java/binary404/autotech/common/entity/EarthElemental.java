package binary404.autotech.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.world.World;
import software.bernie.geckolib.animation.builder.AnimationBuilder;
import software.bernie.geckolib.animation.controller.AnimationController;
import software.bernie.geckolib.animation.controller.EntityAnimationController;
import software.bernie.geckolib.entity.IAnimatedEntity;
import software.bernie.geckolib.event.AnimationTestEvent;
import software.bernie.geckolib.manager.EntityAnimationManager;

public class EarthElemental extends MobEntity implements IAnimatedEntity {

    public EntityAnimationManager manager = new EntityAnimationManager();
    public AnimationController controller = new EntityAnimationController(this, "openController", 0f, this::playAnimation);

    private <ENTITY extends Entity & IAnimatedEntity> boolean playAnimation(AnimationTestEvent<ENTITY> event) {
        controller.setAnimation(new AnimationBuilder().addAnimation("animation.earth_elemental.open").addAnimation("animation.earth_elemental.open_stay", true));
        return true;
    }

    protected EarthElemental(EntityType<? extends MobEntity> type, World worldIn) {
        super(type, worldIn);
        manager.addAnimationController(controller);
    }

    @Override
    public EntityAnimationManager getAnimationManager() {
        return manager;
    }
}
