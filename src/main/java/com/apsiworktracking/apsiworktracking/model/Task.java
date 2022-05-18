package com.apsiworktracking.apsiworktracking.model;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "task", schema = "public")
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
}
