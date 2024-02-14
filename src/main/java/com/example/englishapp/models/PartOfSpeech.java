package com.example.englishapp.models;

import jakarta.persistence.*;
import lombok.*;


@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class PartOfSpeech {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "part_of_speech_id")
    private Integer id;

    private String name;

}
