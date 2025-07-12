package org.confluence.terraentity.entity.ai.goal.skill;

import java.util.*;

public class SkillCooldownManager implements ISkillManager{

    private final HashMap<Integer, ISkill> cooldownMap = new HashMap<>(); // 技能名 -> 剩余冷却时间
    private final PriorityQueue<ISkill> cooldownQueue = new PriorityQueue<>(Comparator.comparing(ISkill::getCooldown));
    private final Queue<ISkill> availableSkills = new LinkedList<>(); // 可用的技能列表


    /**
     * 获取剩余冷却时间最短的技能
     */
    public ISkill getNextAvailableSkill() {
        return cooldownQueue.peek();
    }

    @Override
    public void addSkill(ISkill skill) {
        cooldownMap.put(skill.getIndex(), skill);
        cooldownQueue.add(skill);
    }

    @Override
    public void triggerSkill(ISkill skill) {
        if(canTriggerSkill(skill)){
            this.availableSkills.poll();
            skill.reset();
            addSkill(skill);
        }
    }

    @Override
    public void update(int deltaTime) {
        cooldownMap.forEach((k,v)->v.update(deltaTime));
        cooldownMap.entrySet().removeIf(entry -> entry.getValue().getCooldown() <= 0);

        while (!cooldownQueue.isEmpty() &&
               (!cooldownMap.containsKey(cooldownQueue.peek().getIndex()) || cooldownQueue.peek()!= null &&
                cooldownQueue.peek().getCooldown() <= 0)) {
            this.availableSkills.add(cooldownQueue.poll());
        }

    }

    @Override
    public String toString(){
        StringBuilder log = new StringBuilder();
        for (ISkill skill : cooldownQueue) {
            log.append(String.format(skill.getIndex() + ":" + skill.getCooldown() + " ; "));
        }
        return log.toString();
    }

    @Override
    public boolean canTriggerSkill(ISkill skill) {
        return !this.availableSkills.isEmpty() && this.availableSkills.peek() == skill;
    }
}