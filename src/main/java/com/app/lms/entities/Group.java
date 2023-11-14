package com.app.lms.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Group {
    @Id
    @GeneratedValue(generator = "group_gen",
            strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "group_gen",
            sequenceName = "group_seq",
            allocationSize = 1,
            initialValue = 4)
    private Long id;
    @Column(nullable = false, unique = true)
    private String groupName;
    private String image;
    private String description;
    private LocalDate createDate;
    private LocalDate dateOfGraduate;

    @OneToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.REMOVE}, mappedBy = "group")
    private List<Student> students;

    @OneToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH}, mappedBy = "group")
    private List<Course> courses;

}
