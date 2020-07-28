package world.lixiang.service.impl;

import org.springframework.stereotype.Service;
import world.lixiang.dao.UserMapper;
import world.lixiang.entity.User;
import world.lixiang.service.UserService;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    UserMapper userMapper;

    @Override
    public User login(User user) {
        return userMapper.login(user);
    }
}
