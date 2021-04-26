package com.easyarch.Chat.demo.HandlerV2;


import com.easyarch.Chat.demo.HandlerV2.entity.MessageV2;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MessageDao {

    private JdbcTemplate jdbcTemplate;

    public List<MessageV2> findNoreadingByToFromUsename(String fromusername, String tousername) {//查询数据库中没有发送的数据
        List<MessageV2> list;
        String sql = "select mid,uid,to_username,from_username,context,message_type,data_type,reading,can_send,m_timestamp" +
                " from messages where reading=false and from_usernmae = ? and to_username = ? order by m_timestamp)";
        list = jdbcTemplate.queryForList(sql, MessageV2.class, fromusername, tousername);
        return list;
    }

    public List<MessageV2> findAllMessageByToFron(String fromusername, String tousername) {
        List<MessageV2> list;
        String sql = "mid,uid,to_username,from_username,context,message_type,data_type,reading,can_send,m_timestamp" +
                " from messages where from_usernmae = ? and to_username = ? order by mid";
        list = jdbcTemplate.queryForList(sql, MessageV2.class, fromusername, tousername);
        return list;
    }

    public int setReadingToTrue(String tousername) {//将所有信息都设为已发送
        String sql = "update messages set reading = true where to_username = ?";
        /*int updates=*/
        return jdbcTemplate.update(sql, tousername);
        //System.out.println(updates);
    }

    public boolean deleteByMid(String mid) {//撤回
        String sql = "delete from messages where mid=?";
        return jdbcTemplate.update(sql, mid) == 1 ? true : false;
    }

    public boolean addMessage(MessageV2 message) {
        String sql = "insert into messages(uid,to_username,from_username,context,message_type,data_type,reading,can_send,m_timestamp) values(?,?,?,?,?,?,?,?,?)";

        return jdbcTemplate.update(sql, message.getUid(), message.getToUsername(), message.getFromUsername(), message.getContent(), message.getMessageType()
                , message.getDataType(), message.isReading(), message.isCanSend(), message.getTimestamp()) == 1 ? true : false;
    }

}
