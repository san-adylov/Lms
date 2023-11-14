package com.app.lms.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "questionAnswers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAnswer {
    @Id
    @GeneratedValue(generator = "question_gen",
            strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "question_gen",
            sequenceName = "question_seq",
            allocationSize = 1,
            initialValue = 4)
    private Long id;
    private double point;

    @ManyToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH})
    private List<OptionTest> option;

    @ManyToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,})
    private Question question;

    @ManyToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,})
    private TestAnswer testAnswer;
}
