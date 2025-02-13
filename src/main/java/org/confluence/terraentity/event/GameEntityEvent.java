package org.confluence.terraentity.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.confluence.terraentity.entity.ai.Boss;
import org.confluence.terraentity.entity.monster.AbstractMonster;
import org.confluence.terraentity.entity.monster.Decayeder;
import org.confluence.terraentity.entity.monster.demoneye.DemonEye;
import org.confluence.terraentity.entity.monster.demoneye.DemonEyeVariant;
import org.confluence.terraentity.entity.monster.slime.BaseSlime;
import org.confluence.terraentity.entity.monster.slime.BlackSlime;
import org.confluence.terraentity.entity.monster.slime.HoneySlime;
import org.confluence.terraentity.init.TEAttachments;
import org.confluence.terraentity.init.TEEffects;
import org.confluence.terraentity.init.TEEntities;
import org.confluence.terraentity.init.TETags;
import org.confluence.terraentity.utils.TEUtils;

import static org.confluence.terraentity.TerraEntity.MODID;

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.GAME)
public class GameEntityEvent {
    @SubscribeEvent
    public static void entityJoinLevel(EntityJoinLevelEvent event) {
        Level level = event.getLevel();
        if (event.loadedFromDisk() || !(level instanceof ServerLevel serverLevel)) return;
        if (event.getEntity() instanceof Zombie zombie && !zombie.isBaby() && !zombie.isVehicle() && zombie.getRandom().nextFloat() < 0.05F) {
            BaseSlime slime = TEEntities.BLUE_SLIME.get().create(level);
            if (slime != null) {
                slime.moveTo(zombie.getX(), zombie.getY(), zombie.getZ(), zombie.getYRot(), 0.0F);
                slime.finalizeSpawn(serverLevel, level.getCurrentDifficultyAt(zombie.blockPosition()), MobSpawnType.JOCKEY, null);
                slime.startRiding(zombie);
                level.addFreshEntity(slime);
            }
        }
        if(event.getEntity() instanceof Monster living)
            TEUtils.monsterEnhance(living);
        else if(event.getEntity() instanceof Slime slime)
            TEUtils.monsterEnhance(slime);
        // 生成信息
        Boss.sendBossSpawnMessage(event.getEntity());
        if(event.getEntity() instanceof ServerPlayer player){
            // debug
//            player.getInventory().add(TEItems.SLIME_STAFF.toStack());
        }
    }

    @SubscribeEvent
    public static void entityLeaveLevelEvent (EntityLeaveLevelEvent event) {
        if(event.getEntity() instanceof ServerPlayer player){
            // 清除召唤物
            player.getData(TEAttachments.SUMMONER_STORAGE.get()).clear(player);
        }
    }

    @SubscribeEvent
    public static void playerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if(event.getEntity() instanceof ServerPlayer player) {
            // 同步召唤栏信息
            player.getData(TEAttachments.SUMMONER_STORAGE.get()).sync(player);
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
        Boss.sendBossDeathMessage(event.getEntity());
        if(event.getEntity() instanceof ServerPlayer player){
            player.getData(TEAttachments.SUMMONER_STORAGE.get()).clear(player);
        }
    }

    @SubscribeEvent
    public static void livingDamageEntity(LivingDamageEvent.Post event) {
        // LivingEntity e = (LivingEntity) event.getSource().getEntity();
        // Caused by: java.lang.ClassCastException: class net.minecraft.world.entity.projectile.Arrow cannot be cast to class net.minecraft.world.entity.LivingEntity
        LivingEntity e1 = event.getEntity();
        Level level = event.getEntity().level();
        if (!(level instanceof ServerLevel serverLevel)) return;
        if (event.getSource().getEntity() instanceof Decayeder dp){
            if (!e1.hasEffect(TEEffects.DEMONIC_THOUGHTS)){
                e1.addEffect(new MobEffectInstance(
                        TEEffects.DEMONIC_THOUGHTS, 200
                ), dp);
            } else {
                e1.removeEffect(TEEffects.DEMONIC_THOUGHTS);
                e1.hurt(event.getSource(), 6);
                AbstractMonster soulEater = TEEntities.EATER_OF_SOULS.get().create(level);
                if (soulEater!=null) {
                    soulEater.setPos(e1.getEyePosition());
                    soulEater.setTarget(e1);
                    level.addFreshEntity(soulEater);
                }
                e1.removeEffect(TEEffects.DEMONIC_THOUGHTS);
            }
        }
    }

    @SubscribeEvent
    public static void entityInteract(PlayerInteractEvent.EntityInteract event){
        ItemStack item = event.getItemStack();
        if(!(event.getTarget() instanceof LivingEntity entity)) return;
        Player player = event.getEntity();
        Level level = event.getLevel();
        if (    entity.getType().equals(TEEntities.BLUE_SLIME.get()) ||
                entity.getType().equals(TEEntities.GREEN_SLIME.get()) ||
                entity.getType().equals(TEEntities.PURPLE_SLIME.get())){
            if (item.is(TETags.Items.HONEY_TRANSLATION_BUCKET)){
                HoneySlime slime = TEEntities.HONEY_SLIME.get().create(level);
                if (slime!=null) {
                    item.shrink(1);
                    player.addItem(new ItemStack(Items.BUCKET));
                    slime.setSize(2, true);
                    slime.setPos(entity.position());
                    slime.setXRot(entity.getXRot());
                    slime.setYRot(entity.getYRot());
                    level.addFreshEntity(slime);
                }
                entity.remove(Entity.RemovalReason.DISCARDED);
            } else if (item.is(TETags.Items.HONEY_TRANSLATION)){
                HoneySlime slime = TEEntities.HONEY_SLIME.get().create(level);
                if (slime!=null) {
                    item.shrink(1);
                    slime.setSize(2, true);
                    slime.setPos(entity.position());
                    slime.setXRot(entity.getXRot());
                    slime.setYRot(entity.getYRot());
                    level.addFreshEntity(slime);
                }
                entity.remove(Entity.RemovalReason.DISCARDED);
            } else if (item.is(TETags.Items.HONEY_TRANSLATION_NOT_CONSUMED)){
                HoneySlime slime = TEEntities.HONEY_SLIME.get().create(level);
                if(slime!=null) {
                    slime.setSize(2, true);
                    slime.setPos(entity.position());
                    slime.setXRot(entity.getXRot());
                    slime.setYRot(entity.getYRot());
                    level.addFreshEntity(slime);
                }
                entity.remove(Entity.RemovalReason.DISCARDED);
                event.setCanceled(true);
                return;
            }
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void mobFinalizeSpawn(FinalizeSpawnEvent event) {
        Mob mob = event.getEntity();
        RandomSource randomSource = mob.getRandom();
        if (mob instanceof DemonEye demonEye) {
            demonEye.setVariant(DemonEyeVariant.random(randomSource));
        } else if (mob instanceof BlackSlime blackSlime) {
            blackSlime.finalizeSpawn(randomSource, event.getDifficulty());
        }
    }

}
