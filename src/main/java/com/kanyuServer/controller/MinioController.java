package com.kanyuServer.controller;
import com.kanyuServer.common.Result;
import com.kanyuServer.utils.MinioUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

/**
 * @Description minio文件上传控制器
 */
@CrossOrigin
@RestController
@RequestMapping("/file")
public class MinioController {
    @Autowired
    private MinioUtil minioUtils;

    /**
     * @param file     文件
     * @param fileName 文件名称
     * @return {@link Result }
     * @Description 上传文件
     */
    @PostMapping("/upload")
    public Result uploadFile(@RequestParam("file") MultipartFile file, String fileName) {
        String upload = minioUtils.upload(file, fileName);
        return Result.ok(upload);
    }

    /**
     * @param fileName 文件名称
     * @return {@link ResponseEntity }
     * @Description dowload文件
     */
    @GetMapping("/dowload")
    public ResponseEntity dowloadFile(@RequestParam("fileName") String fileName) {
        return minioUtils.download(fileName);
    }

    /**
     * @param fileName 文件名称
     * @return {@link Result }
     * @Description 得到文件url
     */
    @GetMapping("/getUrl")
    public Result getFileUrl(@RequestParam("fileName") String fileName){
        HashMap map=new HashMap();
        map.put("FileUrl",minioUtils.getFileUrl(fileName));
        return Result.ok(map);
    }
}
