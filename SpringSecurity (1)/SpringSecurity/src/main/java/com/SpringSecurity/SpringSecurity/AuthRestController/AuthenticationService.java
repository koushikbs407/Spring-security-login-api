package com.SpringSecurity.SpringSecurity.AuthRestController;

import com.SpringSecurity.SpringSecurity.Config.JwtService;
import com.SpringSecurity.SpringSecurity.User.User;
import com.SpringSecurity.SpringSecurity.User.UserRepository;
import io.jsonwebtoken.Jwt;
import lombok.AllArgsConstructor;
import org.springframework.context.support.BeanDefinitionDsl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.BadAttributeValueExpException;
import javax.management.relation.Role;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;

    public AuthenticationResponse authenticationResponse(RegisterRequest registerRequest) {

          User user = User.builder().firstName(registerRequest.getFirstName())
                  .lastName(registerRequest.getLastName())
                  .email(registerRequest.getEmail())
                  .passw(registerRequest.getPassword()).build();

          var JwtToken = jwtService.generateToken(user);
          return  AuthenticationResponse.builder().token(JwtToken).build();



    }

    public AuthenticationResponse authenticationResponse(Authenticationrequest authenticationrequest) {

        String username = authenticationrequest.getEmail();
        User user = userRepository.findByEmail(username).orElse(null);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        if (!passwordEncoder.matches(authenticationrequest.getPassword(), user.getPassw())) {
            throw new BadCredentialsException("Wrong password");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationrequest.getEmail(),
                        authenticationrequest.getPassword()
                )
        );
//       // User user = userRepository.findByEmail(authenticationrequest.getEmail()).orElseThrow();
        var JwtToken = jwtService.generateToken(user);
        return  AuthenticationResponse.builder().token(JwtToken).build();


    }
}
