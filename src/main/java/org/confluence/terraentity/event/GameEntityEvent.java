package org.confluence.terraentity.event;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import org.confluence.terraentity.entity.ai.Boss;
import org.confluence.terraentity.entity.monster.slime.BaseSlime;
import org.confluence.terraentity.entity.monster.slime.BlackSlime;
import org.confluence.terraentity.entity.util.DeathAnimOptions;
import org.confluence.terraentity.init.ModEntities;
import org.confluence.terraentity.utils.FloatRGB;

import static org.confluence.terraentity.TerraEntity.MODID;

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.GAME)
public class GameEntityEvent {
    @SubscribeEvent
    public static void entityJoinLevel(EntityJoinLevelEvent event) {
        Level level = event.getLevel();
        if (event.loadedFromDisk() || !(level instanceof ServerLevel serverLevel)) return;
        if (event.getEntity() instanceof Zombie zombie && !zombie.isBaby() && !zombie.isVehicle() && zombie.getRandom().nextFloat() < 0.05F) {
            BaseSlime slime = ModEntities.BLUE_SLIME.get().create(level);
            if (slime != null) {
                slime.moveTo(zombie.getX(), zombie.getY(), zombie.getZ(), zombie.getYRot(), 0.0F);
                slime.finalizeSpawn(serverLevel, level.getCurrentDifficultyAt(zombie.blockPosition()), MobSpawnType.JOCKEY, null);
                slime.startRiding(zombie);
                level.addFreshEntity(slime);
            }
        }
        if (event.getEntity() instanceof Boss boss){
            if (boss.shouldShowMessage()){
                Component mes;
                FloatRGB color;
                if (event.getEntity() instanceof DeathAnimOptions dao){
                    float[] _color = dao.getBloodColor();
                    color = new FloatRGB(_color[0], _color[1], _color[2]);
                } else {
                    color = new FloatRGB(0.7F, 0, 0);
                }
                if (event.getEntity().getCustomName() != null){
                    mes = Component.translatable("message.terraentity.boss_spawn",
                            event.getEntity().getCustomName().getString()).withColor(color.get()).withStyle(ChatFormatting.BOLD);
                } else {
                    mes = Component.translatable("message.terraentity.boss_spawn",
                            event.getEntity().getName().getString()).withColor(color.get()).withStyle(ChatFormatting.BOLD);
                }
                for (Player player : level.players()){
                    player.sendSystemMessage(mes);
                }
            }
        }
    }

    @SubscribeEvent
    public static void FinalizeSpawnRegister(FinalizeSpawnEvent event) {
        if(event.getEntity() instanceof BlackSlime entity){
            entity.finalizeSpawn(entity.getRandom(),event.getDifficulty());
        }

    }
    @SubscribeEvent
    public static void FinalizeSpawnRegister(LivingEvent.LivingJumpEvent event) {

    }

    @SubscribeEvent
    public static void entityDeathLevel(LivingDeathEvent event) {
        Level level = event.getEntity().level();
        if (event.getEntity() instanceof Boss boss){
            if (boss.shouldShowMessage()){
                Component mes;
                FloatRGB color;
                if (event.getEntity() instanceof DeathAnimOptions dao){
                    float[] _color = dao.getBloodColor();
                    color = new FloatRGB(_color[0], _color[1], _color[2]);
                } else {
                    color = new FloatRGB(0.7F, 0, 0);
                }
                if (event.getEntity().getCustomName() != null){
                    mes = Component.translatable("message.terraentity.boss_leave",
                            event.getEntity().getCustomName().getString()).withColor(color.get()).withStyle(ChatFormatting.BOLD);
                } else {
                    mes = Component.translatable("message.terraentity.boss_leave",
                            event.getEntity().getName().getString()).withColor(color.get()).withStyle(ChatFormatting.BOLD);
                }
                for (Player player : level.players()){
                    player.sendSystemMessage(mes);   //todo 报两次
                }
            }
        }
    }
}
