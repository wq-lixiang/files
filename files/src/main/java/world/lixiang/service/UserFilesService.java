package world.lixiang.service;

import world.lixiang.entity.UserFiles;

import java.util.List;

public interface UserFilesService {
    List<UserFiles> findAllById(Integer id);

    void addUserFiles(UserFiles userFiles);

    UserFiles findById(String id);

    void update(UserFiles userFiles);

    void delete(String id);
}
