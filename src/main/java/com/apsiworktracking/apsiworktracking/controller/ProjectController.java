package com.apsiworktracking.apsiworktracking.controller;


import com.apsiworktracking.apsiworktracking.model.Job;
import com.apsiworktracking.apsiworktracking.model.Project;
import com.apsiworktracking.apsiworktracking.model.User;
import com.apsiworktracking.apsiworktracking.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins={"https://ashy-ground-0223e9e03.1.azurestaticapps.net", "http://localhost:4200"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping("/create")
    public Project createProject(@RequestBody Project project)
    {
        return projectService.createProject(project);
    }

    @GetMapping("/all")
    public List<Project> getAllProjects()
    {
        return projectService.getAllProjects();
    }

    @GetMapping("/get/{id}")
    public Project getProjectById(@PathVariable("id") Long id) {
        return projectService.getProject(id);
    }

    @PostMapping("/add/project/{project_id}/user/{user_id}")
    public void addUserToProject(@PathVariable("project_id") Long projectId, @PathVariable("user_id") Long userId){

        projectService.addUserToProject(projectId, userId);

    }

    @DeleteMapping("/delete/{id}")
    public void deleteProjects (@PathVariable("id") Long id)  {
        projectService.deleteProject(id);
    }

    @PutMapping("/update/{id}")
    public void updateProject(@PathVariable("id") Long id, @RequestBody Project project) {
        projectService.updateProject(id, project);
    }

    @GetMapping("/{id}/jobs")
    public List<Job> getAllJobsForProject(@PathVariable("id") Long id) {
        return projectService.getAllJobsForProject(id);
    }
}
