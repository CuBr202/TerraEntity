package org.confluence.terraentity.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.confluence.terraentity.entity.ai.Boss;
import org.confluence.terraentity.entity.monster.AbstractMonster;
import org.confluence.terraentity.entity.monster.demoneye.DemonEye;
import org.confluence.terraentity.entity.monster.demoneye.DemonEyeVariant;
import org.confluence.terraentity.entity.monster.slime.BaseSlime;
import org.confluence.terraentity.entity.monster.slime.BlackSlime;
import org.confluence.terraentity.entity.npc.trade.ITradeHolder;
import org.confluence.terraentity.entity.summon.ISummonMob;
import org.confluence.terraentity.init.*;
import org.confluence.terraentity.init.entity.TEMonsterEntities;
import org.confluence.terraentity.mixed.IPlayer;
import org.confluence.terraentity.utils.TEUtils;

import static org.confluence.terraentity.TerraEntity.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GameEntityEvent {
    @SubscribeEvent
    public static void entityJoinLevel(EntityJoinLevelEvent event) {
        // 生成信息
        Boss.sendBossSpawnMessage(event.getEntity());
        Level level = event.getLevel();
        if (event.loadedFromDisk() || !(level instanceof ServerLevel serverLevel)) return;


    }

    @SubscribeEvent
    public static void entityLeaveLevelEvent (EntityLeaveLevelEvent event) {
        if(event.getEntity() instanceof ServerPlayer player){
            // 清除召唤物
            player.getCapability(TEAttachments.SUMMONER_STORAGE).resolve().ifPresent(data->data.clear(player));
        }
    }

    @SubscribeEvent
    public static void playerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if(event.getEntity() instanceof ServerPlayer player) {
            // 同步召唤栏信息
            player.getCapability(TEAttachments.SUMMONER_STORAGE).resolve().ifPresent(data->data.sync(player));

            if(player.level().getEntities(player, player.getBoundingBox().inflate(32), e-> e instanceof Player && e!= player).isEmpty()){
                player.level().getEntities(player, player.getBoundingBox().inflate(32), e->e instanceof Boss).forEach(e->{
                    e.discard();
                });
            }

        }
    }

    @SubscribeEvent
    public static void entityDeathLevel(LivingDeathEvent event) {
        Level level = event.getEntity().level();
        Boss.sendBossDeathMessage(event.getEntity());
        if(event.getEntity() instanceof ServerPlayer player){
            player.getCapability(TEAttachments.SUMMONER_STORAGE).resolve().ifPresent(data->data.clear(player));
        }
    }

    @SubscribeEvent
    public static void livingDamageEntity(LivingDamageEvent event) {
        // LivingEntity e = (LivingEntity) event.getSource().getEntity();
        // Caused by: java.lang.ClassCastException: class net.minecraft.world.entity.projectile.Arrow cannot be cast to class net.minecraft.world.entity.LivingEntity
        LivingEntity e1 = event.getEntity();
        Level level = event.getEntity().level();
        Entity attacker = event.getSource().getEntity();
        if (!(level instanceof ServerLevel serverLevel)) return;
        if (attacker != null && attacker.getType() == TEMonsterEntities.DECAYEDER.get()) {
            if (!e1.hasEffect(TEEffects.DEMONIC_THOUGHTS.get())) {
                e1.addEffect(new MobEffectInstance(
                        TEEffects.DEMONIC_THOUGHTS.get(), 200
                ), attacker);
            } else {
                e1.removeEffect(TEEffects.DEMONIC_THOUGHTS.get());
                e1.hurt(event.getSource(), 6);
                AbstractMonster soulEater = TEMonsterEntities.EATER_OF_SOULS.get().create(level);
                if (soulEater != null) {
                    soulEater.setPos(e1.getEyePosition());
                    soulEater.setTarget(e1);
                    level.addFreshEntity(soulEater);
                }
                e1.removeEffect(TEEffects.DEMONIC_THOUGHTS.get());
            }
        }
        if (attacker != null && (attacker.getType() == TEMonsterEntities.CRIMSLIME.get() || attacker.getType() == TEMonsterEntities.CORRUPT_SLIME.get())) {
            if (e1.getRandom().nextFloat() <= 0.25f){
                e1.addEffect(new MobEffectInstance(
                        MobEffects.DARKNESS, 300
                ), attacker);
            }
        }
    }

    @SubscribeEvent
    public static void livingDamage$Pre(LivingDamageEvent event) {
        DamageSource damageSource = event.getSource();
        float amount = event.getAmount();
        LivingEntity hurter = event.getEntity();
        Entity attacker = event.getSource().getEntity();

        if (damageSource.is(TETags.DamageTypes.SUMMONER) || attacker instanceof ISummonMob<?> summoner) {
            // 召唤物集火伤害加成
            if (hurter.hasEffect(TEEffects.SUMMON_FOCUS.get())) {
                amount = amount + 2;

            }
            // 召唤物标记伤害增加
            if(attacker instanceof ISummonMob<?> summoner){
                LivingEntity owner = summoner.summon_getOwner();
                if(owner != null){
                    var att = owner.getAttribute(TEAttributes.MARK_DAMAGE.get());
                    if(att!= null){
                        double damage = att.getValue();
                        amount += (float) damage;
                    }
                }
            } else if (attacker instanceof LivingEntity owner) {
                var att = owner.getAttribute(TEAttributes.MARK_DAMAGE.get());
                if(att!= null){
                    double damage = att.getValue();
                    amount += (float) damage;
                }
            }
        }

        event.setAmount(amount);
    }

    @SubscribeEvent
    public static void entityInteract(PlayerInteractEvent.EntityInteract event){
        // 打开商店
        if (event.getTarget() instanceof ITradeHolder holder) {

            ((IPlayer) event.getEntity()).terra_entity$setTradeHolder(holder);
            return;
        }
        ItemStack item = event.getItemStack();
        if (!(event.getTarget() instanceof LivingEntity entity)) return;
        Player player = event.getEntity();
        Level level = event.getLevel();
//        if (entity.getType().equals(TEMonsterEntities.BLUE_SLIME.get()) ||
//                entity.getType().equals(TEMonsterEntities.GREEN_SLIME.get()) ||
//                entity.getType().equals(TEMonsterEntities.PURPLE_SLIME.get())) {
//            if (item.is(TETags.Items.HONEY_TRANSLATION_BUCKET)) {
//                HoneySlime slime = TEMonsterEntities.HONEY_SLIME.get().create(level);
//                if (slime != null) {
//                    item.shrink(1);
//                    player.addItem(new ItemStack(Items.BUCKET));
//                    slime.setSize(2, true);
//                    slime.setPos(entity.position());
//                    slime.setXRot(entity.getXRot());
//                    slime.setYRot(entity.getYRot());
//                    level.addFreshEntity(slime);
//                }
//                entity.remove(Entity.RemovalReason.DISCARDED);
//            } else if (item.is(TETags.Items.HONEY_TRANSLATION)) {
//                HoneySlime slime = TEMonsterEntities.HONEY_SLIME.get().create(level);
//                if (slime != null) {
//                    item.shrink(1);
//                    slime.setSize(2, true);
//                    slime.setPos(entity.position());
//                    slime.setXRot(entity.getXRot());
//                    slime.setYRot(entity.getYRot());
//                    level.addFreshEntity(slime);
//                }
//                entity.remove(Entity.RemovalReason.DISCARDED);
//            } else if (item.is(TETags.Items.HONEY_TRANSLATION_NOT_CONSUMED)) {
//                HoneySlime slime = TEMonsterEntities.HONEY_SLIME.get().create(level);
//                if (slime != null) {
//                    slime.setSize(2, true);
//                    slime.setPos(entity.position());
//                    slime.setXRot(entity.getXRot());
//                    slime.setYRot(entity.getYRot());
//                    level.addFreshEntity(slime);
//                }
//                entity.remove(Entity.RemovalReason.DISCARDED);
//                event.setCanceled(true);
//                return;
//            }
//            event.setCanceled(true);
//        }
    }

    @SubscribeEvent
    public static void mobFinalizeSpawn(MobSpawnEvent.FinalizeSpawn event) {
        Mob mob = event.getEntity();
        RandomSource randomSource = mob.getRandom();
        Level level = mob.level();
        if (mob instanceof DemonEye demonEye) {
            demonEye.setVariant(DemonEyeVariant.random(randomSource));
        } else if (mob instanceof BlackSlime blackSlime) {
            blackSlime.finalizeSpawn(randomSource, event.getDifficulty());
        }

        if (event.getEntity() instanceof Zombie zombie && !zombie.isBaby() && !zombie.isVehicle() && zombie.getRandom().nextFloat() < 0.05F) {
            BaseSlime slime = TEMonsterEntities.BLUE_SLIME.get().create(zombie.level());
            if (slime != null) {
//                level.addFreshEntity(slime);
                slime.moveTo(zombie.getX(), zombie.getY(), zombie.getZ(), zombie.getYRot(), 0.0F);
                slime.finalizeSpawn((ServerLevelAccessor) level, level.getCurrentDifficultyAt(zombie.blockPosition()), MobSpawnType.JOCKEY, null,null);
                slime.startRiding(zombie);
                TEUtils.monsterEnhance(slime);
            }
        }
        if(event.getEntity() instanceof Monster living && !(event.getEntity() instanceof ISummonMob<?>))
            TEUtils.monsterEnhance(living);
        else if(event.getEntity() instanceof Slime slime)
            TEUtils.monsterEnhance(slime);

    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)  {


    }

}
