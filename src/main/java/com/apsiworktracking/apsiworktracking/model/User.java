package com.apsiworktracking.apsiworktracking.model;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user", schema = "public")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
    @Getter
    @Setter
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    @Getter (AccessLevel.PUBLIC)private String password;
}
