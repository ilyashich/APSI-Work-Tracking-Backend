package com.apsiworktracking.apsiworktracking.model;

import com.fasterxml.jackson.annotation.*;
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
//@JsonIgnoreProperties("client")
@Table(name = "project", schema = "public")
//@JsonIgnoreProperties({"signedUsers"})
public class Project implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long projectId;
    @JsonProperty("name")
    @Column(name = "name")
    private String name;
    @JsonProperty("description")
    @Column(name = "description")
    private String description;
//    @ManyToMany
//    @JsonProperty("signedUsers")
//    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Long.class)
//    @JoinTable(
//            name = "user_project",
//            joinColumns = @JoinColumn(name = "projectId"),
//            inverseJoinColumns = @JoinColumn(name = "id"))
//    private Set<User> signedUsers;
    @OneToMany(mappedBy = "project")
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    Set<ProjectDetail> signedUsers;
    @ManyToOne
    @JoinColumn(name = "user_fk")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Long.class)
    private User client;
    @OneToMany(mappedBy = "project")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "taskId", scope = Task.class)
    private Set<Task> tasks;


}
