package com.easyarch.controller;

import com.easyarch.annotation.LogRecord;
import com.easyarch.dao.UserFriendsdao;
import com.easyarch.entity.Resp;
import com.easyarch.service.UserDo.UserFriendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/friends")
public class UserFriendsController {


    @Autowired
    private UserFriendsService friendsService;

    @PostMapping(value = "addfriendbyid")
    @LogRecord(operation = "好友操作", type = "添加好友")
    public Resp addFriendbyUserid(@RequestParam(name = "userid") String userid, @RequestParam(name = "addid") String addid,
                                  @RequestParam(name = "name") String name, @RequestParam(name = "other") String other) {
        return getResp(friendsService.addFriendbyUserid(userid, addid, name, other));
    }

    @PostMapping(value = "addfriendbyusername")
    @LogRecord(operation = "好友操作", type = "添加好友")
    public Resp addFriendbyUsername(@RequestParam(name = "username") String userid, @RequestParam(name = "addid") String addid,
                                    @RequestParam(name = "name") String name, @RequestParam(name = "other") String other) {
        return getResp(friendsService.addFriendbyUsername(userid, addid, name, other));
    }

    @PostMapping(value = "updatename")
    @LogRecord(operation = "好友操作", type = "修改昵称")
    public Resp updateName(){
        return null;
    }

    /*
     * errerx信息
     * 返回值
     * 0：执行成功
     * 1：没有该用户
     * 2：空指针异常
     * 3：没有该好友
     * 4：表创建失败
     * 5：表删除失败
     * 6：执行失败
     * 7：选择有误
     * 8：已有该好友
     * */
    private Resp getResp(int i) {
        Resp resp;
        switch (i) {
            case 0: {
                resp = new Resp("200", "success", "执行成功", null, null);
                return resp;
            }
            case 1: {
                resp = new Resp("400", "fail", "没有该用户", null, null);
                return resp;
            }
            case 2: {
                resp = new Resp("500", "fail", "空指针异常", null, null);
                return resp;
            }
            case 3: {
                resp = new Resp("400", "fail", "没有该好友", null, null);
                return resp;
            }
            case 4: {
                resp = new Resp("500", "fail", "表创建失败", null, null);
                return resp;
            }
            case 5: {
                resp = new Resp("500", "fail", "表删除失败", null, null);
                return resp;
            }
            case 6: {
                resp = new Resp("400", "fail", "执行失败", null, null);
                return resp;
            }
            case 7: {
                resp = new Resp("400", "fail", "选择有误", null, null);
                return resp;
            }
            case 8: {
                resp = new Resp("400", "fail", "已有该好友", null, null);
                return resp;
            }
            default: {
                resp = new Resp("400", "fail", "预期之外的错误", null, null);
                return resp;
            }

        }
    }
}
