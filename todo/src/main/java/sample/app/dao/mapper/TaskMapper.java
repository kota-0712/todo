package sample.app.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import sample.app.dao.entity.Task;

@Mapper
public interface TaskMapper {

    @Select("SELECT id, user_id AS userId, title, content, name, start_date AS startDate, end_date AS endDate, created_at AS createdAt, updated_at AS updatedAt FROM tasks ORDER BY id DESC")
    List<Task> findAll();

    @Select("SELECT id, user_id AS userId, title, content, name, start_date AS startDate, end_date AS endDate, created_at AS createdAt, updated_at AS updatedAt FROM tasks WHERE id = #{id}")
    Task findById(Integer id);

    @Insert("INSERT INTO tasks (user_id, title, content, name, start_date, end_date, created_at, updated_at) VALUES (#{userId}, #{title}, #{content}, #{name}, #{startDate}, #{endDate}, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Task task);

    @Update("UPDATE tasks SET title=#{title}, content=#{content}, name=#{name}, start_date=#{startDate}, end_date=#{endDate}, updated_at=CURRENT_TIMESTAMP WHERE id=#{id}")
    void update(Task task);

    @Delete("DELETE FROM tasks WHERE id=#{id}")
    void delete(Integer id);
    @Select("SELECT id, user_id AS userId, title, content, name, start_date AS startDate, end_date AS endDate, created_at AS createdAt, updated_at AS updatedAt FROM tasks WHERE user_id = #{userId} ORDER BY id DESC")
    List<Task> findByUserId(Integer userId);
}