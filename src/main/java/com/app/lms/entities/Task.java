package com.app.lms.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(generator = "task_gen",
            strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "task_gen",
            sequenceName = "task_seq",
            allocationSize = 1,
            initialValue = 4)
    private Long id;
    private String taskName;
    @Column(length = 1000)
    private String text;
    private String fileName;
    @Column(length = 500)
    private String fileLink;
    private String linkName;
    @Column(length = 500)
    private String link;
    @Column(length = 1000)
    private String image;
    @Column(length = 2000)
    private String code;
    private LocalDate deadline;

    @ManyToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH})
    private Lesson lesson;

    @OneToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.REMOVE}, mappedBy = "task")
    private List<TaskAnswer> taskAnswers;
}
