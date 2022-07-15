package br.com.forum.controller.form;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginForm {

    private String email;
    private String password;

}
