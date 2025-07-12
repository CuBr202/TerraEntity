package org.confluence.terraentity.entity.animal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.confluence.terraentity.level.CustomExplodeCalculator;

public class BoomBunny extends Bunny{

    boolean exploded = false;
    public BoomBunny(EntityType<? extends Bunny> entityType, Level level) {
        super(entityType, level);

    }

    @Override
    public void die(DamageSource damageSource) {
        super.die(damageSource);
        this.explode();

    }

    private void explode(){
        if(exploded || level().isClientSide) return;
        this.exploded = true;
        level().explode(
                this,
                this.level().damageSources().explosion(this, this),
                new CustomExplodeCalculator() {
                    @Override
                    public float getEntityDamageAmount(Explosion explosion, Entity entity, double original) {
                        return (float) original;
                    }

                    @Override
                    public boolean shouldBlockExplode(Explosion explosion, BlockGetter reader, BlockPos pos, BlockState state, float power) {
                        return false;
                    }
                } ,
                this.getX(), this.getY(0.0625),this.getZ(),
                1, true, Level.ExplosionInteraction.MOB);
        this.kill();
    }

}
