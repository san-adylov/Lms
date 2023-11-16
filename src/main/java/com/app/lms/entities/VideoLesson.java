package com.app.lms.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "video_lessons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VideoLesson {
    @Id
    @GeneratedValue(generator = "videoLesson_gen",
            strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "videoLesson_gen",
            sequenceName = "videoLesson_seq",
            allocationSize = 1,
            initialValue = 4)
    private Long id;
    private String name;
    private String description;
    private String link;

    @ManyToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH})
    private Lesson lesson;
}
