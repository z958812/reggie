package com.lp.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lp.reggie.entity.DishFlavor;
import com.lp.reggie.mapper.DishFlavorMapper;
import com.lp.reggie.service.DishFlavorService;
import org.springframework.stereotype.Service;

/**
 * @author lp
 * @date 2022-10-09 10:30
 * @description:菜品口味的服务层实现
 */
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
