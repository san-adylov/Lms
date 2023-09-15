package com.example.lms.entities;

import static jakarta.persistence.CascadeType.DETACH;
import static jakarta.persistence.CascadeType.MERGE;
import static jakarta.persistence.CascadeType.REFRESH;
import static jakarta.persistence.CascadeType.REMOVE;

import jakarta.persistence.Entity;
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
@Table(name = "test_answers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestAnswer {

  @Id
  @GeneratedValue(
      generator = "testAnswer_gen",
      strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(
      name = "testAnswer_gen",
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
      DETACH,
      MERGE,
      REFRESH})
  private Test test;

  @ManyToOne(cascade = {
      DETACH,
      MERGE,
      REFRESH})
  private Student student;

  @OneToMany(cascade = {
      DETACH,
      MERGE,
      REFRESH,
      REMOVE},
      mappedBy = "testAnswer")
  private List<QuestionAnswer> questionAnswers;
}
