package br.com.forum.config.security;

import br.com.forum.repository.UserForumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private UserForumRepository userForumRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userForumRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Invalid username or password!"));
    }
}
