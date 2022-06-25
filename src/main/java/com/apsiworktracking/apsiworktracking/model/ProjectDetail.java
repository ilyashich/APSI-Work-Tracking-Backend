package com.apsiworktracking.apsiworktracking.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_project", schema = "public")
@JsonIgnoreProperties({"user", "project"})
public class ProjectDetail {

    @EmbeddedId
    ProjectDetailKey projectDetailId;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_fk")
    User user;

    @ManyToOne
    @MapsId("projectId")
    @JoinColumn(name = "project_fk")
    Project project;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private ProjectRoleEnum role;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;
}
