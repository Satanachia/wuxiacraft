package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.capabilities.SkillsProvider;
import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import com.airesnor.wuxiacraft.cultivation.skills.Skill;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SkillCapMessageHandler implements IMessageHandler {
    @Override
    public IMessage onMessage(IMessage message, MessageContext ctx) {
        if(ctx.side == Side.CLIENT) {
            if(message instanceof  SkillCapMessage) {
                Minecraft.getMinecraft().addScheduledTask(() -> {
                    SkillCapMessage scm = (SkillCapMessage)message;
                    EntityPlayer player = Minecraft.getMinecraft().player;
                    ISkillCap skillCap = player.getCapability(SkillsProvider.SKILL_CAP_CAPABILITY, null);
                    if(skillCap != null) {
                        for(Skill skill : scm.skillCap.getKnownSkills()) {
                            skillCap.addSkill(skill);
                        }
                        skillCap.stepCooldown(scm.skillCap.getCooldown());
                        skillCap.stepCastProgress(scm.skillCap.getCastProgress());
                    }
                });
            }
        }
        return null;
    }
}
