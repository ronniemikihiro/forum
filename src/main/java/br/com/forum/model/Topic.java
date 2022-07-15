package br.com.forum.model;

import br.com.forum.controller.form.TopicForm;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String message;

    private LocalDateTime creationDate = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private TopicStatus status = TopicStatus.NOT_ANSWERED;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private UserForum author;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Course course;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    public Topic(TopicForm topicForm) {
        this.title = topicForm.getTitle();
        this.message = topicForm.getMessage();
        this.course = Course.builder()
                .name(topicForm.getCourseName())
                .build();
    }
}
