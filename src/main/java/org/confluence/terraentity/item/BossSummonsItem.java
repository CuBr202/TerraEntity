package org.confluence.terraentity.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.api.event.TEBossEvent;
import org.confluence.terraentity.network.s2c.SummonBossPacket;
import org.confluence.terraentity.utils.AdapterUtils;
import org.confluence.terraentity.utils.TEUtils;

import java.util.function.*;

public class BossSummonsItem<T extends Mob> extends Item {
    float maxSummonRange = 15;
    float offsetY = 0;

    private final Supplier<EntityType<T>> entityType;
    private Predicate<Player> condition;
    private BiConsumer<Player, T> onSummon;
    private Function<Player, Vec3> summonPosFunc;

    public BossSummonsItem(Properties properties, Supplier<EntityType<T>> entityType) {
        super(properties);
        this.entityType = entityType;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if(usedHand == InteractionHand.OFF_HAND) return InteractionResultHolder.fail(player.getItemInHand(usedHand));
        if (level instanceof ServerLevel serverLevel && (condition == null || condition.test(player))) {
            EntityType<T> type = entityType.get();
            T mob = TEUtils.spawnEntity(type, serverLevel, summonPosFunc == null? this.getSummonPos(player, 0.5f) : summonPosFunc.apply(player));
            if (mob == null) {
                return InteractionResultHolder.fail(player.getItemInHand(usedHand));
            }

            TEBossEvent.Summon event = AdapterUtils.postEvent(new TEBossEvent.Summon(type,player));
            if(event.isCanceled()){
                return InteractionResultHolder.fail(player.getItemInHand(usedHand));
            }

            if (onSummon!= null) {
                onSummon.accept(player, mob);
            }

            if(event.shouldChangeCamera()){
                SummonBossPacket.sendTo((ServerPlayer) player, mob, event.getDistance());
            }
            if(!player.isCreative()) {
                player.getItemInHand(usedHand).shrink(1);
            }
            return InteractionResultHolder.consume(player.getItemInHand(usedHand));
        }
        return InteractionResultHolder.fail(player.getItemInHand(usedHand));
    }

    public EntityType<T> getEntityType() {
        return entityType.get();
    }

    public Vec3 getSummonPos(Player player, float partialTicks){
        // todo 可以用轮滚控制
        return TEUtils.getEyeVec3(player, this.maxSummonRange, partialTicks).add(0, offsetY, 0);
    }

    public boolean hasSpecificSummonPos(){
        return summonPosFunc!= null;
    }

    public BossSummonsItem<T> setCondition(Predicate<Player> condition) {
        this.condition = condition;
        return this;
    }

    public BossSummonsItem<T> setOnSummon(BiConsumer<Player, T> onSummon) {
        this.onSummon = onSummon;
        return this;
    }

    public BossSummonsItem<T> setSummonPosFunc(Function<Player, Vec3> summonPosFunc) {
        this.summonPosFunc = summonPosFunc;
        return this;
    }

    public BossSummonsItem<T> setMaxSummonRange(float maxSummonRange) {
        this.maxSummonRange = maxSummonRange;
        return this;
    }

    public BossSummonsItem<T> setOffsetY(float offsetY) {
        this.offsetY = offsetY;
        return this;
    }

}
