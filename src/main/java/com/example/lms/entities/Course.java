package com.example.lms.entities;

import static jakarta.persistence.CascadeType.DETACH;
import static jakarta.persistence.CascadeType.MERGE;
import static jakarta.persistence.CascadeType.REFRESH;
import static jakarta.persistence.CascadeType.REMOVE;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
public class Course {

  @Id
  @GeneratedValue(
      generator = "course_gen",
      strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(
      name = "course_gen",
      sequenceName = "course_seq",
      allocationSize = 1,
      initialValue = 4)
  private Long id;

  private String courseName;

  private String image;

  private String description;

  private LocalDate date;

  private LocalDate dateOfGraduation;

  public Course(String courseName, String image, String description, LocalDate dateOfGraduation) {
    this.courseName = courseName;
    this.image = image;
    this.description = description;
    this.dateOfGraduation = dateOfGraduation;
  }

  @ManyToMany(cascade = {
      DETACH,
      MERGE,
      REFRESH})
  private List<Instructor> instructors;

  @OneToMany(cascade = {
      DETACH,
      MERGE,
      REFRESH,
      REMOVE},
      mappedBy = "course")
  private List<Lesson> lessons;

  @ManyToOne(cascade = {
      DETACH,
      MERGE,
      REFRESH})
  private Group group;

}
