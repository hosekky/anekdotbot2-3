package ru.poldjoke.demo.jokebot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.poldjoke.demo.jokebot.exceptions.UsernameAlreadyExistsException;
import ru.poldjoke.demo.jokebot.model.User;
import ru.poldjoke.demo.jokebot.model.UserAuthority;
import ru.poldjoke.demo.jokebot.model.UserRole;
import ru.poldjoke.demo.jokebot.repository.UserRepository;
import ru.poldjoke.demo.jokebot.repository.UserRolesRepository;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRolesRepository userRolesRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void registration(String username, String password) {
        if (userRepository.findByUsername(username).isEmpty()) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setLocked(false);
            user.setExpired(false);
            user.setEnabled(true);
            user = userRepository.save(user);
            userRolesRepository.save(new UserRole(null, user, UserAuthority.PLACE_ORDERS));
        }
        else {
            throw new UsernameAlreadyExistsException();
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
