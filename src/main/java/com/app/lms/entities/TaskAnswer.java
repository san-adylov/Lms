package com.app.lms.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.app.lms.enums.TaskAnswerStatus;

@Entity
@Table(name = "task_answers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskAnswer {
    @Id
    @GeneratedValue(generator = "taskAnswer_gen",
            strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "taskAnswer_gen",
            sequenceName = "taskAnswer_seq",
            allocationSize = 1,
            initialValue = 4)
    private Long id;
    private String file;
    private String text;
    private String comment;
    @Enumerated(EnumType.STRING)
    private TaskAnswerStatus taskAnswerStatus;

    @ManyToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH})
    private Student student;

    @ManyToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH})
    private Task task;
}