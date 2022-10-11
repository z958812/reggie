package com.lp.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lp.reggie.common.CustomException;
import com.lp.reggie.entity.Category;
import com.lp.reggie.entity.Dish;
import com.lp.reggie.entity.Setmeal;
import com.lp.reggie.mapper.CategoryMapper;
import com.lp.reggie.service.CategoryService;
import com.lp.reggie.service.DishService;
import com.lp.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealservice;
    /*
    *根据id删除分类
    *删除之前需要进行判断
     */
    @Override
    public void remove(Long ids) {
//        添加查询信息,根据分类id进行查询菜品数据
        LambdaQueryWrapper<Dish> queryWrapper= new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId,ids);
        int count1= dishService.count(queryWrapper);
//        如果已经关联,抛出一个业务异常
        if (count1 > 0) {
            throw new CustomException("当前分类已关联菜品,不能删除");
        }
//        查询当前分类是否关联套餐,如果已经关联,抛出一个业务异常
        LambdaQueryWrapper<Setmeal> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(Setmeal::getCategoryId,ids);
        int count2 = setmealservice.count(queryWrapper1);
        if (count2 >0) {
            throw new CustomException("当前分类已关联套餐,不能删除");
        }
        super.removeById(ids);
    }
}
