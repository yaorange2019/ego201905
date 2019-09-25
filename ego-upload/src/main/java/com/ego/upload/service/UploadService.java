package com.ego.upload.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 〈〉
 *
 * @author coach tam
 * @email 327395128@qq.com
 * @create 2019/9/25
 * @since 1.0.0
 * 〈坚持灵活 灵活坚持〉
 */
@Service
public class UploadService {


    @Autowired
    private FastFileStorageClient storageClient;

    private List<String> contentTypes = Arrays.asList("image/jpeg", "image/png");


    public String uploadImage(MultipartFile image) {
        //检查图片后缀
        String contentType = image.getContentType();

        if(!contentTypes.contains(contentType))
        {
            return null;
        }
        //检查是否图片内容

        try {
            BufferedImage read = ImageIO.read(image.getInputStream());
            if(read==null)
            {
                return null;
            }
            //保存图片
//            image.transferTo(new File("d://images/"+image.getOriginalFilename()));

            String ext = StringUtils.substringAfterLast(image.getOriginalFilename(),".");
            StorePath storePath = storageClient.uploadFile(image.getInputStream(), image.getSize(), ext, null);
            //返回图片地址
            return "http://image.ego.com/"+ storePath.getFullPath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

}
