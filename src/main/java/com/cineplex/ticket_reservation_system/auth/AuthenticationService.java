package com.cineplex.ticket_reservation_system.auth;

import com.cineplex.ticket_reservation_system.config.JwtService;
import com.cineplex.ticket_reservation_system.entity.Roles;
import com.cineplex.ticket_reservation_system.entity.User;
import com.cineplex.ticket_reservation_system.exceptions.InternalServerException;
import com.cineplex.ticket_reservation_system.exceptions.UsernameAlreadyExistsException;
import com.cineplex.ticket_reservation_system.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        try {
            boolean usernameExists = userRepo.findUserByUsername(registerRequest.getUsername());
            if (usernameExists) {
                throw new UsernameAlreadyExistsException("Username already exists");
            }
            var user = User.builder()
                    .firstName(registerRequest.getFirstName())
                    .lastName(registerRequest.getLastName())
                    .username(registerRequest.getUsername())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .roles(Roles.USER)
                    .build();

            userRepo.save(user);
            var jwtToken = jwtService.generateToken(user);

            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .user(user)
                    .build();
        } catch (Exception e) {
            throw new InternalServerException("Error occur during register");
        }

    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        // Check if the user is disabled before attempting authentication
        var user = userRepo.findByUsername(authenticationRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!user.isEnabled()) {
            // Return a response indicating that the user is disabled
            return AuthenticationResponse.builder().build();
        }

        // Proceed with authentication if the user is enabled
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()
                    )
            );

            // Generate JWT token
            var jwtToken = jwtService.generateToken(user);
            var authenticationResponse = AuthenticationResponse.builder()
                    .token(jwtToken)
                    .user(user)
                    .build();

            // Log the response
            log.info("Authentication Response: {}", authenticationResponse);

            return authenticationResponse;
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace(); // Log or print the stack trace
            throw new RuntimeException("Authentication failed", e);
        }
    }
}
