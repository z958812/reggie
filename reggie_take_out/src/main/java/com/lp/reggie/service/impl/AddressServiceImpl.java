package com.lp.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lp.reggie.entity.AddressBook;
import com.lp.reggie.mapper.AddressBookMapper;
import com.lp.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @author lp
 * @date 2022-10-15 20:52
 * @description:地址簿管理
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
