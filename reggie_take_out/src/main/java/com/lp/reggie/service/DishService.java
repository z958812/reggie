package com.lp.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lp.reggie.dto.DishDto;
import com.lp.reggie.entity.Dish;

public interface DishService extends IService<Dish> {
//    保存菜品口味
    void saveWithFlavor(DishDto dishDto);
//    根据id查询菜品信息和对应的口味信息
    DishDto getByIdWithFlavor(Long id);
//    更新菜品信息,同时更新对相应口味的信息
     void updateWithFlavor(DishDto dishDto);
}
