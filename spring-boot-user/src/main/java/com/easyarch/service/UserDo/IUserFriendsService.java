package com.easyarch.service.UserDo;

import com.easyarch.entity.UserFriend;

import java.util.List;

public interface IUserFriendsService {
    /*
     * errerx信息
     * 返回值
     * 0：执行成功
     * 1：没有该用户
     * 2：空指针异常
     * 3：没有该好友
     * */

    int addFriendbyUserid(String userid, String addid, String name,String other);

    int addFriendbyUsername(String username, String addid, String name,String other);

    int deleteFriend(String userid, String addid);

    int updateFriend(String userid, int changes, String addid,String data);//1：昵称；   2：备注 ； 等等

    int deleteUsers(String addid);

    List<UserFriend> getFriends(String addid);
}
