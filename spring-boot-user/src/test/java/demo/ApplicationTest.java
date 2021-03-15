package demo;


import com.easyarch.Application;
import com.easyarch.entity.Log;
import com.easyarch.entity.User;
import com.easyarch.service.Log.LogServie;
import com.easyarch.service.Users.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class ApplicationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private LogServie logServie;

    @Resource
    private RedisTemplate<String, User> redisTemplate;

    @Before
    public void before(){
        userService.deleteAllUser();
    }

    @Test
    public void testadduser(){
        //新建用户
        userService.addUser(new User("yxl","qwer","123456"));
        userService.addUser(new User("yxl","asdf","123456"));
        userService.addUser(new User("yxl","zxcv","123456"));
        userService.addUser(new User("yxl","qwer","123456"));
        User user=new User("wy","wy0705","16638375481");
        user.setTel("16638375481");
        DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        user.setBirthday(df.format(new Date()));
        userService.addUser(user);
        Assert.assertEquals(4,userService.getAllUser().intValue());
        //修改密码
        Assert.assertEquals(true,userService.updatePassword("qwer","123456","121212"));
        //注销用户
        userService.deleteUser("asdf","123456");
        Assert.assertEquals(3,userService.getAllUser().intValue());
        //查询用户
        User u=userService.findByUsernameAndPassword("qwer","121212");
        Assert.assertEquals(DigestUtils.md5DigestAsHex("121212".getBytes()),u.getPassword());
        //修改用户信息
        User un=new User();
        un.setName("xinlei.yu");
        Assert.assertEquals(true,userService.updateByUsernameAndPassword("zxcv","123456",un));
    }

    @Test
    public void testaddlog(){
        Log log=new Log();
        log.setLogOp("aaa");
        log.setLogType("bbb");
        log.setUserId(1);
        log.setCreateTime(System.currentTimeMillis());
        log.setUrl("localhost");
        logServie.addLog(log);
    }
}
