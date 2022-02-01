package edu.tomerbu.lec4tdd.user;

import edu.tomerbu.lec4tdd.errors.DuplicateUserNameException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User saveUser(User user){
        //org.springframework.security.core.userdetails.User
        User inDb = userRepository.findByUserName(user.getUserName());
        if (inDb!= null){
            throw new DuplicateUserNameException();
        }

        //first encode the password:
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        return userRepository.save(user);
    }
}
