package br.com.forum.controller.dto;

import br.com.forum.model.Answer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AnswerDTO {

    private Long id;
    private String message;
    private LocalDateTime creationDate;
    private String author;

    public AnswerDTO(Answer answer) {
        this.id = answer.getId();
        this.message = answer.getMessage();
        this.creationDate = answer.getCreationDate();
        this.author = answer.getAuthor().getName();
    }
}
