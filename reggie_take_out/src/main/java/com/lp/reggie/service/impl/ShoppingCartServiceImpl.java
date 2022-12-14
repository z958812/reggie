package com.lp.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lp.reggie.entity.ShoppingCart;
import com.lp.reggie.mapper.ShoppingCartMapper;
import com.lp.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @author lp
 * @date 2022-10-15 20:06
 * @description:购物车
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
