package org.confluence.terraentity.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.confluence.terraentity.api.ILeftClickStateItem;
import org.confluence.terraentity.attachment.WeaponStorage;
import org.confluence.terraentity.entity.proj.YoyosEntity;
import org.confluence.terraentity.init.TEAttachments;
import org.confluence.terraentity.init.entity.TESummonEntities;

public class YoyosItem extends Item implements ILeftClickStateItem  {

    int stringColor;
    float attackDamage;
    public YoyosItem(Properties properties, float attackDamage, int stringColor) {
        super(properties);
        this.attackDamage = attackDamage;
        this.stringColor = stringColor;

    }

    public float getAttackDamage() {
        return attackDamage;
    }

    public int getStringColor() {
        return stringColor;
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return !player.isCreative();
    }

    @Override
    public void onLeftClick(Player player, ItemStack itemStack) {
        WeaponStorage weaponStorage = player.getData(TEAttachments.WEAPON_STORAGE.get());
        if(weaponStorage.yoyosEntity != null && weaponStorage.yoyosEntity.isAlive()){
            weaponStorage.yoyosEntity.onReceiveLeftClick(player, itemStack);
            return;
        }
        Level level = player.level();
        YoyosEntity<?> proj = TESummonEntities.YOYOS_ENTITY.get().create(level);
        if(proj!= null){

            weaponStorage.yoyosEntity = proj;
            proj.setPos(player.getX(), player.getY(0.5f), player.getZ());
            proj.summon_setOwnerUUID(player.getUUID());
            proj.setWeaponItem(itemStack);
            level.addFreshEntity(proj);
        }
    }

    @Override
    public void onLeftRelease(Player player, ItemStack itemStack) {
        WeaponStorage weaponStorage = player.getData(TEAttachments.WEAPON_STORAGE.get());
        if(weaponStorage.yoyosEntity != null && weaponStorage.yoyosEntity.isAlive()){
            weaponStorage.yoyosEntity.onReceiveLeftRelease(player, itemStack);
        }

    }

    @Override
    public boolean canSwitchWithoutRelease(Player player, ItemStack itemStack) {
        return false;
    }
}
