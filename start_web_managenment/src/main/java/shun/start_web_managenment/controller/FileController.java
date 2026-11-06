package shun.start_web_managenment.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import shun.start_web_managenment.pojo.Result;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

/**
 * 文件上传
 */
@Slf4j
@RestController
public class FileController {

    /** 只允许的扩展名（按需求：仅 jpg / png） */
    private static final Set<String> ALLOWED_EXT = Set.of("jpg", "png");

    /** 上传目录，来自 application.yml 的 file.upload-dir */
    @Value("${file.upload-dir}")
    private String uploadDir;

    /** 单文件大小上限(MB)，来自 application.yml 的 file.max-size-mb */
    @Value("${file.max-size-mb:10}")
    private long maxSizeMb;

    /**
     * 前端 el-upload 的 action="/api/upload"，经过 nginx/vite 去掉 /api 后即 POST /upload
     * 返回 Result.data 即可直接访问的相对地址，如 /images/xxx.jpg
     */
    @PostMapping("/upload")
    public Result upload(@RequestParam(value = "file", required = false) MultipartFile file) {
        // 1. 空文件校验
        if (file == null || file.isEmpty()) {
            return Result.error("上传文件不能为空");
        }
        // 2. 大小校验（兜底，真正超限会在进入方法前被 MaxUploadSizeExceededException 拦下）
        if (file.getSize() > maxSizeMb * 1024 * 1024) {
            return Result.error("文件大小不能超过" + maxSizeMb + "MB");
        }
        // 3. 扩展名白名单校验
        String originalFilename = file.getOriginalFilename();
        String ext = getExtension(originalFilename);
        if (!ALLOWED_EXT.contains(ext)) {
            return Result.error("只允许上传 jpg/png 格式的图片");
        }
        // 4. 目标目录（不存在则创建）
        Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
        // 5. 新文件名 = UUID + 原扩展名，绝不使用原始文件名
        String newFileName = UUID.randomUUID().toString().replace("-", "") + "." + ext;
        Path target = dir.resolve(newFileName).normalize();
        // 6. 防路径穿越兜底
        if (!target.startsWith(dir)) {
            return Result.error("非法的文件路径");
        }
        // 7. 落盘：try-with-resources 关流 + REPLACE_EXISTING
        try (InputStream in = file.getInputStream()) {
            Files.createDirectories(dir);
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("文件保存失败: {}", target, e);
            return Result.error("文件保存失败，请稍后重试");
        }
        String url = "/images/" + newFileName;
        log.info("文件上传成功: {} -> {}，访问地址: {}", originalFilename, target, url);
        return Result.success(url);
    }

    /** 取小写扩展名，取不到返回空串 */
    private String getExtension(String filename) {
        if (filename == null) {
            return "";
        }
        int i = filename.lastIndexOf('.');
        if (i < 0 || i == filename.length() - 1) {
            return "";
        }
        return filename.substring(i + 1).toLowerCase(Locale.ROOT);
    }
}