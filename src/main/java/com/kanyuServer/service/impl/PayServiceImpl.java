package com.kanyuServer.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kanyuServer.common.Result;
import com.kanyuServer.entity.*;
import com.kanyuServer.mapper.PayMapper;
import com.kanyuServer.service.*;
import com.kanyuServer.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Slf4j
@Service
public class PayServiceImpl extends ServiceImpl<PayMapper, Pay> implements PayService {

    @Resource
    GoodsService goodsService;
    @Resource
    UserWalletService userWalletService;
    @Resource
    OrderService orderService;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Override
    public Result pay(String orderId,Long goodsId,String password) {
            //1,校验支付密码是否正确
            UserWallet userWallet = userWalletService.query().eq("user_id", UserHolder.getUser().getId()).one();
            if (userWallet == null){
                return Result.fail("服务报错了",100009);
            }
            if (!userWallet.getPassword().equals(password)){
                return Result.fail("密码错误请重试",1000006);
            }
            //2，校验用户金额是否充足 对比实际支付金额
            Order order = orderService.query().eq("order_id", orderId).one();
        System.out.println(order.getOrderId());
        System.out.println(order.getCouponId());
            Goods goods = goodsService.query().eq("id", goodsId).one();
            Long result = userWalletService.queryByUserId(order.getUserId());
            if (result-order.getActualPrice()<0){
                return Result.fail("用户钱包金额不足，请充值之后再支付",1000009);
            }
//        3,扣减用户钱包
//        发送支付成功异步消息
        rabbitTemplate.convertAndSend("goods.order","goods.order.pay", JSONUtil.toJsonStr(order));

//        Long bigDecimal = userWalletService.updateBalance(order.getUserId(), order.getActualPrice(), 1);
//            //设置订单状态=2 已支付
//            orderService.update().eq("order_id",orderId).setSql("status=2").update();
//            //优惠券设置为2 已使用
//            Boolean isUpdate = couponOrderService.updateStatus(orderId, 2);
//            //用户积分增加 积分与金额一比一
//            Long points = order.getActualPrice();
//            pointsService.updatePoints(order.getUserId(),points);
//            //卖家收益记录
//            Earning earning = new Earning();
//            earning.setBuyerId(order.getUserId());
//            earning.setSellerId(goods.getUserId());
//            earning.setRevenueAmount(order.getActualPrice());
//            earning.setStatus(1);
//            earningService.createEarning(earning);
            return Result.ok("支付成功了");
        }

}
