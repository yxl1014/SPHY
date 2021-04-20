package com.easyarch.using.service.Log;

import com.easyarch.using.dao.Logdao;
import com.easyarch.using.entity.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogServie implements ILogService{
    @Autowired
    private Logdao logdao;

    @Override
    public void addLog(Log log) {
        logdao.addLog(log);
    }
}
