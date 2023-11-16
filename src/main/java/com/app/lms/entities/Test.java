package com.app.lms.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH})
    private Lesson lesson;

    @OneToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.REMOVE}, mappedBy = "test")
    private List<TestAnswer> testAnswers;

    @OneToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.REMOVE}, mappedBy = "test")
    private List<Question> questions;
}
