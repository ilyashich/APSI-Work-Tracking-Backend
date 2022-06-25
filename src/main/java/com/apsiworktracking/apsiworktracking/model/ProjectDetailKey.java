package com.apsiworktracking.apsiworktracking.model;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ProjectDetailKey implements Serializable {

    @Column(name = "user_id")
    private Long userId;
    @Column(name = "project_id")
    private Long projectId;
}
