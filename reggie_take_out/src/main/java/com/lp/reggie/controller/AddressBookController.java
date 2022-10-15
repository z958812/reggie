package com.lp.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lp.reggie.common.BaseContext;
import com.lp.reggie.common.R;
import com.lp.reggie.entity.AddressBook;
import com.lp.reggie.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lp
 * @date 2022-10-15 20:54
 * @description: 地址簿管理
 */
@RestController
@RequestMapping("addressBook")
@Slf4j
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    /*
     * @param AddressBook
     * @return String
     * @description 新增收货地址
     */
    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook) {
        Long userId = BaseContext.getCurRentId();
        addressBook.setUserId(userId);
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }

    /*
     * @param
     * @return List<AddressBook>
     * @description 根据用户id userId查收获地址
     */
    @GetMapping("list")
    public R<List<AddressBook>> getList() {
//        获取用户id
        Long userId = BaseContext.getCurRentId();
//        构建条件构造器
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
//        添加条件
        queryWrapper.eq(AddressBook::getUserId, userId);
//        查询
        List<AddressBook> addressBooks = addressBookService.list(queryWrapper);
        return R.success(addressBooks);
    }
    /*
     * @param AddressBook
     * @return String
     * @description 设置默认地址
     */

    @PutMapping("default")
    public R<String> setDefault(@RequestBody AddressBook addressBook) {
//        创建条件构造器
        LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();
//        添加条件
        updateWrapper.set(AddressBook::getIsDefault, 1);
        updateWrapper.eq(AddressBook::getId, addressBook.getId());
//        修改
        addressBookService.update(updateWrapper);
        return R.success("默认地址修改成功");
    }

    /*
     * @param id
     * @return AddressBook
     * @description 根据id查询地址展示出来
     */
    @GetMapping("/{id}")
    public R<AddressBook> getAddressBook(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        return R.success(addressBook);
    }

    /*
     * @param AddressBook
     * @return Address
     * @description 编辑收货地址
     */
    @PutMapping
    public R<String> update(AddressBook addressBook) {
        return R.success("成功");

    }


}
