package com.easyarch.service.UserDo;

import com.easyarch.dao.Userdao;
import com.easyarch.dao.UserFriendsdao;
import com.easyarch.entity.User;
import com.easyarch.entity.UserFriend;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserFriendsService implements IUserFriendsService {
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
     * */

    @Autowired
    private Userdao userdao;

    @Autowired
    private UserFriendsdao userFriendsdao;

    @Override
    public int addFriend(String userid, String addid, String name, String other) {
        if (userid == null || userid.equals("")
                || addid == null || addid.equals("")
                || name == null || name.equals(""))//空指针判断
            return 2;
        User user = userdao.findbyId(userid);//查询这个用户

        if (user == null)//查看该用户是否存在
            return 1;

        if (!userFriendsdao.findtable(addid)) {//查询这个用户的好友库是不是存在
            if (!userFriendsdao.createUsersTable(addid))//若不存在则创建一个数据库
                return 4;
        }

        if (!userFriendsdao.addUsers(userid, addid, name, other))//添加好友
            return 6;
        return 0;
    }

    @Override
    public int deleteFriend(String userid, String addid) {
        if (userid == null || userid.equals("")
                || addid == null || addid.equals(""))//空指针判断
            return 2;

        if (userFriendsdao.deleteFriend(userid, addid))//删除好友
            return 0;
        else
            return 6;
    }

    @Override
    public int updateFriend(String userid, int changes, String addid,String data) {//1：昵称；   2：备注 ； 等等
        if (userid == null || userid.equals("")
                || addid == null || addid.equals(""))//空指针判断
            return 2;
        switch (changes){
            case 1:{
                if(userFriendsdao.updateName(addid,userid,data))//更改昵称
                    return 0;
                else
                    return 6;
            }
            case 2:{
                if(userFriendsdao.updateOther(addid,userid,data))//更改备注
                    return 0;
                else
                    return 6;
            }
            default:
                return 7;
        }
    }

    @Override
    public int deleteUsers(String addid) {
        if (addid == null || addid.equals(""))//空指针判断
            return 2;
        if (userFriendsdao.dropTalbe(addid))//删除
            return 0;
        else
            return 5;
    }

    @Override
    public List<UserFriend> getFriends(String addid) {
        if (addid == null || addid.equals(""))//空指针判断
            return null;
        List<UserFriend> list=userFriendsdao.getALlFriend(addid);
        return list;
    }
}