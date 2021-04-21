package demo;


import com.easyarch.Application;
import com.easyarch.using.entity.Log;
import com.easyarch.using.entity.User;
import com.easyarch.using.service.Log.LogServie;
import com.easyarch.using.service.Users.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

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
       /* userService.deleteAllUser();*/
    }

    @Test
    public void testadduser(){
        //新建用户
        /*userService.addUser(new User("yxl","qwer","123456"));
        userService.addUser(new User("yxl","asdf","123456"));
        userService.addUser(new User("yxl","zxcv","123456"));
        userService.addUser(new User("yxl","qwer","123456"));*/
    }

    @Test
    public void testaddlog(){
        /*Log log=new Log();
        log.setLogOp("aaa");
        log.setLogType("bbb");
        log.setUserId("1");
        log.setCreateTime(System.currentTimeMillis());
        log.setUrl("localhost");
        logServie.addLog(log);*/
    }
}
