package com.kanyuServer.dto;

import com.kanyuServer.entity.ChatContent;
import com.kanyuServer.entity.User;
import lombok.Data;

import java.util.List;

@Data
public class ChatContentDto {
    private ChatContent goodsId;
    private User sendUser;
    private User recieiveUser;
    private List<ChatContent> chatContents;
    private Integer notRead;
}
