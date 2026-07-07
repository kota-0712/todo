package sample.app.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping
    public String list(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<Task> tasks = taskService.getTasksByUserId(getLoginUserId(userDetails));
        model.addAttribute("tasks", tasks);
        return "tasks/list"; // 戻り値（遷移先のビュー名）を返す必要があります
    }
}