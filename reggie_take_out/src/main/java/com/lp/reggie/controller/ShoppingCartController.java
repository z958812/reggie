package com.lp.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lp.reggie.common.BaseContext;
import com.lp.reggie.common.R;
import com.lp.reggie.entity.ShoppingCart;
import com.lp.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    /*
     * @param ShoppingCart
     * @return ShoppingCart
     * @description 添加菜品套餐到购物车
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
//        设置userId
        Long userId = BaseContext.getCurRentId();
        shoppingCart.setUserId(userId);
//        判断是否添加过
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        Long dishId = shoppingCart.getDishId();
        if (dishId != null) {
//           是菜品
            queryWrapper.eq(ShoppingCart::getUserId, userId);
            queryWrapper.eq(ShoppingCart::getDishId, dishId);

        } else {
//            是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
            queryWrapper.eq(ShoppingCart::getUserId, userId);
        }
        ShoppingCart shoppingCart1 = shoppingCartService.getOne(queryWrapper);
//        相同
        if (shoppingCart1 != null) {
            shoppingCart1.setNumber(shoppingCart1.getNumber() + 1);
            shoppingCartService.updateById(shoppingCart1);
        } else {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCart1 = shoppingCart;
            shoppingCartService.save(shoppingCart1);
        }
        return R.success(shoppingCart1);
    }

    /*
     * @param ShoppingCart
     * @return ShoppingCart
     * @description 从购物车删除菜品
     */
    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart) {
        Long userId = BaseContext.getCurRentId();
//        查出对应菜品的个数
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        Long dishId = shoppingCart.getDishId();
        if (dishId != null) {
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
        } else {
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart shoppingCart1 = shoppingCartService.getOne(queryWrapper);
        int number = shoppingCart1.getNumber();
        if (number > 0) {
            shoppingCart1.setNumber(shoppingCart1.getNumber() - 1);
            shoppingCartService.updateById(shoppingCart1);
        }
        return R.success(shoppingCart1);
    }

    /*
     * @param
     * @return ShoppingCart
     * @description 清空购物车
     */
    @DeleteMapping("clean")
    public R<String> clean() {
        Long userId = BaseContext.getCurRentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        shoppingCartService.remove(queryWrapper);
        return R.success("清空购车成功");
    }


}
