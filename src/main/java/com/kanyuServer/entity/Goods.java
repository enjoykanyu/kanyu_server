package com.kanyuServer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("goods_db")
public class Goods implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关联用户id
     */
    private Long userId;

    /**
     * 关联优惠券id
     */
    private Long couponId;

    /**
     * 资源文件url
     */
    private String resource;

    /**
     * 封面介绍url
     */
    private String coverImg;

    /**
     * 资源介绍url
     */
    private String IntroduceImg;

    /**
     * 资源商品文件描述
     */
    private String content;

    /**
     * 资源商品标题
     */
    private String title;

    /**
     * 下载所需金币数量
     */
    private Long price;

    /**
     * 下载量销量
     */
    private Integer sold;

    /**
     * 评论数量
     */
    private Integer comments;

    /**
     * 收藏数量
     */
    private Integer collect;

    /**
     * 点赞数量
     */
    private Integer liked;


    /**
     * 商品状态 0新建审核中 1审核通过 2审核拒绝
     */
    private Integer status;

    /**
     * 商品拒绝理由
     */
    private String rejectReason;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;



}
