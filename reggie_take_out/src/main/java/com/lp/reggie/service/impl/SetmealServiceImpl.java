package com.lp.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lp.reggie.common.CustomException;
import com.lp.reggie.dto.SetmealDto;
import com.lp.reggie.entity.Setmeal;
import com.lp.reggie.entity.SetmealDish;
import com.lp.reggie.mapper.SetmealMapper;
import com.lp.reggie.service.SetmealDishService;
import com.lp.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    @Override
    public void saveWithDish(@RequestBody SetmealDto setmealDto) {
//        保存套餐基本信息
        this.save(setmealDto);
//        获取套餐关联菜品集合,并为集合每个元素赋值套餐setmealId
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
//        保存套餐与菜品信息的关联信息,操作setmeal_dish,执行insert操作
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    @Transactional
    public void deleteWithDish(List<Long> ids) {
//        创建条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
//        添加条件
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);
//        统计正在售卖的套餐的个数
        int count = this.count(queryWrapper);
        if (count > 0) {
            throw new CustomException("有" + count + "套餐正在售卖,不能删除");
        }
//        删除套餐setmeal中数据
        this.removeByIds(ids);
//        删除套餐中关联的菜品setmeal_dish
        LambdaQueryWrapper<SetmealDish> queryWrapper1 = new LambdaQueryWrapper<>();
//        添加条件
        queryWrapper1.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(queryWrapper1);
    }
}
