package com.corporation.apiclient.services;

import com.corporation.apiclient.dto.security.AccountCredentialsDTO;
import com.corporation.apiclient.dto.security.TokenDTO;
import com.corporation.apiclient.entities.User;
import com.corporation.apiclient.repositories.ClientRepository;
import com.corporation.apiclient.repositories.UserRepository;
import com.corporation.apiclient.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthServices {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity signin(AccountCredentialsDTO data){
        try{
            String username = data.getUsername();
            String password = data.getPassword();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            User user = userRepository.findByUsername(username);
            var tokenResponse = new TokenDTO();
            if(user != null){
                tokenResponse = jwtTokenProvider.createAcessToken(username,user.getRoles());
            }else{
               throw new UsernameNotFoundException("Username " + username + "Not Found");
            }
            return ResponseEntity.ok(tokenResponse);
        }catch (Exception e){
            throw new BadCredentialsException("Invalid username or Password");
        }
    }

}
