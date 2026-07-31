package com.example.blogcommunity.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class BrowserAutoOpenConfig {

    @Bean
    public CommandLineRunner openBrowser() {
        return args -> {
            String frontendUrl = "http://localhost:5173";
            
            System.out.println("\n======================================");
            System.out.println("博客社区后端服务启动完成");
            System.out.println("======================================");
            System.out.println("后端地址: http://localhost:8081");
            System.out.println("前端地址: " + frontendUrl);
            System.out.println("======================================");
            System.out.println("正在打开浏览器...");
            System.out.println("如果页面无法访问，请先启动前端服务:");
            System.out.println("cd frontend && npm run dev");
            System.out.println("======================================\n");
            
            try {
                String os = System.getProperty("os.name").toLowerCase();
                ProcessBuilder pb;
                
                if (os.contains("win")) {
                    pb = new ProcessBuilder("cmd", "/c", "start", frontendUrl);
                } else if (os.contains("mac")) {
                    pb = new ProcessBuilder("open", frontendUrl);
                } else if (os.contains("nix") || os.contains("nux")) {
                    pb = new ProcessBuilder("xdg-open", frontendUrl);
                } else {
                    System.out.println("不支持的操作系统，请手动打开浏览器访问: " + frontendUrl);
                    return;
                }
                
                pb.start();
                System.out.println("已尝试打开浏览器: " + frontendUrl);
                
            } catch (IOException e) {
                System.out.println("自动打开浏览器失败，请手动访问: " + frontendUrl);
                System.out.println("错误原因: " + e.getMessage());
            }
        };
    }
}
