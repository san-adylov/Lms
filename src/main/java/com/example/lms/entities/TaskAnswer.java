package com.example.lms.entities;

import static jakarta.persistence.CascadeType.DETACH;
import static jakarta.persistence.CascadeType.MERGE;
import static jakarta.persistence.CascadeType.REFRESH;

import com.example.lms.enums.TaskAnswerStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "task_answers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskAnswer {

  @Id
  @GeneratedValue(
      generator = "taskAnswer_gen",
      strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(
      name = "taskAnswer_gen",
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
      DETACH,
      MERGE,
      REFRESH})
  private Student student;

  @ManyToOne(cascade = {
      DETACH,
      MERGE,
      REFRESH})
  private Task task;
}