package com.example.lms.entities;

import static jakarta.persistence.CascadeType.DETACH;
import static jakarta.persistence.CascadeType.MERGE;
import static jakarta.persistence.CascadeType.REFRESH;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
      DETACH,
      MERGE,
      REFRESH})
  private List<OptionTest> option;

  @ManyToOne(cascade = {
      DETACH,
      MERGE,
      REFRESH,})
  private Question question;

  @ManyToOne(cascade = {
      DETACH,
      MERGE,
      REFRESH,})
  private TestAnswer testAnswer;
}
