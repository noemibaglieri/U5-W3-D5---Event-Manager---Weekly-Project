package noemibaglieri.controllers;

import noemibaglieri.entities.User;
import noemibaglieri.exceptions.ValidationException;
import noemibaglieri.payloads.LoginDTO;
import noemibaglieri.payloads.LoginRespDTO;
import noemibaglieri.payloads.NewUserDTO;
import noemibaglieri.payloads.NewUserRespDTO;
import noemibaglieri.services.AuthService;
import noemibaglieri.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UsersService usersService;

    @PostMapping("/login")
    public LoginRespDTO login(@RequestBody LoginDTO body) {
        String accessToken = authService.checkCredentialsAndGenerateToken(body);
        return new LoginRespDTO(accessToken);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public NewUserRespDTO save(@RequestBody @Validated NewUserDTO payload, BindingResult validationResult) {
        if(validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        } else {
            User newUser = this.usersService.save(payload);
            return new NewUserRespDTO(newUser.getId());
        }
    }
}