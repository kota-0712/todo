package sample.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import sample.app.TaskService;
import sample.app.dao.entity.Task;
import sample.app.dao.mapper.UserMapper;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserMapper userMapper;

    private Integer getLoginUserId(UserDetails userDetails) {
        return userMapper.findByUsername(userDetails.getUsername()).getId();
    }

    @GetMapping
    public String list(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Integer userId = getLoginUserId(userDetails);
        List<Task> tasks = taskService.getTasksByUserId(userId);
        model.addAttribute("tasks", tasks);
        return "taskList";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("task", new Task());
        return "taskForm";
    }

    @PostMapping("/new")
    public String create(@Validated @ModelAttribute Task task,
                         BindingResult result,
                         @AuthenticationPrincipal UserDetails userDetails,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("task", task);
            return "taskForm";
        }
        task.setUserId(getLoginUserId(userDetails));
        taskService.createTask(task);
        return "redirect:/tasks";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model,
                           @AuthenticationPrincipal UserDetails userDetails) {
        Task task = taskService.getTaskById(id);
        if (task == null || !task.getUserId().equals(getLoginUserId(userDetails))) {
            return "redirect:/tasks";
        }
        model.addAttribute("task", task);
        return "taskEdit";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Integer id,
                         @Validated @ModelAttribute Task task,
                         BindingResult result,
                         @AuthenticationPrincipal UserDetails userDetails,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("task", task);
            return "taskEdit";
        }
        task.setId(id);
        taskService.updateTask(task, getLoginUserId(userDetails));
        return "redirect:/tasks";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id,
                         @AuthenticationPrincipal UserDetails userDetails) {
        taskService.deleteTask(id, getLoginUserId(userDetails));
        return "redirect:/tasks";
    }

    @GetMapping(value = "/api", produces = "application/json")
    @ResponseBody
    public List<Task> apiList(@AuthenticationPrincipal UserDetails userDetails) {
        return taskService.getTasksByUserId(getLoginUserId(userDetails));
    }
}