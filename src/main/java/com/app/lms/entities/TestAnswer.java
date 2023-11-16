package com.app.lms.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "test_answers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestAnswer {
    @Id
    @GeneratedValue(generator = "testAnswer_gen",
            strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "testAnswer_gen",
            sequenceName = "testAnswer_seq",
            allocationSize = 1,
            initialValue = 4)
    private Long id;
    private String testName;
    private int correct;
    private int inCorrect;
    private double point;
    private boolean isAccepted;

    @ManyToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH})
    private Test test;

    @ManyToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH})
    private Student student;

    @OneToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.REMOVE}, mappedBy = "testAnswer")
    private List<QuestionAnswer> questionAnswers;
}
