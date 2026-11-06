package shun.start_web_managenment.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import shun.start_web_managenment.pojo.Result;

/**
 * 全局异常处理器：
 * 1. 上传超限
 * 2. 业务校验失败（班级下有学员、部门下有员工）
 * 3. 数据库唯一约束冲突（学号/手机号/身份证号重复）
 * 统一转成 code=0 + msg，前端直接弹提示，不会出现 500 白页。
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Value("${file.max-size-mb:10}")
    private long maxSizeMb;

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result handleMaxUploadSize(MaxUploadSizeExceededException e) {
        log.warn("上传文件超过大小限制: {}", e.getMessage());
        return Result.error("上传文件超过大小限制，单个文件最大" + maxSizeMb + "MB");
    }

    @ExceptionHandler(BusinessException.class)
    public Result handleBusiness(BusinessException e) {
        log.warn("业务校验失败: {}", e.getMessage());
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public Result handleDuplicateKey(DuplicateKeyException e) {
        log.warn("唯一约束冲突: {}", e.getMessage());
        return Result.error("保存失败：学号、手机号或身份证号已存在，请检查后重试");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public Result handleDataIntegrity(DataIntegrityViolationException e) {
        log.warn("数据完整性异常: {}", e.getMessage());
        return Result.error("保存失败：数据不完整或不符合数据库约束");
    }
}