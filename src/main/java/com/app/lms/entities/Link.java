package com.app.lms.entities;

import jakarta.persistence.*;
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
    @GeneratedValue(generator = "link_gen",
            strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "link_gen",
            sequenceName = "link_seq",
            allocationSize = 1,
            initialValue = 4)
    private Long id;
    private String text;
    private String link;

    @ManyToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH})
    private Lesson lesson;
}
