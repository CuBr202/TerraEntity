package org.confluence.terraentity.api.entity;

/**
 * 受伤或再次生成时自动切换状态
 */
public interface IStateChangeableMob {

    /**
     * 当受伤或生成时触发
     */
    void changeState();

}
