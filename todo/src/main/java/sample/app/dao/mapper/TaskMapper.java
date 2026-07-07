package sample.app.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import sample.app.dao.entity.Task;

/**
 * タスクMapper
 * SQLはresources/mapper/TaskMapper.xmlに記述
 */
@Mapper
public interface TaskMapper {

    List<Task> findAll();

    Task findById(Long id);

    List<Task> findByUserId(Long userId);

    void insert(Task task);

    void update(Task task);

    void delete(Long id);
}