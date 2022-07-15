package br.com.forum.controller.dto;

import br.com.forum.model.Answer;
import br.com.forum.model.Topic;
import br.com.forum.model.TopicStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class DetailTopicDTO {

    private Long id;
    private String title;
    private String message;
    private LocalDateTime creationDate;
    private String author;
    private TopicStatus status;
    private List<AnswerDTO> answers;

    public DetailTopicDTO(Topic topic) {
        this.id = topic.getId();
        this.title = topic.getTitle();
        this.message = topic.getMessage();
        this.creationDate = topic.getCreationDate();
        this.author = topic.getAuthor().getName();
        this.status = topic.getStatus();
        this.answers = topic.getAnswers().stream().map(AnswerDTO::new).toList();
    }

}
