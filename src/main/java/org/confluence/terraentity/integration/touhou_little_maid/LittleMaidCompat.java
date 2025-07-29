package org.confluence.terraentity.integration.touhou_little_maid;


import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskAttack;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskBowAttack;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskCrossBowAttack;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import org.confluence.terraentity.integration.touhou_little_maid.task_boomerang.TaskBoomerangAttack;
import org.confluence.terraentity.integration.touhou_little_maid.task_boomerang.TaskDistributor;

import java.util.List;

/**
 * 此对象会在女仆模组运行时自动实例化并加载
 */
@LittleMaidExtension
public class LittleMaidCompat implements ILittleMaid {
    @Override
    public void addMaidTask(TaskManager manager) {
        manager.add(new TaskBoomerangAttack());
//        manager.add(new TaskDistributor(List.of(
//                new TaskBoomerangAttack(),
//                new TaskBowAttack(),
//                new TaskCrossBowAttack(),
//                new TaskAttack()
//        )));
    }
}
