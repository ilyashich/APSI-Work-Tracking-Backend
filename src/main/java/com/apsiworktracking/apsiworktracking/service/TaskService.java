package com.apsiworktracking.apsiworktracking.service;


import com.apsiworktracking.apsiworktracking.model.Job;
import com.apsiworktracking.apsiworktracking.model.Project;
import com.apsiworktracking.apsiworktracking.model.Task;
import com.apsiworktracking.apsiworktracking.repository.ProjectReposioty;
import com.apsiworktracking.apsiworktracking.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectReposioty projectReposioty;

    public Task getTask (Long id){
        return taskRepository.findById(id).orElse(null);
    }

    public List<Task> getAllTask () {
        return taskRepository.findAll();
    }

    @Transactional
    public void createTask(Long projectId, Task task) {
        Project project = projectReposioty.getById(projectId);

        project.getTasks().add(task);
        task.setProject(project);

        projectReposioty.save(project);
        taskRepository.save(task);
    }

//    public void deleteTask(Long id) {
//        taskRepository.deleteById(id);
//    }
//
//    public void updateTask(Long id, Task task) {
//        Task taskTuUpdate = taskRepository.getById(id);
//        if (taskTuUpdate == null) {
//            throw new EntityExistsException("Task with this id does not exist");
//        }
//        taskTuUpdate.setName(task.getName());
//        taskTuUpdate.setDescription(task.getDescription());
////        taskTuUpdate.setProject(task.getProject());
//        taskRepository.save(taskTuUpdate);
//    }

    public Set<Job> getAllJobsForTask (Long id) {
        Task task = taskRepository.getById(id);
        return task.getJobs();
    }
}
