package com.lp.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lp.reggie.common.R;
import com.lp.reggie.entity.Category;
import com.lp.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    /*
    * 分类
    * */
    @PostMapping
    public R<String> save(@RequestBody Category category ){
        log.info("分类项目启动成功");
        categoryService.save(category);
        return R.success("新增成功");
    }

    @GetMapping("/page")
    public R<Page>page (int page,int pageSize){

//        构造分页构造器
        Page <Category>pageInfo = new Page<>(page,pageSize);
//        构造分页条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
//        添加排序条件,根据sort进行排序
        queryWrapper.orderByDesc(Category::getSort);
//        分页查询
        categoryService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    /*
    * 删除分类
    * */
    @DeleteMapping
    public R<String> delete( Long ids){
        log.info("删除分类,id为:{}",ids);
        categoryService.remove(ids);
        return R.success("删除成功");
    }
    /*
    * 修改分类
    * */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改分类");
        categoryService.updateById(category);
        return R.success("修改分类成功");
    }
    /*
     * @param type = 1
     * @return  List
     * @description 查询菜品分类
     */
     @GetMapping("/list")
    public R<List<Category>> list(Category category){
//      条件构造器
         LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
//         添加条件
         queryWrapper.eq(category.getType()!= null,Category::getType,category.getType());
//         添加排序条件
         queryWrapper.orderByDesc(Category::getSort).orderByDesc(Category::getUpdateTime);
         List<Category> list = categoryService.list(queryWrapper);
         return R.success(list);
     }


}
