package org.confluence.terraentity.api.entity;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.confluence.lib.color.GlobalColors;

/**
 * All bosses should implement this interface
 * <p>
 * 所有boss都应该实现这个接口
 */
public interface Boss extends Enemy, IDiscardWhenRespawnEntity{

    default boolean shouldShowMessage(){
        return isMainBody();
    }

    default boolean isMainBody(){
        return true;
    }

    default boolean shouldEnhanceMultiplayer(){
        return true;
    }

    interface BossPart extends Boss {
        @Override
        default boolean isMainBody(){
            return false;
        }
    }

    static void sendBossSpawnMessage(Entity entity){
        Level level = entity.level();
        if (entity instanceof Boss boss && !level.isClientSide){
            if (boss.shouldShowMessage()){
                Component mes = Component.translatable("message.terraentity.boss_spawn",
                        entity.getDisplayName()).withColor(GlobalColors.EVENT.get()).withStyle(ChatFormatting.BOLD);

                for (Player player : level.players()){
                    player.sendSystemMessage(mes);
                }
            }
        }
    }

    static void sendBossDeathMessage(Entity entity){
        Level level = entity.level();
        if (entity instanceof Boss boss && !level.isClientSide){
            if (boss.shouldShowMessage()){
                Component mes = Component.translatable("message.terraentity.boss_leave",
                            entity.getDisplayName()).withColor(GlobalColors.EVENT.get()).withStyle(ChatFormatting.BOLD);

                for (Player player : level.players()){
                    player.sendSystemMessage(mes);
                }
            }
        }
    }
}
