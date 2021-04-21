package com.easyarch;

import com.easyarch.Chat.demo.Socket.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetSocketAddress;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
        new NettyServer().start(new InetSocketAddress("127.0.0.1", 8090));
    }
}
