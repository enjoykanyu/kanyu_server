package com.kanyuServer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kanyuServer.common.Result;
import com.kanyuServer.entity.ChatContent;

import javax.servlet.http.HttpSession;

public interface ChatService extends IService<ChatContent> {

    Result oneConent(Long sendUserId, Long receiveUserId, HttpSession session);
}
