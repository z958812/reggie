package com.lp.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lp.reggie.entity.Orders;

public interface OrdersService extends IService<Orders> {
    void submit(Orders orders);
}
