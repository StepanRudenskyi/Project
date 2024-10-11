package org.example.petstore.service;

import org.example.petstore.model.User;
import org.example.petstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Custom implementation of the {@link UserDetailsService} interface to handle
 * user authentication by loading user details from the database.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads user details by the given username. This method is used during authentication
     * to validate user credentials and fetch their roles.
     *
     * @param username the username of the user trying to log in
     * @return {@link UserDetails} containing user information and roles
     * @throws UsernameNotFoundException if the user with the given username is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            var user = userOptional.get();

            // Extract user roles and convert them into a String array
            String[] roles = user.getRoles().stream()
                    .map(Enum::name)
                    .toArray(String[]::new);

            // Build and return UserDetails with username, password, and roles
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(roles)
                    .build();
        } else {
            throw new UsernameNotFoundException("User with username: " + username  + " not found");
        }
    }
}
