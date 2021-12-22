package ru.alex.mini_bank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.alex.mini_bank.entity.User;
import ru.alex.mini_bank.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Optional<User> optionalUser = userRepository.findByUniqueName(username);
        if (!optionalUser.isPresent()) {
            throw new UsernameNotFoundException("User does not exist!");
        }
        return optionalUser.get();
    }

    public Optional<User> signIn(@NonNull String username, @NonNull String password) {
        Optional<User> optionalUser = userRepository.findByUniqueName(username);
        if(!optionalUser.isPresent()){
            return Optional.empty();
        }

        User user = optionalUser.get();

        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            return Optional.empty();
        }

        auth(user);
        return Optional.of(userRepository.save(user));
    }

    public Optional<User> signUp(@NonNull String uniqueName, @NonNull String password) {
        String cryptPassword = bCryptPasswordEncoder.encode(password);

        if (userRepository.findByUniqueName(uniqueName).isPresent()) {
            return Optional.empty();
        }
        final User user = new User(uniqueName, cryptPassword);
        auth(user);
        return Optional.of(userRepository.save(user));
    }

    public boolean signOut(@NonNull String uniqueName) {
        Optional<User> optionalUser = userRepository.findByUniqueName(uniqueName);
        if (!optionalUser.isPresent()) {
            return true;
        }

        User user = optionalUser.get();
        user.setSignedOut(false);
        userRepository.save(user);
        return true;
    }

    private void auth(User user) {
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        user.setSignedOut(true);
    }

    public Optional<User> findByToken(@NonNull String token) {
        return userRepository.findByToken(token);
    }

    public User updateUser(@NonNull User user) {
        return userRepository.save(user);
    }
}
