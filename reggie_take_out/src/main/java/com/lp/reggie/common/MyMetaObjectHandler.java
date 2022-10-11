package com.lp.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("自动插入功能启动");
       metaObject.setValue("createTime", LocalDateTime.now());
       metaObject.setValue("createUser", BaseContext.getCurRentId());
       metaObject.setValue("updateTime", LocalDateTime.now());
       metaObject.setValue("updateUser", BaseContext.getCurRentId());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("自动插入功能启动");
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", BaseContext.getCurRentId());

    }
}
