package com.lp.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lp.reggie.common.BaseContext;
import com.lp.reggie.common.R;
import com.lp.reggie.entity.ShoppingCart;
import com.lp.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lp
 * @date 2022-10-15 20:09
 * @description:购物车
 */
@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    /*
     * @param ...data
     * @return List<ShoppingCart>
     * @description 根据用户id获取购物车中所有数据
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> getShoppingCart() {
        //获取用户id
        Long userId = BaseContext.getCurRentId();
//        根据userId查购物车信息
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(queryWrapper);
        return R.success(shoppingCarts);
    }

}
