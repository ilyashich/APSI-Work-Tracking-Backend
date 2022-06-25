package com.apsiworktracking.apsiworktracking.model;


import com.fasterxml.jackson.annotation.*;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "task", schema = "public")
@JsonIgnoreProperties({"project"})
public class Task implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private Long taskId;
    @Column(name = "name")
    private String name;
    @JsonProperty("description")
    @Column(name = "description")
    private String description;
    @Column(name = "time")
    private Double time;
    @ManyToOne
    @JoinColumn(name = "project_fk")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "projectId", scope = Project.class)
    private Project project;
    @OneToMany(mappedBy = "task")
//    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "jobId", scope = Job.class)
    private Set<Job> jobs;
}
