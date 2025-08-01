package noemibaglieri.services;

import noemibaglieri.entities.User;
import noemibaglieri.enums.Role;
import noemibaglieri.exceptions.BadRequestException;
import noemibaglieri.exceptions.EmailAlreadyExistsException;
import noemibaglieri.exceptions.NotFoundException;
import noemibaglieri.payloads.NewUserDTO;
import noemibaglieri.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersService {

    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private PasswordEncoder bCrypt;

    public User save(NewUserDTO payload) {

        this.userRepository.findByUsername(payload.username()).ifPresent(user -> {
            throw new BadRequestException("This username * " + payload.username() + " * is already in use!");
        });

        if(userRepository.existsByEmail(payload.email())) {
            throw new EmailAlreadyExistsException("This email * " + payload.email() + " * is already in use!");
        }

        User newUser = new User(payload.username(), payload.email(), bCrypt.encode(payload.password()));
        this.userRepository.save(newUser);
        return newUser;
    }

    public List<User> findAll() { return this.userRepository.findAll(); }

    public User findById(long employeeId) {
        return this.userRepository.findById(employeeId).orElseThrow(() -> new NotFoundException(employeeId));
    }

    public User findByEmail(String email){
        return this.userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(email));
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User updateUserRole(Long userId, Role newRole) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));

        user.setRole(newRole);
        return userRepository.save(user);
    }

}
