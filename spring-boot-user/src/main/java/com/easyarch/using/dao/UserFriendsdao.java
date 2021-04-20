package com.easyarch.using.dao;

import com.easyarch.using.entity.UserFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserFriendsdao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean addUsers(String userid, String addid, String name, String other) {
        String sql = "insert into users_" + addid + " (userid,name,other) values(?,?,?);";
        try {
            int j = jdbcTemplate.update(sql, new Object[]{userid, name, other});
            if (j != 0)
                return true;
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean findtable(String addid) {
        String sql = "select * from users_" + addid + ";";
        try {
            jdbcTemplate.execute(sql);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean dropTalbe(String addid) {
        String sql = "drop table users_" + addid + ";";
        int i = jdbcTemplate.update(sql);
        if (i != 0)
            return true;
        else
            return false;
    }

    public boolean createUsersTable(String addid) {
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE `users_" + addid + "` (");
        sb.append(" `userid` varchar(11) NOT NULL,");
        sb.append(" `name` varchar(100) DEFAULT '',");
        sb.append(" `other` varchar(255) DEFAULT '',");
        sb.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
        String sql = "insert into users_" + addid + " (userid,other) values(?,?);";
        try {
            int i = jdbcTemplate.update(sb.toString());
            int j = jdbcTemplate.update(sql, new Object[]{addid, "自己"});
            if (i != 0 && j != 0)
                return true;
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            dropTalbe(addid);
            return false;
        }
    }

    public boolean deleteFriend(String userid, String addid) {
        String sql = "delete from users_" + addid + " where userid=?;";
        try {
            int j = jdbcTemplate.update(sql, userid);
            if (j != 0)
                return true;
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateName(String addid, String userid, String data) {
        String sql = "UPDATE users_" + addid + " SET name=? WHERE userid=?";
        try {
            int j = jdbcTemplate.update(sql, new Object[]{data, userid});
            if (j != 0)
                return true;
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateOther(String addid, String userid, String data) {
        String sql = "UPDATE users_" + addid + " SET other =? WHERE userid=?";
        try {
            int j = jdbcTemplate.update(sql, new Object[]{data, userid});
            if (j != 0)
                return true;
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<UserFriend> getALlFriend(String addid) {
        String sql = "select * from USER";
        return jdbcTemplate.queryForList(sql, UserFriend.class);
    }

    public boolean findFriend(String userid, String addid) {
        String sql = "select count(1) from users_" + addid + " where userid=?;";
        try {
            Integer i = jdbcTemplate.queryForObject(sql, Integer.class, userid);
            return i != 0;
        } catch (Exception e) {
            return false;
        }
    }
}
