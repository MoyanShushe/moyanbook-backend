package com.moyanshushe.controller;

/*
 * Author: Hacoj
 * Version: 1.0
 */

import com.moyanshushe.model.Result;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.babyfish.jimmer.client.meta.Api;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.util.UUID;

@Api
@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {
    /**
     * @param path     想要下载的文件的路径
     * @param response
     * @功能描述 下载文件:
     */
    @Async
    @GetMapping("/download")
    public void download(@RequestParam(value = "name") String path, HttpServletResponse response) {
        String bookPath = "../";
        log.info("downloading file: {}", new File(bookPath).getAbsolutePath());
        String bookDirectory = "imageDirectory/";
        try {
            // path是指想要下载的文件的路径
            File file = new File(bookDirectory + path);
            log.info(file.getPath());
            // 获取文件名
            String filename = file.getName();
            // 获取文件后缀名
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
            log.info("文件后缀名：" + ext);

            // 将文件写入输入流
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStream fis = new BufferedInputStream(fileInputStream);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();

            // 清空response
            response.reset();
            // 设置response的Header
            response.setCharacterEncoding("UTF-8");
            //Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
            //attachment表示以附件方式下载   inline表示在线打开   "Content-Disposition: inline; filename=文件名.mp3"
            // filename表示文件的默认名称，因为网络传输只支持URL编码的相关支付，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            // 告知浏览器文件的大小
            response.addHeader("Content-Length", "" + file.length());
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            outputStream.write(buffer);
            outputStream.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @PostMapping("/file/upload")
    public Result upload(MultipartFile file) {
        // TODO 完善
        // 获取文件名称
        file.getOriginalFilename();

        if (file.isEmpty()) {
            return Result.error("文件为空");
        }

        try {
            InputStream fileInputStream = file.getInputStream();
            String fileName = UUID.randomUUID().toString().replace("-", "") + file.getOriginalFilename();
        }

        return Result.error("");
    }
}
