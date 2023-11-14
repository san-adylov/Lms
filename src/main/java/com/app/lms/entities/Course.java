package com.app.lms.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue(generator = "course_gen",
            strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "course_gen",
            sequenceName = "course_seq",
            allocationSize = 1,
            initialValue = 4)
    private Long id;
    private String courseName;
    private String image;
    private String description;
    private LocalDate date;
    private LocalDate dateOfGraduation;

    public Course(String courseName, String image, String description, LocalDate dateOfGraduation) {
        this.courseName = courseName;
        this.image = image;
        this.description = description;
        this.dateOfGraduation = dateOfGraduation;
    }

    @ManyToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH})
    private List<Instructor> instructors = new ArrayList<>();

    @OneToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.REMOVE}, mappedBy = "course")
    private List<Lesson> lessons;

    @ManyToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH})
    private Group group;

}
