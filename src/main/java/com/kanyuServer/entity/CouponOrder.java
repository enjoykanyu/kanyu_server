package com.kanyuServer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("coupon_order_db")
public class CouponOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;



    /**
     * 唯一订单id
     */
    private String orderId;

    /**
     * 下单的用户id
     */
    private Long userId;

    /**
     * 抢购的的优惠券id
     */
    private Long couponId;


    /**
     * 下单时间
     */
    private LocalDateTime createTime;


    /**
     * 核销时间用户使用了优惠券则更新该时间
     */
    private LocalDateTime useTime;

    /**
     * 订单状态，1：未使用；2：已使用；
     */
    private Integer status;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
