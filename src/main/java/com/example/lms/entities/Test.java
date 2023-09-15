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
@Table(name = "tests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Test {

  @Id
  @GeneratedValue(generator = "test_gen",
      strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(name = "test_gen",
      sequenceName = "test_seq",
      allocationSize = 1,
      initialValue = 4)
  private Long id;

  private String name;

  private boolean isEnable;

  @ManyToOne(cascade = {
      DETACH,
      MERGE,
      REFRESH})
  private Lesson lesson;

  @OneToMany(cascade = {
      DETACH,
      MERGE,
      REFRESH,
      REMOVE},
      mappedBy = "test")
  private List<TestAnswer> testAnswers;

  @OneToMany(cascade = {
      DETACH,
      MERGE,
      REFRESH,
      REMOVE},
      mappedBy = "test")
  private List<Question> questions;
}
