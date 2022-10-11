package com.lp.reggie.dto;

import com.lp.reggie.entity.Setmeal;
import com.lp.reggie.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
