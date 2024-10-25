package com.example.demo.service;

import com.example.demo.entity.AppUser;
import com.example.demo.payload.LoginDto;
import com.example.demo.payload.RUserDto;
import com.example.demo.payload.UserDto;
import com.example.demo.repository.AppUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserService {
    private AppUserRepository appUserRepository;
 private JWTService jwtService;
 private ModelMapper modelMapper;
    public AppUserService(AppUserRepository appUserRepository, JWTService jwtService, ModelMapper modelMapper) {
        this.appUserRepository = appUserRepository;
        this.jwtService = jwtService;
        this.modelMapper = modelMapper;
    }
    public RUserDto signUP(UserDto userDto) {
        AppUser user = modelMapper.map(userDto, AppUser.class);
        String hashpw = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(5));
        user.setPassword(hashpw);
        AppUser save = appUserRepository.save(user);
       return modelMapper.map(save,RUserDto.class);
    }

    public String login(LoginDto loginDto) {
        Optional<AppUser> username =
                appUserRepository.findByUsername(loginDto.getUsername());
        if (username.isPresent()){
            boolean checkpw = BCrypt.checkpw(loginDto.getPassword(), username.get().getPassword());
            if (checkpw){
                String token = jwtService.generateToken(loginDto.getUsername());
                return token;
            }
        }
        return null;
    }
}
