package com.easyarch.using.dao;

import com.easyarch.using.entity.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class Logdao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addLog(Log log){
        String sql="insert into logs(log_op,log_type,log_userid,log_createtime,log_url) values(?,?,?,?,?)";
        jdbcTemplate.update(sql,log.getLogOp(),log.getLogType(),log.getUserId(),log.getCreateTime(),log.getUrl());
    }
}
