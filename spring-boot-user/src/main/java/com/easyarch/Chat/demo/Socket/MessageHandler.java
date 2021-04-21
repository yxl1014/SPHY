package com.easyarch.Chat.demo.Socket;

import com.easyarch.Chat.demo.entity.ChatMessage;
import com.easyarch.error.entity.Erormessage;
import com.easyarch.error.service.Errorservice;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class MessageHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    public static ConcurrentHashMap<String, ConcurrentLinkedDeque<ChatMessage>> usernamecache=new ConcurrentHashMap<>();
    public static Executor poolExecutor = Executors.newCachedThreadPool();

    @Autowired
    private Errorservice errorservice;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //异常日志
        poolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                errorservice.insert(new Erormessage(String.valueOf(System.currentTimeMillis()), ctx.channel().remoteAddress().toString(), cause.toString()));
            }
        });
        super.exceptionCaught(ctx, cause);
    }

    private void sendMessageInUnlion(String username){
        ConcurrentLinkedDeque<ChatMessage> chats=usernamecache.get(username);

        if (chats==null)
            return;
        else
            usernamecache.remove(username);


        //给指定用户发信息
    }
}
