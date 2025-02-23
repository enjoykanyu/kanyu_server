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
@TableName("user_wallet_db") //用户钱包表 用户支付完成更新用户钱包金额
public class UserWallet implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;



    /**
     * 关联的用户id
     */
    private Long userId;


    /**
     * 新建时间
     */
    private LocalDateTime createTime;




    /**
     * 用户钱包金额
     */
    private Long balance;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


    /**
     * 支付密码
     */
    private String password;
}
