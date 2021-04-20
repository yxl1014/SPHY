package com.easyarch.error.service;

import com.easyarch.error.dao.Errordao;
import com.easyarch.error.entity.Erormessage;
import org.springframework.beans.factory.annotation.Autowired;

public class Errorservice {

    @Autowired
    private Errordao errordao;

    public void insert(Erormessage message) {
        errordao.insert(message);
    }
}
