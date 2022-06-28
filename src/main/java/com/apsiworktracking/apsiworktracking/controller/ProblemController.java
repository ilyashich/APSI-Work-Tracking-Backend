package com.apsiworktracking.apsiworktracking.controller;


import com.apsiworktracking.apsiworktracking.model.Problem;
import com.apsiworktracking.apsiworktracking.model.Project;
import com.apsiworktracking.apsiworktracking.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins={"https://jolly-field-05989fe03.1.azurestaticapps.net", "http://localhost:4200"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/problem")
public class ProblemController {

    @Autowired
    private ProblemService problemService;


    @GetMapping("/all")
    public List<Problem> getAllProblems() {
        return problemService.getAllProblems();
    }

    @GetMapping("/get/{id}")
    public Problem getProblemById(@PathVariable("id") Long id) {
        return problemService.getProblem(id);
    }
}
