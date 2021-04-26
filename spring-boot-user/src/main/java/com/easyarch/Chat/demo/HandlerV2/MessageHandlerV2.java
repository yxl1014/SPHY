package com.easyarch.Chat.demo.HandlerV2;

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

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class MessageHandlerV2 extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
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
        String username = channelToUsername.get(ctx.channel());
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
            channelToUsername.put(ctx.channel(), message.getContent());
            /*sendMessageInUnlion(ctx.channel(), message.getContent());*/

            sendMessageForAll(new Message(ctx.channel().id().asShortText(), "用户上线：" + message.getContent(), System.currentTimeMillis(), MessageType.TEXT.name()));
            return;
        }

        if (MessageType.VIDEO.name().equals(message.getMessageType())) {
            Channel channel = usernameToChannel.get(message.getToUsername());
            if (channel != null)
                sendVideoByChannel(message, channel, true);
            else
                sendVideoByChannel(message, null, false);
            return;
        }

        if (MessageType.PICTURE.name().equals(message.getMessageType())) {
            Channel channel = usernameToChannel.get(message.getToUsername());
            if (channel != null)
                sendPicByChannel(message, channel, true);
            else
                sendPicByChannel(message, null, false);
            return;
        }

        if (MessageType.VOICE.name().equals(message.getMessageType())) {
            Channel channel = usernameToChannel.get(message.getToUsername());
            if (channel != null)
                sendVoiceByChannel(message, channel, true);
            else
                sendVoiceByChannel(message, null, false);
            return;
        }


        String to = message.getToUsername();
        Channel c = usernameToChannel.get(to);

        if (c == null) {
        } else
            sendMessageByChannel(c, message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        poolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                errorservice.insert(new Erormessage(String.valueOf(System.currentTimeMillis()), ctx.channel().remoteAddress().toString(), cause.toString()));
            }
        });
        super.exceptionCaught(ctx, cause);
    }

    private void sendPicByChannel(Message message, Channel channel, boolean life) {
        String path = "/test/sphy/pic/";
        String picname = message.getTimestamp() + message.getFromUsername() + ".jpg";

        poolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                int len;
                OutputStream os;
                try {
                    os = new FileOutputStream(path + picname);
                    len = message.getContent().getBytes().length;
                    os.write(message.getContent().getBytes(), 0, len);
                    os.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        if (life) {
            sendMessageByChannel(channel, message);
        } else {

        }

    }

    private void sendVoiceByChannel(Message message, Channel channel, boolean life) {
        String path = "/test/sphy/voi/";
        String voiname = message.getTimestamp() + message.getFromUsername() + ".wav";

        poolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                int len;
                OutputStream os;
                try {
                    os = new FileOutputStream(path + voiname);
                    len = message.getContent().getBytes().length;
                    os.write(message.getContent().getBytes(), 0, len);
                    os.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        if (life) {
            sendMessageByChannel(channel, message);
        } else {
            //存缓存
        }
    }

    private void sendVideoByChannel(Message message, Channel channel, boolean life) {
        String path = "/test/sphy/vid/";
        String vidname = message.getTimestamp() + message.getFromUsername() + ".mp4";

        poolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                int len;
                OutputStream os;
                try {
                    os = new FileOutputStream(path + vidname);
                    len = message.getContent().getBytes().length;
                    os.write(message.getContent().getBytes(), 0, len);
                    os.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        if (life) {
            sendMessageByChannel(channel, message);
        } else {
            //存缓存
        }
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
