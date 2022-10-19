package com.lp.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lp.reggie.common.R;
import com.lp.reggie.dto.DishDto;
import com.lp.reggie.entity.Category;
import com.lp.reggie.entity.Dish;
import com.lp.reggie.entity.DishFlavor;
import com.lp.reggie.service.CategoryService;
import com.lp.reggie.service.DishFlavorService;
import com.lp.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lp
 * @date 2022-10-09 10:35
 * @description:菜品及口味的控制层
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private RedisTemplate redisTemplate;

    /*
     * @param json dishdto
     * @return String
     * @description 保存菜品及其口味的方法
     */
    @PostMapping
    @CacheEvict(value = "dishCache", key = "#dishDto.categoryId+'_'+ #dishDto.status")
    public R<String> save(@RequestBody DishDto dishDto) {
        dishService.saveWithFlavor(dishDto);
        return R.success("保存成功");
    }

    /*
     * @param page=1 pageSize=10
     * @return String
     * @description 菜品分页查询
     */
     @GetMapping("/page")
    public R<Page> page (int page , int pageSize,String name){
//         构造分页构造器
         Page<Dish> pageInfo = new Page<>(page,pageSize);
         Page<DishDto> dishDtoPage = new Page<>();
//         构造分页条件查询构造器
         LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();
//         添加过滤条件
         queryWrapper.like(name != null ,Dish::getName,name);
//         添加分页排序条件
         queryWrapper.orderByDesc(Dish::getUpdateTime);
         dishService.page(pageInfo,queryWrapper);
//         对象拷贝
         BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
         List<Dish> records = pageInfo.getRecords();
         List<DishDto> list = records.stream().map((item)->{
             DishDto dishDto = new DishDto();
             BeanUtils.copyProperties(item,dishDto);
//             分类id
             Long categoryId = item.getCategoryId();
//             根据id查询分类对象
             Category category = categoryService.getById(categoryId);
              if(category != null){
                  String  categoryName = category.getName();
                  dishDto.setCategoryName(categoryName);
              }
              return dishDto;
                 }).collect(Collectors.toList());
         dishDtoPage.setRecords(list);
         return R.success(pageInfo);
     }

    /*
     * @param id
     * @return DishDto
     * @description 根据id查询菜品信息和对应的口味信息
     */
    @GetMapping("/{id}")
    public R<DishDto> get (@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    /*
     * @param DishDto
     * @return String
     * @description 修改菜品
     */
    @PutMapping
    @CacheEvict(value = "dishCache", key = "#dishDto.categoryId+'_'+ #dishDto.status")
    public R<String> update(@RequestBody DishDto dishDto) {
        dishService.updateWithFlavor(dishDto);
        return R.success("修改菜品成功");
    }
    /*
     * @param categoryId
     * @return list
     * @description 根据categoryId 查对应菜品
     */

    @GetMapping("list")
    @Cacheable(value = "dishCache", key = "#dish.categoryId+'_' + #dish.status")
    public R<List<DishDto>> list(Dish dish) {
//      查询Redis中有无缓存有直接返回
        List<DishDto> dishDtos = null;
//        创建条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        添加查询条件
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus, 1);
        queryWrapper.orderByAsc(Dish::getSort).orderByAsc(Dish::getUpdateTime);
//      查询
        List<Dish> list = dishService.list(queryWrapper);
//       Dish组装为DishDto
        dishDtos = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
//            复制基本信息
            BeanUtils.copyProperties(item, dishDto);
//            根据categoryId查出categoryName
            Category category = categoryService.getById(dishDto.getCategoryId());
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
//           根据dishId 从dish_flavor查出口味信息
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> queryWrapper2 = new LambdaQueryWrapper<>();
            queryWrapper2.eq(DishFlavor::getDishId, dishId);
            List<DishFlavor> dishFlavors = dishFlavorService.list(queryWrapper2);
            dishDto.setFlavors(dishFlavors);
            return dishDto;
        }).collect(Collectors.toList());
        return R.success(dishDtos);
    }
    /*
     * @param ids
     * @return String
     * @description 根据菜品id删除菜品
     */

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        log.info("删除菜品功能启动");
//        删除
        dishService.removeByIds(ids);
        return R.success("删除成功");
    }

    /*
     * @param ids
     * @return String
     * @description 根据id批量修改菜品状态
     */
    @PostMapping("status/{status}")
    public R<String> update(@PathVariable("status") int status, @RequestParam List<Long> ids) {
//        创建条件构造器
        LambdaUpdateWrapper<Dish> updateWrapper = new LambdaUpdateWrapper<>();
//        添加条件
        updateWrapper.in(Dish::getId, ids).set(Dish::getStatus, status);
//       修改状态
        dishService.update(updateWrapper);
        return R.success("修改菜品状态成功");
    }
}
