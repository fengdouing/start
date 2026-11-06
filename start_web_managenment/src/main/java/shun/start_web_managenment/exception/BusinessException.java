package shun.start_web_managenment.exception;

/**
 * 业务异常：用于「班级下有学员不能删除」「部门下有员工不能删除」这类
 * 可预期的业务规则校验，由全局异常处理器转换成 code=0 + msg 返回前端。
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}