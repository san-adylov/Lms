package com.app.lms.entities;

import jakarta.persistence.*;
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
    @GeneratedValue(generator = "presentation_gen",
            strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "presentation_gen",
            sequenceName = "presentation_seq",
            allocationSize = 1,
            initialValue = 4)
    private Long id;
    private String name;
    private String description;
    private String linkFilePpt;

    @ManyToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH})
    private Lesson lesson;
}
