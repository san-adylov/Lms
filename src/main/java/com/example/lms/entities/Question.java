package com.example.lms.entities;

import static jakarta.persistence.CascadeType.DETACH;
import static jakarta.persistence.CascadeType.MERGE;
import static jakarta.persistence.CascadeType.REFRESH;
import static jakarta.persistence.CascadeType.REMOVE;

import com.example.lms.enums.QuestionType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Question {

  @Id
  @GeneratedValue(
      generator = "question_gen",
      strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(
      name = "question_gen",
      sequenceName = "question_seq",
      allocationSize = 1,
      initialValue = 4)
  private Long id;

  private String questionName;

  @Enumerated(EnumType.STRING)
  private QuestionType questionType;

  @ManyToOne(cascade = {
      DETACH,
      MERGE,
      REFRESH})
  private Test test;

  @OneToMany(cascade = {
      DETACH,
      MERGE,
      REFRESH,
      REMOVE},
      mappedBy = "question")
  private List<OptionTest> optionTests;

  @OneToMany(cascade = {
      DETACH,
      MERGE,
      REFRESH,
      REMOVE},
      mappedBy = "question")
  private List<QuestionAnswer> questionAnswers;
}
