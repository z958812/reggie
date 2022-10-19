package com.lp.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lp.reggie.common.R;
import com.lp.reggie.dto.SetmealDto;
import com.lp.reggie.entity.Category;
import com.lp.reggie.entity.Setmeal;
import com.lp.reggie.service.CategoryService;
import com.lp.reggie.service.DishService;
import com.lp.reggie.service.SetmealService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ author lp
 * @ date 2022-10-10 15:09
 * @ description:  套餐管理
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DishService dishService;

    @PostMapping
    @CacheEvict(value = "setmealCache", allEntries = true)
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        setmealService.saveWithDish(setmealDto);
        return R.success("添加套餐成功");
    }

    /*
     * @param page =1 pageSize=10
     * @return pageInfo
     * @description 套餐的分页展示
     */
    @GetMapping("page")
    public R<Page<SetmealDto>> page(int page, int pageSize, String name) {
//        构建分页构造器
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
//      构造条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
//      添加条件
        queryWrapper.like(StringUtils.isNotEmpty(name), Setmeal::getName, name);
//        查询
        setmealService.page(pageInfo, queryWrapper);
//      完善查询
        Page<SetmealDto> dtoPage = new Page<>(page, pageSize);
//        分页构造器copy
        BeanUtils.copyProperties(pageInfo, dtoPage);
        List<Setmeal> setmeals = pageInfo.getRecords();
        List<SetmealDto> setmealDtos = setmeals.stream().map((item) -> {
//            setmeal copy 到 setmealdto
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
//            categoryId
            Long id = item.getCategoryId();
//            根据categoryId 查category 取出name存入setmealdto
            Category category = categoryService.getById(id);
            setmealDto.setCategoryName(category.getName());
            return setmealDto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(setmealDtos);
        return R.success(dtoPage);
    }

    /*
     * @param ids
     * @return String
     * @description 根据传入的setmealid 删除套餐
     */
    @DeleteMapping
    @CacheEvict(value = "setmealCache", allEntries = true)
    public R<String> delete(@RequestParam List<Long> ids) {
        setmealService.deleteWithDish(ids);
        return R.success("套餐删除成功");
    }

    /*
     * @param List<Long>ids
     * @return String
     * @description 套餐状态修改
     */
    @PostMapping("/status/{status}")
    public R<String> update(@PathVariable int status, @RequestParam List<Long> ids) {
//         创建条件构造器
        LambdaUpdateWrapper<Setmeal> updateWrapper = new LambdaUpdateWrapper<>();
//         添加条件
        updateWrapper.in(Setmeal::getId, ids).set(Setmeal::getStatus, status);
//         更新套餐状态
        setmealService.update(updateWrapper);
        return R.success("套餐状态修改成功");
    }

    /*
     * @param categoryId&status=1
     * @return Setmeal
     * @description 根据categoryId 和状态查询到Setmeal
     */
    @GetMapping("/list")
    @Cacheable(value = "setmealCache", key = "#setmeal.categoryId + '_' + #setmeal.status")
    public R<List<Setmeal>> list(Setmeal setmeal) {
//         查询到套餐中的菜品
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getCategoryId, setmeal.getCategoryId());
        queryWrapper.eq(Setmeal::getStatus, setmeal.getStatus());
        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }

    /*
     * @param id
     * @return DishDto
     * @description 根据id查询套餐信息和对应的口味信息
     */
    @GetMapping("/{id}")
    public R<Setmeal> get(@PathVariable Long id) {
        Setmeal setmeal = setmealService.getById(id);
        return R.success(setmeal);
    }

}
