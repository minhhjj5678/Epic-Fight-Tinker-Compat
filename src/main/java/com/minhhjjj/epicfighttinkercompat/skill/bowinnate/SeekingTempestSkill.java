package com.minhhjjj.epicfighttinkercompat.skill.bowinnate;

import com.minhhjjj.epicfighttinkercompat.gameasset.EFTAnimations;
import com.minhhjjj.epicfighttinkercompat.skill.PersistentWeaponInnateSkill;
import net.minecraft.network.FriendlyByteBuf;
import yesman.epicfight.skill.SkillBuilder;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;

public class SeekingTempestSkill extends PersistentWeaponInnateSkill {

    public SeekingTempestSkill(SkillBuilder<? extends WeaponInnateSkill> builder) {
        super(builder.setResource(Resource.COOLDOWN));
    }

    public void executeOnServer(SkillContainer container, FriendlyByteBuf args) {
        container.getExecutor().playAnimationSynchronized(EFTAnimations.SEEKING_TEMPEST, 0.0F);
        super.executeOnServer(container, args);
    }

}
