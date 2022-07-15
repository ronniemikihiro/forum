package br.com.forum.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Topic topic;

    @Builder.Default
    private LocalDateTime creationDate = LocalDateTime.now();

    @ManyToOne(cascade = CascadeType.PERSIST)
    private UserForum author;

    @Builder.Default
    private Boolean solution = false;
}
