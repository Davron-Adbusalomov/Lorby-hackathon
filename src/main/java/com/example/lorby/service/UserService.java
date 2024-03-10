package com.example.lorby.service;

import com.example.lorby.configs.JwtService;
import com.example.lorby.dto.Token;
import com.example.lorby.dto.UserLoginDTO;
import com.example.lorby.dto.UserRegisterDTO;
import com.example.lorby.dto.VerificationDTO;
import com.example.lorby.model.TemporaryUser;
import com.example.lorby.model.Users;
import com.example.lorby.repository.TemporaryDBRepository;
import com.example.lorby.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TemporaryDBRepository temporaryDBRepository;

    public String registerUser(UserRegisterDTO userRegisterDTO) throws Exception {
        if (userRepository.findByUsername(userRegisterDTO.getUsername()).isPresent()){
            throw new Exception("Username is already taken!");
        }

        if (!Objects.equals(userRegisterDTO.getPassword(), userRegisterDTO.getPassword2())){
            throw new Exception("Passwords don't match!");
        }

        TemporaryUser users = new TemporaryUser();
        users.setEmail(userRegisterDTO.getEmail());
        users.setUsername(userRegisterDTO.getUsername());
        users.setPassword(userRegisterDTO.getPassword());

        int minRoomNumber = 1000;
        int maxRoomNumber = 9999;
        int code = (int) (Math.random() * (maxRoomNumber - minRoomNumber + 1)) + minRoomNumber;

        users.setCode(code);

        temporaryDBRepository.save(users);

        emailService.sendEmail(users.getEmail(), "Lorby Verification", String.valueOf(code));

        return "Successfully saved!";
    }

    public String verifyUser(VerificationDTO verificationDTO) throws Exception {
        Optional<TemporaryUser> temporaryUser = temporaryDBRepository.findByUsername(verificationDTO.getUsername());
        if (temporaryUser.isEmpty()){
            throw new EntityNotFoundException("No user found");
        }

        if (temporaryUser.get().getCode()==verificationDTO.getCode()){
            Users users = new Users();
            users.setEmail(temporaryUser.get().getEmail());
            users.setUsername(temporaryUser.get().getUsername());
            users.setPassword(temporaryUser.get().getPassword());

            userRepository.save(users);
            temporaryDBRepository.delete(temporaryUser.get());
            return "Successfully saved!";
        }
        else {
            throw new Exception("Password did not match!");
        }

    }

    public Token loginUser(UserLoginDTO userLoginDTO) {
        Optional<Users> user = userRepository.findByUsername(userLoginDTO.getUsername());
        if (user.isPresent() && user.get().getPassword().equals(userLoginDTO.getPassword())){
            String token = jwtService.generateToken(user.get());
            return new Token(token);
        }

        throw new EntityNotFoundException("Login failed!");
    }
}
