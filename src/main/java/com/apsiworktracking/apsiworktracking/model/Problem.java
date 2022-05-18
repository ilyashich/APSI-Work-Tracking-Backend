package com.apsiworktracking.apsiworktracking.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "problem", schema = "public")
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
}
