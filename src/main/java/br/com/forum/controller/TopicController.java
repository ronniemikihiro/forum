package br.com.forum.controller;

import br.com.forum.controller.dto.DetailTopicDTO;
import br.com.forum.controller.dto.TopicDTO;
import br.com.forum.controller.form.TopicForm;
import br.com.forum.controller.form.UpdateTopicForm;
import br.com.forum.model.Topic;
import br.com.forum.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Controller
@RequestMapping("/topics")
public class TopicController {

    @Autowired
    private TopicRepository topicRepository;

    @GetMapping
    @Cacheable(value = "pageTopics")
    public ResponseEntity<Page<TopicDTO>> listAll(@RequestParam(required = false) String courseName,
                                                  @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        final Page<Topic> pageTopic = StringUtils.hasLength(courseName) ?
                topicRepository.findByCourseName(courseName, pageable) :
                topicRepository.findAll(pageable);

        return ResponseEntity.ok(pageTopic.map(TopicDTO::new));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetailTopicDTO> findById(@PathVariable Long id) {
        return topicRepository.findById(id)
                .map(topic -> ResponseEntity.ok(new DetailTopicDTO(topic)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Transactional
    @CacheEvict(value = "pageTopics", allEntries = true)
    public ResponseEntity<TopicDTO> insert(@RequestBody @Valid TopicForm topicForm) {
        final Topic topic = topicRepository.save(new Topic(topicForm));
        return ResponseEntity.status(HttpStatus.CREATED).body(new TopicDTO(topic));
    }

    @PutMapping("/{id}")
    @Transactional
    @CacheEvict(value = "pageTopics", allEntries = true)
    public ResponseEntity<TopicDTO> update(@PathVariable Long id, @RequestBody @Valid UpdateTopicForm updateTopicForm) {
        return topicRepository.findById(id)
                .map(topic -> {
                    topic.setTitle(updateTopicForm.getTitle());
                    topic.setMessage(updateTopicForm.getMessage());
                    return ResponseEntity.ok(new TopicDTO(topic));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Transactional
    @CacheEvict(value = "pageTopics", allEntries = true)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return topicRepository.findById(id)
                .map(topic -> {
                    topicRepository.deleteById(id);
                    return ResponseEntity.ok().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
