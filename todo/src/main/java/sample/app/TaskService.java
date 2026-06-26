package sample.app;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import sample.app.dao.entity.Task;
import sample.app.dao.mapper.TaskMapper;

/**
 * タスク管理サービス
 * タスクのビジネスロジックを提供する
 */
@org.springframework.stereotype.Service
public class TaskService {

    private final TaskMapper taskMapper;

    // コンストラクタ注入
    public TaskService(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    /** ユーザーIDでタスク一覧を取得 */
    @Transactional(readOnly = true)
    public List<Task> getTasksByUserId(Integer userId) {
        return taskMapper.findByUserId(userId);
    }

    /** IDでタスクを1件取得 */
    @Transactional(readOnly = true)
    public Task getTaskById(Integer id) {
        return taskMapper.findById(id);
    }

    /** タスクを登録 */
    @Transactional
    public void createTask(Task task) {
        taskMapper.insert(task);
    }

    /** タスクを更新（所有者チェックあり） */
    @Transactional
    public boolean updateTask(Task task, Integer loginUserId) {
        Task existing = taskMapper.findById(task.getId());
        if (existing == null || !existing.getUserId().equals(loginUserId)) {
            return false;
        }
        taskMapper.update(task);
        return true;
    }

    /** タスクを削除（所有者チェックあり） */
    @Transactional
    public boolean deleteTask(Integer id, Integer loginUserId) {
        Task existing = taskMapper.findById(id);
        if (existing == null || !existing.getUserId().equals(loginUserId)) {
            return false;
        }
        taskMapper.delete(id);
        return true;
    }
}