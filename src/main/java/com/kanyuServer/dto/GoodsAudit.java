package com.kanyuServer.dto;

import lombok.Data;

@Data
public class GoodsAudit {
    private Integer goodsId;
    private Integer auditStatus;
    private String rejectReason;
}
