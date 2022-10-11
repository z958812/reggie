package com.lp.reggie.controller;


import com.lp.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${reggie.path}")
    private String basePath;
    /*
     * @param  file
     * @return
     * @description 文件上传
     */
    @PostMapping("/upload")
    public R<String> upload (MultipartFile file){
//        file是一个临时文件,需要转存到指定位置,否则本次请求完成后临时文件会删除
        log.info(file.toString());
//        原始文件名
        String originalFilename = file.getOriginalFilename();
        String suffix=originalFilename.substring(originalFilename.lastIndexOf("."));
//        使用UUID重新生成文件名,防止文件名称重复造成文件覆盖
        String fileName= UUID.randomUUID() +suffix;
//        创建一个文件目录
        File dir = new File(basePath);
//        判断当前钼是否存在
        if(! dir.exists()){
           //目录不存在,需要创建
           dir.mkdirs();
        }
        try {
            file.transferTo(new File(basePath+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }
    /*
     * @param
     * @return void
     * @description 文件下载
     */
    @GetMapping("/download")
     public void download(String name , HttpServletResponse response) {
        try {
//        输入流,通过输入流获取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath+name));
//        输出流,通过输出流将内容写回浏览器
            ServletOutputStream servletOutputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len= fileInputStream.read(bytes))!=-1){
                servletOutputStream.write(bytes,0,len);
                servletOutputStream.flush();
            }
//            关闭资源
            servletOutputStream.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
