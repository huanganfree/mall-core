package com.ha.springbootdemo.SeckillTest;

import com.ha.springbootdemo.common.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@SpringBootTest
public class SeckillTest {

    @Autowired
    private JwtUtils jwtUtils;

    public SeckillTest() {
    }

    @Test
    public void generateTokens() {
        String filePath = "./tokens.txt";
        System.out.println("开始批量生成 1000 个 Token...");

        // 使用 try-with-resources 自动关闭流，防止内存泄漏
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            for (int i = 1; i <= 1000; i++) {
                long userId = i;

                // 直接调用你项目里已经验证通过的、完美的 Token 生成方法
                String token = jwtUtils.generateToken(userId, userId, String.valueOf(userId));

                // 写入文件，并换行
                writer.write("Bearer " + token);
                writer.newLine();
            }

            System.out.println(">>>> 1000 个 Token 成功导出至: " + filePath + " <<<<");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
