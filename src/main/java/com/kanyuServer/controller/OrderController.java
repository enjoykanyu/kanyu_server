package com.kanyuServer.controller;

import com.kanyuServer.common.Result;
import com.kanyuServer.entity.Goods;
import com.kanyuServer.entity.User;
import com.kanyuServer.service.AdminGoodsService;
import com.kanyuServer.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

;

/**
 * 文件资源控制器
 */
@Slf4j
@RestController
@RequestMapping("/order")
@CrossOrigin(origins = "http://localhost:5177")
public class OrderController {

}
