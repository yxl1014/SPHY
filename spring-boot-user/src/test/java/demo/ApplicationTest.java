package demo;


import com.easyarch.Application;
import com.easyarch.entity.User;
import com.easyarch.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class ApplicationTest {

    @Autowired
    private UserService userService;

    @Resource
    private RedisTemplate<String, User> redisTemplate;

    @Before
    public void before(){
        userService.deleteAllUser();
    }

    @Test
    public void testadduser(){
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
    }
}
