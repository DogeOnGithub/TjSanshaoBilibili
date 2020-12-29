package top.tjsanshao.bilibili.data;

import lombok.Data;

/**
 * wallet
 *
 * @author TjSanshao
 * @date 2020-12-29 17:32
 */
@Data
class Wallet {
    private long mid;
    private int bcoin_balance;
    private int coupon_balance;
    private int coupon_due_time;
}
