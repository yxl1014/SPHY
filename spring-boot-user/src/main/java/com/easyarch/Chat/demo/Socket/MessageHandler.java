package com.easyarch.Chat.demo.Socket;

import com.easyarch.Chat.demo.entity.Message;
import com.easyarch.Chat.demo.entity.MessageType;
import com.easyarch.error.entity.Erormessage;
import com.easyarch.error.service.Errorservice;
import com.google.gson.Gson;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class MessageHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    public static ConcurrentHashMap<String, ConcurrentLinkedDeque<Message>> usernamecache = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Channel> usernameToChannel = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<Channel, String> channelToUsername = new ConcurrentHashMap<>();
    public static Executor poolExecutor = Executors.newCachedThreadPool();
    public static AtomicInteger online = new AtomicInteger();

    @Autowired
    private Errorservice errorservice;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        channelGroup.add(ctx.channel());
        online.set(channelGroup.size());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        String username=channelToUsername.get(ctx.channel());
        usernameToChannel.remove(username);
        channelToUsername.remove(ctx.channel());
        channelGroup.remove(ctx.channel());
        online.set(channelGroup.size());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        Message message = new Gson().fromJson(msg.text(), Message.class);

        if (message == null) {
            sendMessageByChannel(ctx.channel(), new Message(ctx.channel().id().asShortText(), "消息错误", System.currentTimeMillis(), MessageType.TEXT.name()));
            return;
        }

        //用户上线给其他用户发送上线提示
        if (MessageType.INIT.name().equals(message.getMessageType())) {
            usernameToChannel.put(message.getContent(), ctx.channel());
            channelToUsername.put(ctx.channel(),message.getContent());
            sendMessageInUnlion(ctx.channel(), message.getContent());
            sendMessageForAll(new Message(ctx.channel().id().asShortText(), "用户上线：" + message.getContent(), System.currentTimeMillis(), MessageType.TEXT.name()));
            return;
        }

        if (!MessageType.TEXT.name().equals(message.getMessageType())) {
            return;
        }

        String to = message.getToUsername();
        Channel c = usernameToChannel.get(to);

        if (c == null) {
            ConcurrentLinkedDeque<Message> messages = usernamecache.get(to);
            if (messages == null) {
                usernamecache.put(to, new ConcurrentLinkedDeque<>());
                messages = usernamecache.get(to);
            }
            messages.addFirst(message);
        } else
            sendMessageByChannel(c, message);
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

    private void sendMessageInUnlion(Channel channel, String username) {
        ConcurrentLinkedDeque<Message> chats = usernamecache.get(username);

        if (chats == null)
            return;

        while (chats.size() != 0) {
            Message m = chats.getLast();
            sendMessageByChannel(channel, new Message(channel.id().asShortText(), m.getToUsername(), m.getForUsername(), m.getContent(), m.getMessageType(), m.getTimestamp()));
        }

        usernamecache.remove(username);
    }

    private void sendMessageByChannel(Channel channel, Message message) {
        channel.writeAndFlush(new TextWebSocketFrame(new Gson().toJson(message)));
    }

    private void sendMessageForAll(Message message) {
        for (Channel channel : channelGroup) {
            channel.writeAndFlush(new TextWebSocketFrame(new Gson().toJson(message)));
        }
    }
}
