package com.example.demo.controller;

import com.example.demo.entity.AppUser;
import com.example.demo.payload.LoginDto;
import com.example.demo.payload.RUserDto;
import com.example.demo.payload.TokenDto;
import com.example.demo.payload.UserDto;
import com.example.demo.repository.AppUserRepository;
import com.example.demo.service.AppUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class AppUserController {
    private AppUserService appUserService;
private AppUserRepository appUserRepository;
    public AppUserController(AppUserService appUserService, AppUserRepository appUserRepository) {
        this.appUserService = appUserService;
        this.appUserRepository = appUserRepository;
    }
@PostMapping("/signup")
    public ResponseEntity<?>createRegistration(@Valid @RequestBody UserDto userDto, BindingResult result){
        if (result.hasErrors()){
            return new ResponseEntity<>(result.getFieldError().getDefaultMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    Optional<AppUser> username = appUserRepository.findByUsername(userDto.getUsername());
        if (username.isPresent()){
            return new ResponseEntity<>("Username already exist",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    Optional<AppUser> email = appUserRepository.findByUsername(userDto.getEmail());
    if (email.isPresent()){
        return new ResponseEntity<>("email already exist",HttpStatus.INTERNAL_SERVER_ERROR);
    }
    userDto.setRole("ROLE_USER");
    RUserDto rUserDto = appUserService.signUP(userDto);
    return new ResponseEntity<>(rUserDto,HttpStatus.CREATED);
}

    @PostMapping("/signup-property-owner")
    public ResponseEntity<?>createPropertyOwnerUser(@Valid @RequestBody UserDto userDto, BindingResult result){
        if (result.hasErrors()){
            return new ResponseEntity<>(result.getFieldError().getDefaultMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
        Optional<AppUser> username = appUserRepository.findByUsername(userDto.getUsername());
        if (username.isPresent()){
            return new ResponseEntity<>("Username already exist",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Optional<AppUser> email = appUserRepository.findByUsername(userDto.getEmail());
        if (email.isPresent()){
            return new ResponseEntity<>("email already exist",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        userDto.setRole("ROLE_OWNER");
        RUserDto rUserDto = appUserService.signUP(userDto);
        return new ResponseEntity<>(rUserDto,HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?>login(@RequestBody LoginDto loginDto){
        String login = appUserService.login(loginDto);
        if (login!=null){
            TokenDto tokenDto=new TokenDto();
            tokenDto.setToken(login);
            tokenDto.setType("JWT");
            return new ResponseEntity<>(tokenDto,HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Username / password not mathched",HttpStatus.FORBIDDEN);
        }
    }
}
