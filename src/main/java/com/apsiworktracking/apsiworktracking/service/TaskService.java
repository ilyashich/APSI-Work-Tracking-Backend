package com.apsiworktracking.apsiworktracking.service;


import com.apsiworktracking.apsiworktracking.model.Job;
import com.apsiworktracking.apsiworktracking.model.JobStateEnum;
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
        Task task = taskRepository.findById(id).orElse(null);
        if (task != null & task.getTaskId() != null){
            setHoursForTask(id);
        }
        return task;
    }

    public List<Task> getAllTask () {
        List<Task> tasks = taskRepository.findAll();
        for (Task task: tasks) {
            setHoursForTask(task.getTaskId());
        }
        return tasks;
    }

    @Transactional
    public void createTask(Long projectId, Task task) {
        Project project = projectReposioty.getById(projectId);

        project.getTasks().add(task);
        task.setProject(project);

        Double time = 0.0;
        if (task.getJobs() != null) {
            for (Job job: task.getJobs()) {
                if(!JobStateEnum.REJECTED.equals(job.getState()) && !JobStateEnum.REJECTED_BY_CLIENT.equals(job.getState())) {
                    time += job.getTime();
                }
            }
        }
        task.setTime(time);

        projectReposioty.save(project);
        taskRepository.save(task);
    }

//    public void deleteTask(Long id) {
//        taskRepository.deleteById(id);
//    }
//
    public void updateTask(Long id, Task task) {
        Task taskToUpdate = taskRepository.getById(id);
        if (taskToUpdate == null) {
            throw new EntityExistsException("Task with this id does not exist");
        }
        taskToUpdate.setName(task.getName());
        taskToUpdate.setDescription(task.getDescription());

        Double time = 0.0;
        if (task.getJobs() != null) {
            for (Job job: task.getJobs()) {
                if(!JobStateEnum.REJECTED.equals(job.getState()) && !JobStateEnum.REJECTED_BY_CLIENT.equals(job.getState())) {
                    time += job.getTime();
                }
            }
        }
        taskToUpdate.setTime(time);
        taskToUpdate.setJobs(task.getJobs());
//        taskTuUpdate.setProject(task.getProject());
        taskRepository.save(taskToUpdate);
    }

    public Set<Job> getAllJobsForTask (Long id) {
        Task task = taskRepository.getById(id);
        return task.getJobs();
    }

    public void setHoursForTask(Long id) {
        Task task = taskRepository.getById(id);
        Double time = 0.0;
        Set<Job> jobs = task.getJobs();
        for(Job job: jobs) {
            if(!JobStateEnum.REJECTED.equals(job.getState()) && !JobStateEnum.REJECTED_BY_CLIENT.equals(job.getState())) {
                time += job.getTime();
            }
        }
        task.setTime(time);
        taskRepository.save(task);
    }

    public void updateTime(Long id, Double time) {
        Task task = taskRepository.getById(id);
        task.setTime(task.getTime() + time);
        taskRepository.save(task);
    }
}
