package br.com.forum.repository;

import br.com.forum.model.UserForum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserForumRepository extends JpaRepository<UserForum, Long> {

    Optional<UserForum> findByEmail(String email);

}
