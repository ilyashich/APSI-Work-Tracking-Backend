package com.apsiworktracking.apsiworktracking.controller;


import com.apsiworktracking.apsiworktracking.model.Job;
import com.apsiworktracking.apsiworktracking.model.Project;
import com.apsiworktracking.apsiworktracking.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins={"https://ashy-ground-0223e9e03.1.azurestaticapps.net", "http://localhost:4200"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/job")
public class JobController {

    @Autowired
    private JobService jobService;

    @PostMapping("/create")
    public Job createJob(@RequestBody Job job)
    {
        return jobService.createJob(job);
    }

    @GetMapping("/get/{id}")
    public Job getJob (@PathVariable("id") Long id) {
        return jobService.getJob(id);
    }

    @GetMapping("/all")
    public List<Job> getAllJobs() {
        return jobService.getAllJobs();
    }

    @DeleteMapping("/delete/{id}")
    public void deleteJob (@PathVariable("id") Long id) {
        jobService.deleteJob(id);
    }

    @PutMapping("/update/{id}")
    public Job updateJob(@PathVariable("id") Long id, @RequestBody Job job) {
        return jobService.updateJob(id, job);
    }

    @GetMapping("/all_to_accept")
    public List<Job> getAllJobsToAccept() {
        return jobService.getAllJobsaToBeAccepted();
    }

    @GetMapping("/all_rejected_by_client")
    public List<Job> getAllJobsRejectedByClient() {
        return jobService.getAllJobsRejectedByClient();
    }
}
