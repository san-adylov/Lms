package com.app.lms.entities;

import jakarta.persistence.*;
import lombok.*;
import com.app.lms.enums.StudyFormat;
import java.util.List;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {
    @Id
    @GeneratedValue(generator = "student_gen",
            strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "student_gen",
            sequenceName = "student_seq",
            allocationSize = 1,
            initialValue = 4)
    private Long id;
    @Enumerated(value = EnumType.STRING)
    private StudyFormat studyFormat;
    private boolean isBlocked;

    @ManyToOne(cascade = {
            CascadeType.MERGE,
            CascadeType.DETACH,
            CascadeType.REFRESH})
    private Group group;

    @OneToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.REMOVE})
    private User user;

    @OneToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.REMOVE}, mappedBy = "student")
    private List<TaskAnswer> taskAnswers;

    @OneToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.REMOVE}, mappedBy = "student")
    private List<TestAnswer> testAnswers;


    public Student(long id, String s) {

    }
}
