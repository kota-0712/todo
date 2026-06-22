package sample.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sample.app.dao.entity.Task;
import sample.app.dao.mapper.TaskMapper;

@Service
public class TaskService {

    @Autowired
    private TaskMapper taskMapper;

    public List<Task> getTasksByUserId(Integer userId) {
        return taskMapper.findByUserId(userId);
    }

    public Task getTaskById(Integer id) {
        return taskMapper.findById(id);
    }

    public void createTask(Task task) {
        taskMapper.insert(task);
    }

    public boolean updateTask(Task task, Integer loginUserId) {
        Task existing = taskMapper.findById(task.getId());
        if (existing == null || !existing.getUserId().equals(loginUserId)) {
            return false;
        }
        taskMapper.update(task);
        return true;
    }

    public boolean deleteTask(Integer id, Integer loginUserId) {
        Task existing = taskMapper.findById(id);
        if (existing == null || !existing.getUserId().equals(loginUserId)) {
            return false;
        }
        taskMapper.delete(id);
        return true;
    }
}