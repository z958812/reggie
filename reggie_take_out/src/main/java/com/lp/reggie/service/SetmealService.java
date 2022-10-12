package com.lp.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lp.reggie.dto.SetmealDto;
import com.lp.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    void saveWithDish(SetmealDto setmealDto);

    /*
     * @param List <ids>
     * @return void
     * @description 删除套餐时同时删除关联的菜品
     */
    void deleteWithDish(List<Long> ids);
}
