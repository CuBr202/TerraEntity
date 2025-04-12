package org.confluence.terraentity.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.init.block.TEFigureBlocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FigureBlock extends BaseEntityBlock {
    ResourceLocation entityType;
    float scale;

    public FigureBlock(ResourceLocation entityType, Properties properties) {
        this(entityType, 1, properties);
    }

    public FigureBlock(ResourceLocation entityType, float scale, Properties properties) {
        super(properties);
        this.entityType = entityType;
        this.scale = scale;
    }



    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand pHand,BlockHitResult hitResult) {
        FigureBlockEntity blockEntity = (FigureBlockEntity) level.getBlockEntity(pos);
        if (blockEntity != null) {
            blockEntity.turnOn = !blockEntity.turnOn;
        }
        player.swing(InteractionHand.MAIN_HAND);
        return InteractionResult.PASS;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        FigureBlockEntity blockEntity = new FigureBlockEntity(blockPos, blockState);
        return blockEntity;
    }


    @Override
    public <T extends BlockEntity> BlockEntityTicker getTicker(@NotNull Level pLevel, @NotNull BlockState pState, @NotNull BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, TEFigureBlocks.FIGURE_BLOCK_ENTITY.get(), (level, pos, state, blockEntity) -> {
            if (blockEntity.entity != null) {
                if (blockEntity.turnOn)
                    ++blockEntity.ticks;
                blockEntity.entity.tickCount = blockEntity.ticks;
            } else {
                blockEntity.entity = BuiltInRegistries.ENTITY_TYPE.get(entityType).create(level);
            }
            if (!level.isClientSide && blockEntity.ticks % 20 == 0) {

//                level.sendBlockUpdated(pos, state, state, 2);
                blockEntity.setChanged();
            }
        });
    }

    public static class FigureBlockEntity extends BlockEntity {
        public Entity entity;
        public int ticks;
        public boolean turnOn = false;
        public ResourceLocation entityType;
        public float scale = 1;

        public FigureBlockEntity(BlockPos pos, BlockState blockState) {
            super(TEFigureBlocks.FIGURE_BLOCK_ENTITY.get(), pos, blockState);
            FigureBlock block = (FigureBlock)blockState.getBlock();
            scale = block.scale;
            this.entityType = block.entityType;
        }

        @Override
        protected void saveAdditional(CompoundTag tag) {
            super.saveAdditional(tag);
            tag.putInt("ticks", ticks);
            tag.putBoolean("turnOn", turnOn);
            tag.putString("entity_type", entityType.toString());
        }

        @Override
        public void load(CompoundTag tag) {
            super.load(tag);
            ticks = tag.getInt("ticks");
            turnOn = tag.getBoolean("turnOn");
            entityType = TerraEntity.parse(tag.getString("entity_type"));

//            if(entity == null){
//                if (this.level != null) {
//                    entity = BuiltInRegistries.ENTITY_TYPE.get(entityType).create(this.level);
//                }
//            }
//            if(entity != null){
//                entity.tickCount = ticks;
//            }
//            if (this.level != null) {
//                this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 2);
//            }
        }

        @Override
        public Packet<ClientGamePacketListener> getUpdatePacket() {
            return ClientboundBlockEntityDataPacket.create(this);
        }

        @Override
        public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
            CompoundTag tag = pkt.getTag();
            ticks = tag.getInt("ticks");
            turnOn = tag.getBoolean("turnOn");
            entityType = TerraEntity.parse(tag.getString("entity_type"));
//            if(entity == null){
//                if (this.level != null) {
//                    entity = BuiltInRegistries.ENTITY_TYPE.get(entityType).create(this.level);
//                }
//            }
//            if(entity != null){
//                entity.tickCount = ticks;
//            }
        }

        @Override
        public CompoundTag getUpdateTag() {
            CompoundTag tag = super.getUpdateTag();
            tag.putInt("ticks", ticks);
            tag.putBoolean("turnOn", turnOn);
            tag.putString("entity_type", entityType.toString());
            return tag;
        }
    }
}
