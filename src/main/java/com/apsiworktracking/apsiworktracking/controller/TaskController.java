package com.apsiworktracking.apsiworktracking.controller;


import com.apsiworktracking.apsiworktracking.model.Job;
import com.apsiworktracking.apsiworktracking.model.Task;
import com.apsiworktracking.apsiworktracking.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@CrossOrigin(origins={"https://ashy-ground-0223e9e03.1.azurestaticapps.net", "http://localhost:4200"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/create/project/{projectId}")
    public void createTask(@PathVariable("projectId") Long projectId, @RequestBody Task task) {
        taskService.createTask(projectId, task);
    }

    @GetMapping("/get/{id}")
    public Task getTask(@PathVariable("id") Long id) {
        return taskService.getTask(id);
    }

    @GetMapping("/all")
    public List<Task> getAllTasks() {
        return taskService.getAllTask();
    }

//    @PutMapping("/update/{id}")
//    public void updateTask (@PathVariable("id") Long id, @RequestBody Task task) {
//        taskService.updateTask(id, task);
//    }
//
//    @GetMapping("/delete/{id}")
//    public void deleteTask (@PathVariable("id") Long id) {
//        taskService.deleteTask(id);
//    }

    @GetMapping("/{id}/jobs")
    public Set<Job> getAllJobsForTask(@PathVariable("id") Long id) {
        return taskService.getAllJobsForTask(id);
    }
}
