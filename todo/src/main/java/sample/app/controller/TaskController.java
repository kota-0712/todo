package sample.app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import sample.app.TaskService;
import sample.app.dao.entity.Task;
import sample.app.dao.mapper.UserMapper;

/**
 * タスク管理コントローラー
 * タスクの一覧・登録・編集・削除・API機能を提供する
 */
@Controller
@RequestMapping("/tasks")
public class TaskController {

	private final TaskService taskService;
	private final UserMapper userMapper;

	// コンストラクタ注入
	public TaskController(TaskService taskService, UserMapper userMapper) {
	    this.taskService = taskService;
	    this.userMapper = userMapper;
	}
    /** ログイン中のユーザーIDを取得する */
    private Integer getLoginUserId(UserDetails userDetails) {
        return userMapper.findByUsername(userDetails.getUsername()).getId();
    }

    /** タスク一覧画面 */
    @GetMapping
    public String list(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<Task> tasks = taskService.getTasksByUserId(getLoginUserId(userDetails));
        model.addAttribute("tasks", tasks);
        return "taskList";
    }

    /** タスク登録フォーム表示 */
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("task", new Task());
        return "taskForm";
    }

    /** タスク登録処理 */
    @PostMapping("/new")
    public String create(@Validated @ModelAttribute Task task,
                         BindingResult result,
                         @AuthenticationPrincipal UserDetails userDetails,
                         Model model) {
        if (result.hasErrors()) {
            return "taskForm";
        }
        task.setUserId(getLoginUserId(userDetails));
        taskService.createTask(task);
        return "redirect:/tasks";
    }

    /** タスク編集フォーム表示 */
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model,
                           @AuthenticationPrincipal UserDetails userDetails) {
        Task task = taskService.getTaskById(id);
        if (task == null || !task.getUserId().equals(getLoginUserId(userDetails))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "タスクが見つかりません");
        }
        model.addAttribute("task", task);
        return "taskEdit";
    }

    /** タスク更新処理 */
    @PostMapping("/update/{id}")
    public String update(@PathVariable Integer id,
                         @Validated @ModelAttribute Task task,
                         BindingResult result,
                         @AuthenticationPrincipal UserDetails userDetails,
                         Model model) {
        if (result.hasErrors()) {
            return "taskEdit";
        }
        task.setId(id);
        boolean success = taskService.updateTask(task, getLoginUserId(userDetails));
        if (!success) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "タスクが見つかりません");
        }
        return "redirect:/tasks";
    }

    /** タスク削除処理 */
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id,
                         @AuthenticationPrincipal UserDetails userDetails) {
        boolean success = taskService.deleteTask(id, getLoginUserId(userDetails));
        if (!success) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "タスクが見つかりません");
        }
        return "redirect:/tasks";
    }

    /** タスク一覧をJSON形式で返すAPI */
    @GetMapping(value = "/api", produces = "application/json")
    @ResponseBody
    public List<Task> apiList(@AuthenticationPrincipal UserDetails userDetails) {
        return taskService.getTasksByUserId(getLoginUserId(userDetails));
    }
}