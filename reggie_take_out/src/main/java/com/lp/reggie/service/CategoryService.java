package com.lp.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lp.reggie.entity.Category;

public interface CategoryService extends IService<Category> {
//    根据id删除分类
void remove(Long id);
}
