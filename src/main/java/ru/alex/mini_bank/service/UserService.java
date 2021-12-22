package ru.alex.mini_bank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.alex.mini_bank.entity.User;
import ru.alex.mini_bank.repository.UserRepository;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Optional<User> optionalUser = userRepository.findByUniqueName(username);
        if (!optionalUser.isPresent()) {
            throw new UsernameNotFoundException("User does not exist!");
        }
        return optionalUser.get();
    }

    public Optional<User> saveOrUpdateUser(@NonNull String uniqueName, @NonNull String password) {
        if (userRepository.findByUniqueName(uniqueName).isPresent()) {
            return Optional.empty();
        }
        final User user = new User(uniqueName, password);
        return Optional.of(userRepository.save(user));
    }

    public User updateUser(@NonNull User user) {
        return userRepository.save(user);
    }
}
