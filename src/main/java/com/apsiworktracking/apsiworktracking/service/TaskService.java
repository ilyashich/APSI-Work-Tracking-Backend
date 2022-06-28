package com.apsiworktracking.apsiworktracking.service;


import com.apsiworktracking.apsiworktracking.model.*;
import com.apsiworktracking.apsiworktracking.repository.ProjectReposioty;
import com.apsiworktracking.apsiworktracking.repository.TaskRepository;
import com.apsiworktracking.apsiworktracking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectReposioty projectReposioty;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectService projectService;

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

    public List<TaskProject> getAllTaskProject (String username) {
        List<Project> projects = getAllUserProjectsManEmp(username);
        List<Task> tasks = new ArrayList<>();
        for(Project project: projects) {
            tasks.addAll(project.getTasks());
        }
        List<TaskProject> taskProjectList = new ArrayList<>();
        for(Task task: tasks) {
            TaskProject taskProject = new TaskProject();
            taskProject.setTaskId(task.getTaskId());
            Project project = projectReposioty.getById(task.getProject().getProjectId());
            String name = project.getName();
            name += " - ";
            name += task.getName();
            taskProject.setName(name);
            taskProjectList.add(taskProject);
        }
        return taskProjectList;
    }

    public List<Project> getAllUserProjectsManEmp(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        Calendar calendar = Calendar.getInstance();
        List<Project> result = new ArrayList<Project> ();
        Map<Long,Project> map = new HashMap<>();
        Set<ProjectDetail> projectDetails = user.getProjects();
        for(ProjectDetail project: projectDetails) {
            if(calendar.getTime().after(project.getStartDate()) && calendar.getTime().before(project.getEndDate())) {
                Project pro = projectService.getProject(project.getProject().getProjectId());
                if(!map.containsKey(pro.getProjectId())) {
                    map.put(pro.getProjectId(), pro);
                }
            }


        }
        for(Project project: map.values()) {
            result.add(project);
        }

        return result;
    }
}
