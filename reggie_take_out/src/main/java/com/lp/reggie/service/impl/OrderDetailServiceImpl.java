package com.lp.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lp.reggie.entity.OrderDetail;
import com.lp.reggie.mapper.OrderDetailMapper;
import com.lp.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @author lp
 * @date 2022-10-18 09:26
 * @description:订单详情
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
