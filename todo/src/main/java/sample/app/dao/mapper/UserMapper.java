package sample.app.dao.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import sample.app.dao.entity.User;

@Mapper
public interface UserMapper {

    @Select("SELECT id, username, password, created_at AS createdAt FROM users WHERE username = #{username}")
    User findByUsername(String username);

    @Insert("INSERT INTO users (username, password, created_at) VALUES (#{username}, #{password}, CURRENT_TIMESTAMP)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(User user);
    @Select("SELECT id, username, password, created_at AS createdAt FROM users WHERE id = #{id}")
    User findById(Integer id);
}