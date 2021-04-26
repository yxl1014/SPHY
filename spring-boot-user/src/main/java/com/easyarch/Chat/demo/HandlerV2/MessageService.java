package com.easyarch.Chat.demo.HandlerV2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Autowired
    private MessageDao messageDao;


}
