package com.app.lms.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "option_tests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OptionTest {
    @Id
    @GeneratedValue(generator = "optionTest_gen",
            strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "optionTest_gen",
            sequenceName = "optionTest_seq",
            allocationSize = 1,
            initialValue = 4)
    private Long id;
    private String option;
    private boolean isTrue;

    @ManyToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH})
    private Question question;
}
