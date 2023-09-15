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
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {

  @Id
  @GeneratedValue(
      generator = "task_gen",
      strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(
      name = "task_gen",
      sequenceName = "task_seq",
      allocationSize = 1,
      initialValue = 4)
  private Long id;

  private String taskName;

  private String fileName;

  private String text;

  private LocalDate deadline;

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
      mappedBy = "task")
  private List<TaskAnswer> taskAnswers;
}
