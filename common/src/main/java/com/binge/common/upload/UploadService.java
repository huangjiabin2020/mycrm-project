package com.binge.common.upload;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.binge.common.http.AxiosResult;
import com.binge.common.http.AxiosStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author JiaBin Huang
 * @date 2020/10/24
 **/
@Component
public class UploadService {
    @Value("${aliyun.endpoint}")
    private String endpoint;
    @Value("${aliyun.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.accessKeySecret}")
    private String accessKeySecret;
    @Value("${aliyun.bucketName}")
    private String bucketName;
    @Value("${aliyun.ext}")
    private List<String> ext;
    @Value("${aliyun.size}")
    private int size;
    @Value("${aliyun.url}")
    private String url;

    /**
     * 缺点：只能通过后缀名判断文件类型，万一别人强改文件后缀名呢？
     *
     * @param in
     * @param fileName
     * @param size
     * @return
     */
    public AxiosResult upload(InputStream in, String fileName, long size) {
        ext = Arrays.asList(ext.get(0).split(","));
        if (!ext.contains(StringUtils.getFilenameExtension(fileName))) {
            return AxiosResult.error(AxiosStatus.EXT_ERROR);
        }

        if (size / 1024 / 1024 > this.size) {
            return AxiosResult.error(AxiosStatus.FILE_TOOLONG);
        }


        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        ossClient.putObject(bucketName, fileName, in);

        ossClient.shutdown();

        return AxiosResult.success(url + fileName);
    }

    /**
     * 通过ImageIO判断是否是图片
     *
     * @param in
     * @param fileName
     * @param size     单位是兆
     * @return
     */
    public AxiosResult imgUpload(InputStream in, String fileName, long size) {

        byte[] bytes = null;
        try {
            bytes = new byte[in.available()];
//        要保存流到内存中，因为流一旦被读取就没了，就不能将流上传到云服务器了
            in.read(bytes);
            BufferedImage read = ImageIO.read(new ByteArrayInputStream(bytes));
            if (Objects.isNull(read)) {
                return AxiosResult.error(AxiosStatus.NOT_IMAGE);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        if (size / 1024 / 1024 > this.size) {
            return AxiosResult.error(AxiosStatus.FILE_TOOLONG);
        }

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        ossClient.putObject(bucketName, fileName, new ByteArrayInputStream(bytes));

        ossClient.shutdown();

        return AxiosResult.success(url + fileName);
    }
}
