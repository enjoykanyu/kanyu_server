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
@Accessors(chain = true)//支付记录表相关 用户支付则产生记录表 用户退款则产生退款表
@TableName("pay_db")
public class Pay implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 支付密码
     */
    private String password;

    /**
     * 关联订单id
     */
    private String orderId;

    /**
     * 支付金额
     */
    private Long price;



    /**
     * 支付时间
     */
    private LocalDateTime createTime;





    /**
     * 支付状态，1：未支付；2：已支付；
     */
    private Integer status;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
