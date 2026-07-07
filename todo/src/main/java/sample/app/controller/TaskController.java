package sample.app.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import sample.app.TaskService;
import sample.app.UserService;
import sample.app.dao.entity.Task;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    private Long getLoginUserId(UserDetails userDetails) {
        return userService.findByUsername(userDetails.getUsername()).getId();
    }

    /** タスク一覧 */
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
    public String create(@Valid @ModelAttribute("task") Task task,
                          BindingResult bindingResult,
                          @AuthenticationPrincipal UserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            return "taskForm";
        }
        task.setUserId(getLoginUserId(userDetails));
        taskService.createTask(task);
        return "redirect:/tasks";
    }

    /** タスク編集フォーム表示 */
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model,
                            @AuthenticationPrincipal UserDetails userDetails) {
        Task task = taskService.getTaskById(id);
        if (task == null || !task.getUserId().equals(getLoginUserId(userDetails))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        model.addAttribute("task", task);
        return "taskEdit";
    }

    /** タスク更新処理 */
    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id,
                          @Valid @ModelAttribute("task") Task task,
                          BindingResult bindingResult,
                          @AuthenticationPrincipal UserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            return "taskEdit";
        }
        task.setId(id);
        boolean success = taskService.updateTask(task, getLoginUserId(userDetails));
        if (!success) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/tasks";
    }
    /** タスク削除処理 */
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                          @AuthenticationPrincipal UserDetails userDetails) {
        boolean success = taskService.deleteTask(id, getLoginUserId(userDetails));
        if (!success) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/tasks";
    }
}
   