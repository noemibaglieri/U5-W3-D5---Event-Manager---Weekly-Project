package noemibaglieri.services;

import noemibaglieri.entities.User;
import noemibaglieri.exceptions.UnauthorisedException;
import noemibaglieri.payloads.LoginDTO;
import noemibaglieri.tools.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UsersService usersService;

    @Autowired
    private JWTTools jwtTools;

    @Autowired
    private PasswordEncoder bCrypt;

    public String checkCredentialsAndGenerateToken(LoginDTO body) {
        User found = this.usersService.findByEmail(body.email());

        if(bCrypt.matches(body.password(), found.getPassword())) {
            return jwtTools.createToken(found);
        } else {
            throw new UnauthorisedException("Wrong credentials!");
        }
    }

}