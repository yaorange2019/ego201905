package com.ego.upload.web;

import com.ego.upload.service.UploadService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 〈〉
 *
 * @author coach tam
 * @email 327395128@qq.com
 * @create 2019/9/25
 * @since 1.0.0
 * 〈坚持灵活 灵活坚持〉
 */
@RestController
public class UploadController {
    @Autowired
    private UploadService uploadService;

    @PostMapping("image")
    public ResponseEntity<String> image(@RequestParam("file") MultipartFile file)
    {
        String url = uploadService.uploadImage(file);
        if (StringUtils.isBlank(url)) {
            //400:bad request,表示客户端提交的请求有问题
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(url);
    }
}
