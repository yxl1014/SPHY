package com.easyarch.Chat.demo.HandlerV2;

import com.easyarch.Chat.demo.HandlerV2.entity.DataType;
import com.easyarch.Chat.demo.HandlerV2.entity.MessageV2;
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

import java.io.*;
import java.util.List;
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

    @Autowired
    private MessageService messageService;

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
        MessageV2 message = new Gson().fromJson(msg.text(), MessageV2.class);

        if (message == null) {//消息错误返回
            sendMessageByChannel(ctx.channel(), new MessageV2(null, ctx.channel().id().asShortText(), channelToUsername.get(ctx.channel()),
                    channelToUsername.get(ctx.channel()), "消息错误",
                    MessageType.TEXT.name(), DataType.TEXT.name(), true, true, System.currentTimeMillis()));
            return;
        }

        //用户上线给其他用户发送上线提示
        if (MessageType.INIT.name().equals(message.getMessageType())) {//初始化
            if (message.getContent().equals("") || message.getContent() == null) {//若用户的用户名为空则返回
                sendMessageByChannel(ctx.channel(), new MessageV2(null, ctx.channel().id().asShortText(), channelToUsername.get(ctx.channel()),
                        channelToUsername.get(ctx.channel()), "消息错误",
                        MessageType.TEXT.name(), DataType.TEXT.name(), true, true, System.currentTimeMillis()));
                return;
            }

            usernameToChannel.put(message.getContent(), ctx.channel());//在缓存中存储用户的用户名
            channelToUsername.put(ctx.channel(), message.getContent());

            List<MessageV2> list = messageService.getNoReadingList(channelToUsername.get(ctx.channel()));//获取用户离线时收到的信息

            for (MessageV2 m:list){
                sendMessageByChannelV2(ctx.channel(),m);//将离线信息发给自己
            }

            return;
        }

        if (MessageType.VIDEO.name().equals(message.getMessageType())) {//判断发送的是否为视频
            Channel channel = usernameToChannel.get(message.getToUsername());//查看发送用户是否在线
            if (channel != null)
                sendVideoByChannel(message, channel, true);
            else
                sendVideoByChannel(message, null, false);
            return;
        }

        if (MessageType.PICTURE.name().equals(message.getMessageType())) {//判断发送的是否为图片
            Channel channel = usernameToChannel.get(message.getToUsername());//查看发送用户是否在线
            if (channel != null)
                sendPicByChannel(message, channel, true);
            else
                sendPicByChannel(message, null, false);
            return;
        }

        if (MessageType.VOICE.name().equals(message.getMessageType())) {//判断发送的是否为音频
            Channel channel = usernameToChannel.get(message.getToUsername());//查看发送用户是否在线
            if (channel != null)
                sendVoiceByChannel(message, channel, true);
            else
                sendVoiceByChannel(message, null, false);
            return;
        }


        String to = message.getToUsername();//获取发给谁
        Channel c = usernameToChannel.get(to);//查看该用户是否在线

        if (c != null) {//若在线直接发送
            message.setReading(true);
            sendMessageByChannel(c, message);
        }

        messageService.addMessage(message);//将信息存入数据库
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

    private void sendPicByChannel(MessageV2 message, Channel channel, boolean life) {
        String path = "/test/sphy/pic/";//图片存储的路径
        String picname = message.getTimestamp() + message.getFromUsername()+getDataTpeToPicture(message.getDataType());//定义图片名

        poolExecutor.execute(new Runnable() {//异步将文件存入本地
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

        if (life) {//若用户在线直接发送
            message.setReading(true);//设置为已读
            sendMessageByChannel(channel, message);
        }

        message.setContent(path+picname);//将存入数据库的聊天内容改为文件的路径
        messageService.addMessage(message);//将信息存入库
    }

    private void sendVoiceByChannel(MessageV2 message, Channel channel, boolean life) {
        String path = "/test/sphy/voi/";//路径
        String voiname = message.getTimestamp() + message.getFromUsername() + getDataTypeToVoice(message.getDataType());//名

        poolExecutor.execute(new Runnable() {//异步存入本地
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

        if (life) {//在线直接发送
            message.setReading(true);//设置为已读
            sendMessageByChannel(channel, message);
        }
        message.setContent(path+voiname);//将存入数据库的聊天内容改为文件的路径
        messageService.addMessage(message);//将信息存入库
    }

    private void sendVideoByChannel(MessageV2 message, Channel channel, boolean life) {
        String path = "/test/sphy/vid/";//路径
        String vidname = message.getTimestamp() + message.getFromUsername() +getDataTypeToVideo(message.getDataType());//名

        poolExecutor.execute(new Runnable() {//异步存入本地
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

        if (life) {//在线直接发送
            message.setReading(true);//设置为已读
            sendMessageByChannel(channel, message);
        }
        message.setContent(path+vidname);//将存入数据库的聊天内容改为文件的路径
        messageService.addMessage(message);//将信息存入库
    }


    private void sendMessageByChannelV2(Channel channel, MessageV2 message) {
        if (!message.getMessageType().equals(MessageType.TEXT.name())){//若数据类型不是文字
            try {
                InputStream in=new FileInputStream(message.getContent());//获取文件路径并创建输入流
                int len;
                byte[] bs=new byte[1024];
                StringBuilder buffer=new StringBuilder();
                while ((len=in.read(bs))!=-1) {
                    buffer.append(new String(bs,0,len));//从本地读数据
                }
                in.close();
                message.setContent(buffer.toString());//将路径替换为数据
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        channel.writeAndFlush(new TextWebSocketFrame(new Gson().toJson(message)));//以json的格式发送数据
    }

    private void sendMessageByChannel(Channel channel, MessageV2 message) {
        channel.writeAndFlush(new TextWebSocketFrame(new Gson().toJson(message)));//若为文本聊天则直接发送不需要判断
    }

    private void sendMessageForAll(MessageV2 message) {
        for (Channel channel : channelGroup) {//发送给聊天里的所有人
            channel.writeAndFlush(new TextWebSocketFrame(new Gson().toJson(message)));
        }
    }

    private String getDataTypeToVideo(String name){//获取文件后缀
        String type=null;
        if (DataType.AVI.name().equals(name)) type=DataType.AVI.getValue();
        if (DataType.MOV.name().equals(name)) type=DataType.MOV.getValue();
        if (DataType.MP4.name().equals(name)) type=DataType.MP4.getValue();
        return type;
    }

    private String getDataTypeToVoice(String name){//获取文件后缀
        String type=null;
        if (DataType.MP3.name().equals(name)) type=DataType.MP3.getValue();
        if (DataType.MPEG.name().equals(name)) type=DataType.MPEG.getValue();
        return type;
    }

    private String getDataTpeToPicture(String name){//获取文件后缀
        String type=null;
        if (DataType.JPEG.name().equals(name)) type=DataType.JPEG.getValue();
        if (DataType.JPG.name().equals(name)) type=DataType.JPG.getValue();
        if (DataType.PNG.name().equals(name)) type=DataType.PNG.getValue();
        if (DataType.TIF.name().equals(name)) type=DataType.TIF.getValue();
        if (DataType.GIF.name().equals(name)) type=DataType.GIF.getValue();
        if (DataType.BMP.name().equals(name)) type=DataType.BMP.getValue();
        return type;
    }
}
