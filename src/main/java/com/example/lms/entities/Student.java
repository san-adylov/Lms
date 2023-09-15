package com.example.lms.entities;

import static jakarta.persistence.CascadeType.DETACH;
import static jakarta.persistence.CascadeType.MERGE;
import static jakarta.persistence.CascadeType.REFRESH;
import static jakarta.persistence.CascadeType.REMOVE;

import com.example.lms.enums.StudyFormat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

  @Id
  @GeneratedValue(
      generator = "student_gen",
      strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(
      name = "student_gen",
      sequenceName = "student_seq",
      allocationSize = 1,
      initialValue = 4)
  private Long id;

  @Enumerated(value = EnumType.STRING)
  private StudyFormat studyFormat;

  private boolean isBlocked;

  @ManyToOne(cascade = {
      MERGE,
      CascadeType.DETACH,
      CascadeType.REFRESH})
  private Group group;

  @OneToOne(cascade = {
      DETACH,
      MERGE,
      REFRESH,
      REMOVE})
  private User user;

  @OneToMany(cascade = {
      DETACH,
      MERGE,
      REFRESH,
      REMOVE},
      mappedBy = "student")
  private List<TaskAnswer> taskAnswers;

  @OneToMany(cascade = {
      DETACH,
      MERGE,
      REFRESH,
      REMOVE},
      mappedBy = "student")
  private List<TestAnswer> testAnswers;


  public Student(long id, String s) {

  }
}
