package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Controller
@Slf4j
public class FileUploadController {

    @Value("${file.save.path}")
    private String path;

    @RequestMapping("/fileUpload")
    @ResponseBody
    public String fileUpload(@RequestParam("files") MultipartFile[] files) {
        if (files != null & files.length > 0) {
            int count = 0;
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                String name = file.getOriginalFilename();
                String newName = UUID.randomUUID().toString().replace("-", "");
                newName = newName + "-" + name;
                if (saveFile(file, newName)) {
                    count++;
                }
            }
            if (count > 0) {
                return  "SUCCESS ! receive " + files.length + " , save " + count + " !";
            }
        }
        return "Fail";

    }

    /***
     * 保存文件
     * @param file
     * @return
     */
    private boolean saveFile(MultipartFile file, String newName) {
        // 判断文件是否为空
        if (!file.isEmpty()) {
            try {
                File filepath = new File(path);
                if (!filepath.exists()) {
                    filepath.mkdirs();
                }
                // 文件保存路径
                String savePath = path + File.separator + newName;
                if (new File(savePath).exists()) {
                    String time = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss:SSS").format(new Date());
                    savePath = time + path + "_" + File.separator + newName;
                }
                // 转存文件
                file.transferTo(new File(savePath));
                return true;
            } catch (Exception e) {
                log.error("file upload error ! ", e);
            }
        }
        return false;
    }

    public static void main(String[] args) {
        String time = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss:SSS").format(new Date());
        System.out.println(time);
    }
}
