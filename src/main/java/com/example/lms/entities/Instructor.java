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
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "instructors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Instructor {

    @Id
    @GeneratedValue(
            generator = "instructor_gen",
            strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "instructor_gen",
            sequenceName = "instructor_seq",
            allocationSize = 1,
            initialValue = 3)
    private Long id;

    private String specialization;

    @OneToOne(cascade = {
            DETACH,
            MERGE,
            REFRESH,
            REMOVE})
    private User user;

    @ManyToMany(cascade = {
            DETACH,
            MERGE,
            REFRESH},
            mappedBy = "instructors")
    private List<Course> courses;


}
