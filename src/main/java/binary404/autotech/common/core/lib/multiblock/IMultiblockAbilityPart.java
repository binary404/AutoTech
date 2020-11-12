package binary404.autotech.common.core.lib.multiblock;

import java.util.List;

public interface IMultiblockAbilityPart<T> extends IMultiblockPart {

    MultiblockAbility<T> getAbility();

    void registerAbilities(List<T> abilityList);

}
