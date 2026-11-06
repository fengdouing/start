package shun.start_web_managenment.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * BCrypt 密文生成工具（不是 Web 接口，不会被前端调用）。
 * 用法：为 main 方法传入一个明文密码参数，控制台会输出可直接执行的 UPDATE 语句。
 */
public class PasswordEncoderTool {

    public static void main(String[] args) {
        if (args.length != 1 || args[0].isBlank()) {
            System.out.println("用法: PasswordEncoderTool <明文密码>");
            return;
        }
        String hash = new BCryptPasswordEncoder().encode(args[0]);
        System.out.println("BCrypt 密文: " + hash);
        System.out.println();
        System.out.println("-- 复制下面这条语句到 MySQL 执行：");
        System.out.println("UPDATE `user` SET password = '" + hash + "' WHERE username = 'admin';");
    }
}
