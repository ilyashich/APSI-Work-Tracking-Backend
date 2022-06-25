package com.apsiworktracking.apsiworktracking.model;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "job", schema = "public")
@JsonIgnoreProperties({"employee"})
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private Long jobId;
    @Column(name = "name")
    private String name;
    @JsonProperty("description")
    @Column(name = "description")
    private String description;
    @Column(name = "time")
    private Double time;
    @Column(name = "data")
    private Date date;
    @Column(name = "state")
    private JobStateEnum state;
    @Column(name = "rejection_res")
    private String rejectionReason;
    @Column(name = "type")
    private JobTypeEnum type;
    @Column(name = "url")
    private String documentUrl;
    @ManyToOne
    @JoinColumn(name = "problem_fk")
    @JsonIdentityInfo(scope = Problem.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "problemId")
    private Problem problem;
    @ManyToOne
    @JoinColumn(name = "user_fk")
    @JsonIdentityInfo(scope = User.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "task_fk")
    @JsonIdentityInfo(scope = Task.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "taskId")
    private Task task;




}
