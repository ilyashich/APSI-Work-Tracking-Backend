package com.apsiworktracking.apsiworktracking.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "problem", schema = "public")
@JsonIgnoreProperties({"jobs"})
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long problemId;
    @JsonProperty("name")
    @Column(name = "name")
    private String name;
    @JsonProperty("description")
    @Column(name = "description")
    private String description;
    @OneToMany(mappedBy = "problem")
//    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "jobId", scope = Long.class)
    private Set<Job> jobs;
}
