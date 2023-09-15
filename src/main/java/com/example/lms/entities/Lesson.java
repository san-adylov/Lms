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
@Table(name = "lessons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {

  @Id
  @GeneratedValue(
      generator = "lesson_gen",
      strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(
      name = "lesson_gen",
      sequenceName = "lesson_seq",
      allocationSize = 1,
      initialValue = 4)
  private Long id;

  private String lessonName;

  @ManyToOne(cascade = {
      DETACH,
      MERGE,
      REFRESH})
  private Course course;

  @OneToMany(cascade = {
      DETACH,
      MERGE,
      REFRESH,
      REMOVE},
      mappedBy = "lesson")
  private List<Task> tasks;

  @OneToMany(cascade = {
      DETACH,
      MERGE,
      REFRESH,
      REMOVE},
      mappedBy = "lesson")
  private List<Test> tests;

  @OneToMany(cascade = {
      DETACH,
      MERGE,
      REFRESH,
      REMOVE},
      mappedBy = "lesson")
  private List<VideoLesson> videoLessons;

  @OneToMany(cascade = {
      DETACH,
      MERGE,
      REFRESH,
      REMOVE},
      mappedBy = "lesson")
  private List<Link> links;

  @OneToMany(cascade = {
      DETACH,
      MERGE,
      REFRESH,
      REMOVE},
      mappedBy = "lesson")
  private List<Presentation> presentations;

}
