package world.lixiang.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import world.lixiang.dao.UserFilesMapper;
import world.lixiang.entity.User;
import world.lixiang.entity.UserFiles;
import world.lixiang.service.UserFilesService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class UserFilesServiceImpl implements UserFilesService {

    @Resource
    private UserFilesMapper userFilesMapper;

    @Override
    public List<UserFiles> findAllById(Integer id) {
        return userFilesMapper.findAllById(id);
    }

    @Override
    public void addUserFiles(UserFiles userFiles) {
        String isImg =  userFiles.getType();
        if(isImg.startsWith("image")){
            userFiles.setIsimg("是");
        }else{
            userFiles.setIsimg("否");
        }

        userFiles.setDowncounts(0);
        userFiles.setUploadTime(new Date());

        userFilesMapper.addUserFiles(userFiles);
    }

    @Override
    public UserFiles findById(String id) {
        return userFilesMapper.findById(id);
    }

    @Override
    public void update(UserFiles userFiles) {
        userFilesMapper.update(userFiles);
    }

    @Override
    public void delete(String id) {
        userFilesMapper.delete(id);
    }
}
