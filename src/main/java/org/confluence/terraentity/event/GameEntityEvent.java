package org.confluence.terraentity.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import org.confluence.terraentity.entity.monster.slime.BaseSlime;
import org.confluence.terraentity.entity.monster.slime.BlackSlime;
import org.confluence.terraentity.init.ModEntities;

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
    }

    @SubscribeEvent
    public static void FinalizeSpawnRegister(FinalizeSpawnEvent event) {
        if(event.getEntity() instanceof BlackSlime entity){
            entity.finalizeSpawn(entity.getRandom(),event.getDifficulty());
        }

    }
}
