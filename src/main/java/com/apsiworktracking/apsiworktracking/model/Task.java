package com.apsiworktracking.apsiworktracking.model;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "task", schema = "public")
//@JsonIgnoreProperties({"project"})
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private Long taskId;
    @Column(name = "name")
    private String name;
    @JsonProperty("description")
    @Column(name = "description")
    private String description;
    @ManyToOne
    @JoinColumn(name = "project_fk")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "projectId", scope = Long.class)
    private Project project;
    @OneToMany(mappedBy = "task")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "jobId", scope = Long.class)
    private Set<Job> jobs;
}
