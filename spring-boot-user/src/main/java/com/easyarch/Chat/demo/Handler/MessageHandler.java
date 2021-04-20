package com.easyarch.Chat.demo.Handler;

import com.easyarch.error.entity.Erormessage;
import com.easyarch.error.service.Errorservice;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;

public class MessageHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Autowired
    private Errorservice errorservice;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg){

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //异常日志
        //errorservice.insert(new Erormessage(String.valueOf(System.currentTimeMillis()),ctx.channel().remoteAddress().toString(),cause.toString()));
        super.exceptionCaught(ctx, cause);
    }
}
