package com.moyanshushe.controller;

/*
 * Author: Hacoj
 * Version: 1.0
 */

import com.moyanshushe.constant.FileConstant;
import com.moyanshushe.exception.io.FileUploadException;
import com.moyanshushe.model.Result;
import com.moyanshushe.properties.OutSideProperty;
import com.moyanshushe.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.babyfish.jimmer.client.meta.Api;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Api
@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    private final OutSideProperty outSideProperty;

    // 构造函数注入OutSideProperty，用于获取文件存储路径和服务器地址
    public FileController(OutSideProperty outSideProperty) {
        this.outSideProperty = outSideProperty;
    }

    /**
     * 异步上传图片
     *
     * @param file 接收上传的文件
     * @return CompletableFuture<Result> 异步返回文件上传结果，其中包含文件的访问URL
     */
    @Api // 标识异步执行的API接口
    @Async // 异步执行
    @PostMapping("/upload/image")
    public CompletableFuture<Result> uploadImage(@RequestParam("file") MultipartFile file) {
        // 获取文件原始名称
        String filename = file.getOriginalFilename();

        // 检查文件名格式是否合法
        if (!FileUtil.checkFileNameFormat(Objects.requireNonNull(filename))) {
            throw new FileUploadException(FileConstant.FILE_NOT_SUPPORTED);
        }

        // 检查文件是否为空
        if (file.isEmpty()) {
            throw new FileUploadException(FileConstant.FILE_IS_NULL);
        }

        try {
            // 获取文件输入流
            InputStream fileInputStream = file.getInputStream();
            // 生成文件名
            String fileName = UUID.randomUUID().toString().replace("-", "") + filename;

            // 创建服务器上存储文件的对象
            File fileInServer = new File(outSideProperty.getFileDir() + fileName);

            // 确保文件存储路径存在，若不存在尝试创建
            if (!fileInServer.exists() && (!fileInServer.createNewFile())) {
                throw new FileUploadException();
            }

            // 写入文件到服务器
            byte[] buf = new byte[2048];
            FileOutputStream fileOutputStream = new FileOutputStream(fileInServer);
            while (fileInputStream.read(buf) != -1) {
                fileOutputStream.write(buf);
            }
            fileOutputStream.flush();

            // 关闭输入输出流
            fileOutputStream.close();
            fileInputStream.close();

            // 记录文件上传日志
            log.info("文件上传成功: {}", fileInServer.getPath());
            // 返回文件的访问URL
            return CompletableFuture.completedFuture(Result.success(outSideProperty.getServerAddress() + fileInServer.getName()));
        } catch (IOException e) {
            throw new FileUploadException();
        }
    }
}
