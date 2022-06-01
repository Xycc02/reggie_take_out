package com.xuyuchao.reggie.controller;

import com.xuyuchao.reggie.common.R;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * @Author: xuyuchao
 * @Date: 2022-05-31-18:39
 * @Description: 文件上传和下载
 */
@RequestMapping("/common")
@RestController
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        //参数名必须和前端form表单中name值一致,file是一个临时文件,需要转存到指定位置,否则本次请求完成之后临时文件会被删除
        log.info("file={}",file);
        //原始文件名
        String originalFilename = file.getOriginalFilename();//xxxx.jpg
        //截取.jpg
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
        log.info("substring={}",substring);
        //使用UUID生成唯一文件名,防止名称重复导致覆盖
        String fileName = UUID.randomUUID().toString() + substring;

        //创建一个目录对象
        File dir = new File(basePath);
        if(!dir.exists()) {
            //目录不存在,则要创建
            dir.mkdirs();
        }
        try {
            //文件转存
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }

    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {

        FileInputStream fileInputStream = null;
        ServletOutputStream outputStream = null;
        try {
            //1.从服务器读出name图片,输入流
            fileInputStream = new FileInputStream(new File(basePath + name));
            //2.将name图片写回浏览器,输出流
            outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while ( (len = fileInputStream.read(bytes) )!= -1 ){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
