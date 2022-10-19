package com.lp.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lp.reggie.common.R;
import com.lp.reggie.entity.User;
import com.lp.reggie.service.UserService;
import com.lp.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author lp
 * @date 2022-10-15 10:34
 * @ description: user控制层
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;

    /*
     * @param user
     * @return String
     * @description 发送短信验证码
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
//        获取手机号
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)) {
//            生成随机的验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}", code);
//            调用阿里云提供的短信服务API完成发送短信
//            SMSUtils.sendMessage("瑞吉外卖","",phone,code);
//            将生成的验证码保存到Session
//            session.setAttribute(phone, code);
//            将生成的验证码缓存到Redis中,并设置有效期为5分钟
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
            R.success("手机短信发送成功");
        }
        return R.error("短信发送失败");
    }

    /*
     * @param post 手机号码 phone 验证码 code
     * @return String
     * @description 手机用户登录
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        log.info(map.toString());
//        获取用户输入的手机号和验证码
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();
//        Object codeInSession = session.getAttribute(phone);
//       获取session中存储的验证码进行判断
//        从Redis中获取验证码,登陆成功删除
        Object codeInRedis = redisTemplate.opsForValue().get(phone);
        if (codeInRedis != null && codeInRedis.equals(code)) {
//            判断是否第一次登录,是则添加到user表中
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(queryWrapper);
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());
//           如果登录成功,删除Redis缓存
            redisTemplate.delete(phone);
            return R.success(user);
        }
        return R.error("登陆失败");
    }


}
