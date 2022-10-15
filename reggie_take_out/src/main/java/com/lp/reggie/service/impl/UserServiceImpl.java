package com.lp.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lp.reggie.entity.User;
import com.lp.reggie.mapper.UserMapper;
import com.lp.reggie.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author lp
 * @date 2022-10-15 10:29
 * @description:user的服务类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
