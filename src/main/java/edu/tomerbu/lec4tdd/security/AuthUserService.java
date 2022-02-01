package edu.tomerbu.lec4tdd.security;

import edu.tomerbu.lec4tdd.user.User;
import edu.tomerbu.lec4tdd.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthUserService implements UserDetailsService {
    private final UserRepository userRepository;

    public AuthUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //given a username->return the user(including the password, so spring can validate the pass)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUserName(username);
        if (user == null){
            throw new UsernameNotFoundException("User Not Found");
        }
        return user;
    }
}
