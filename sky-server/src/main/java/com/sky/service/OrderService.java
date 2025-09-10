package com.sky.service;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.vo.OrderSubmitVO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.vo.OrderPaymentVO;

public interface OrderService {
    /**
     * 用户下单
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付，调用微信支付生成预支付交易单
     * @param ordersPaymentDTO
     * @return OrderPaymentVO
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功回调后修改订单状态
     * @param outTradeNo 商户订单号
     */
    void paySuccess(String outTradeNo);
}
