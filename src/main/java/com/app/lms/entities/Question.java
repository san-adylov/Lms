package com.app.lms.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.app.lms.enums.QuestionType;

import java.util.List;

@Entity
@Table(name = "questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    @Id
    @GeneratedValue(generator = "question_gen",
            strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "question_gen",
            sequenceName = "question_seq",
            allocationSize = 1,
            initialValue = 4)
    private Long id;
    private String questionName;
    @Enumerated(EnumType.STRING)
    private QuestionType questionType;
    @ManyToOne(cascade = {CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH})
    private Test test;

    @OneToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.REMOVE}, mappedBy = "question")
    private List<OptionTest> optionTests;

    @OneToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.REMOVE}, mappedBy = "question")
    private List<QuestionAnswer> questionAnswers;
}
