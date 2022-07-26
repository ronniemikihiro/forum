package br.com.forum.config.security;

import br.com.forum.model.UserForum;
import br.com.forum.repository.UserForumRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class AuthenticationTokenFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserForumRepository userForumRepository;

    public AuthenticationTokenFilter(TokenService tokenService, UserForumRepository userForumRepository) {
        this.tokenService = tokenService;
        this.userForumRepository = userForumRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getToken(request);
        
        if (tokenService.isValidToken(token)) {
            authenticateClient(token);
        }

        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return StringUtils.hasLength(token) && token.startsWith("Bearer") ?
            token.substring(7).trim() : null;
    }

    private void authenticateClient(String token) {
        Long idUserForum = tokenService.getIdUserForum(token);
        Optional<UserForum> opUserForum = userForumRepository.findById(idUserForum);
        UsernamePasswordAuthenticationToken authenticationToken = opUserForum
                .map(userForum -> new UsernamePasswordAuthenticationToken(userForum, null, userForum.getAuthorities()))
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password!"));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
