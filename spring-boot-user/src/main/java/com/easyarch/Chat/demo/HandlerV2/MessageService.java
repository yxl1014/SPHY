package com.easyarch.Chat.demo.HandlerV2;

import com.easyarch.Chat.demo.HandlerV2.entity.MessageV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageDao messageDao;



    public List<MessageV2> getNoReadingList(String tousername) {
        return messageDao.findNoreadingByToFromUsename(tousername);
    }

    public void addMessage(MessageV2 message) {
        //加ｌｏｇ
        messageDao.addMessage(message);
    }
}
