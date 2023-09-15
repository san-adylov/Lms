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
@Table(name = "links")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Link {

  @Id
  @GeneratedValue(
      generator = "link_gen",
      strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(
      name = "link_gen",
      sequenceName = "link_seq",
      allocationSize = 1,
      initialValue = 4)
  private Long id;

  private String text;

  private String link;

  @ManyToOne(cascade = {
      DETACH,
      MERGE,
      REFRESH})
  private Lesson lesson;
}
