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
@TableName("points_db") //用户积分表 用户支付完成才会更新用户积分 用户可以根据订单来知道
public class Points implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;



    /**
     * 用户id
     */
    private Long userId;



    /**
     * 新建时间
     */
    private LocalDateTime createTime;




    /**
     * 用户积分
     */
    private Integer points;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
