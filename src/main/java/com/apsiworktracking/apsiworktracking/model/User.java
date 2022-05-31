package com.apsiworktracking.apsiworktracking.model;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

import static javax.persistence.AccessType.PROPERTY;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users", schema = "public")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "projectId", scope = Long.class)
    @ManyToMany(mappedBy = "signedUsers")
    private Set<Project> projects;
    @OneToMany(mappedBy = "client")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "projectId", scope = Long.class)
    private Set<Project> clientProject;
    @OneToMany(mappedBy = "employee")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "jobId", scope = Long.class)
    private Set<Job> jobs;

}
