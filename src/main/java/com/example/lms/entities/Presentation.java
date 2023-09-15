package com.example.lms.entities;

import static jakarta.persistence.CascadeType.DETACH;
import static jakarta.persistence.CascadeType.MERGE;
import static jakarta.persistence.CascadeType.REFRESH;

import jakarta.persistence.Entity;
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
@Table(name = "presentations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Presentation {

  @Id
  @GeneratedValue(
      generator = "presentation_gen",
      strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(
      name = "presentation_gen",
      sequenceName = "presentation_seq",
      allocationSize = 1,
      initialValue = 4)
  private Long id;

  private String name;

  private String description;

  private String linkFilePpt;

  @ManyToOne(cascade = {
      DETACH,
      MERGE,
      REFRESH})
  private Lesson lesson;
}
