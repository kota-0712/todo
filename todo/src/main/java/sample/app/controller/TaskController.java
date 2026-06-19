package sample.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import sample.app.dao.entity.Task;
import sample.app.dao.mapper.TaskMapper;
import sample.app.dao.mapper.UserMapper;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private UserMapper userMapper;

    @GetMapping
    public String list(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<Task> tasks = taskMapper.findAll();
        model.addAttribute("tasks", tasks);
        return "taskList";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("task", new Task());
        return "taskForm";
    }

    @PostMapping("/new")
    public String create(@AuthenticationPrincipal UserDetails userDetails,
                         @RequestParam String title,
                         @RequestParam String content,
                         @RequestParam String name,
                         @RequestParam String startDate,
                         @RequestParam String endDate) {
        Task task = new Task();
        task.setTitle(title);
        task.setContent(content);
        task.setName(name);
        task.setStartDate(java.time.LocalDate.parse(startDate));
        task.setEndDate(java.time.LocalDate.parse(endDate));
        Integer userId = userMapper.findByUsername(userDetails.getUsername()).getId();
        task.setUserId(userId);
        taskMapper.insert(task);
        return "redirect:/tasks";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        Task task = taskMapper.findById(id);
        model.addAttribute("task", task);
        return "taskEdit";
    }

    @PostMapping("/edit/{id}")
    public String editPost(@PathVariable Integer id,
                         @RequestParam String title,
                         @RequestParam String content,
                         @RequestParam String name,
                         @RequestParam String startDate,
                         @RequestParam String endDate) {
        Task task = taskMapper.findById(id);
        task.setTitle(title);
        task.setContent(content);
        task.setName(name);
        task.setStartDate(java.time.LocalDate.parse(startDate));
        task.setEndDate(java.time.LocalDate.parse(endDate));
        taskMapper.update(task);
        return "redirect:/tasks";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Integer id,
                         @RequestParam String title,
                         @RequestParam String content,
                         @RequestParam String name,
                         @RequestParam String startDate,
                         @RequestParam String endDate) {
        Task task = taskMapper.findById(id);
        task.setTitle(title);
        task.setContent(content);
        task.setName(name);
        task.setStartDate(java.time.LocalDate.parse(startDate));
        task.setEndDate(java.time.LocalDate.parse(endDate));
        taskMapper.update(task);
        return "redirect:/tasks";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        taskMapper.delete(id);
        return "redirect:/tasks";
    }

    @GetMapping(value = "/api", produces = "application/json")
    @ResponseBody
    public List<Task> apiList() {
        return taskMapper.findAll();
    }
}