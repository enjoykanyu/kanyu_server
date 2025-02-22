package com.kanyuServer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("earning_db") //收益记录表只有创造了创意商品的用户才会有
public class Earning implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 关联订单id
     */
    private String orderId;

    /**
     * 关联商品id
     */
    private Long goodsId;

    /**
     * 买家的用户id
     */
    private Long buyerId;
    /**
     * 卖家的用户id
     */
    private Long sellerId;

    /**
     * 收益金额
     */
    private BigDecimal RevenueAmount;

    /**
     * 收益状态 0 待结算 1 已结算 2 已退款
     */
    private Integer status;


    /**
     * 新建时间
     */
    private LocalDateTime createTime;

    /**
     * 结算时间
     */
    private LocalDateTime settleTime;


    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
