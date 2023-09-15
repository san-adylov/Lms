package com.example.lms.entities;

import static jakarta.persistence.CascadeType.DETACH;
import static jakarta.persistence.CascadeType.MERGE;
import static jakarta.persistence.CascadeType.REFRESH;
import static jakarta.persistence.CascadeType.REMOVE;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Group {

  @Id
  @GeneratedValue(
      generator = "group_gen",
      strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(
      name = "group_gen",
      sequenceName = "group_seq",
      allocationSize = 1,
      initialValue = 4)
  private Long id;

  @Column(nullable = false, unique = true)
  private String groupName;

  private String image;

  private String description;

  private LocalDate createDate;

  private LocalDate dateOfGraduate;

  @OneToMany(cascade = {
      DETACH,
      MERGE,
      REFRESH,
      REMOVE},
      mappedBy = "group")
  private List<Student> students;

  @OneToMany(cascade = {
      DETACH,
      MERGE,
      REFRESH},
      mappedBy = "group")
  private List<Course> courses;

}
