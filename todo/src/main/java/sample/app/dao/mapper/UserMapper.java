package sample.app.dao.mapper;

import org.apache.ibatis.annotations.Mapper;

import sample.app.dao.entity.User;

/**
 * ユーザーMapper
 * SQLはresources/mapper/UserMapper.xmlに記述
 */
@Mapper
public interface UserMapper {

    User findByUsername(String username);

    User findById(Long id);

    void insert(User user);
}