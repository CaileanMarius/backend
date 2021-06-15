package cmir2469.backend.springSecurity;

import cmir2469.backend.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository = new UserRepository();

    public MyUserDetailsService() {
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        cmir2469.backend.domain.User user = userRepository.findOneByUsername(username);
        return new User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }
}
