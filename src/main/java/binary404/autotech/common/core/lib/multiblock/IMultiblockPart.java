package binary404.autotech.common.core.lib.multiblock;

public interface IMultiblockPart {

    boolean isAttachedToMultiBlock();

    void addToMultiBlock(MultiblockControllerBase controllerBase);

    void removeFromMultiBlock(MultiblockControllerBase controllerBase);

    MultiblockControllerBase getController();

}
