package com.cineplex.ticket_reservation_system.auth;

import com.cineplex.ticket_reservation_system.config.JwtService;
import com.cineplex.ticket_reservation_system.entity.Roles;
import com.cineplex.ticket_reservation_system.entity.User;
import com.cineplex.ticket_reservation_system.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest registerRequest) {

        var user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .roles(Roles.USER)
                .build();

        userRepo.save(user);
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        // Check if the user is disabled before attempting authentication
        var user = userRepo.findByUsername(authenticationRequest.getUsername())
                .orElseThrow(); // Assuming user is present

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

            return AuthenticationResponse.builder().token(jwtToken).build();
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace(); // Log or print the stack trace
            throw new RuntimeException("Authentication failed", e);
        }
    }


}