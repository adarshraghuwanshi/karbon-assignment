package com.example.blog.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;

import com.example.blog.dto.SignInDTO;
import com.example.blog.dto.SignUpDTO;
import com.example.blog.dto.UpdateUserDTO;
import com.example.blog.security.RoleEnum;
import com.example.blog.entity.User;
import com.example.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;


import java.time.LocalDateTime;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.apache.commons.lang3.RandomStringUtils;


@Service
public class AuthService {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private JavaMailSender mailSender;

  private final String COOKIE_NAME = "SESSIONID";

  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  public User signUp(SignUpDTO request) {
    User user = new User();
    user.setEmail(request.getEmail());
    user.setEncodedPassword(passwordEncoder.encode(request.getPassword()));
    user.setFirstName(request.getFirstName());
    user.setLastName(request.getLastName());
    user.setRoleId(RoleEnum.SIGNED_OUT.getRoleId());
    user.setRoleName(RoleEnum.SIGNED_OUT.getRoleName());
    return userRepository.save(user);
  }

  public User signIn(SignInDTO request, HttpServletResponse response) {
    Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      if (passwordEncoder.matches(request.getPassword(), user.getEncodedPassword())) {
        String sessionId = UUID.randomUUID().toString();
        Cookie cookie = new Cookie(COOKIE_NAME, sessionId);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        user.setSessionId(sessionId);
        user.setRoleId(RoleEnum.USER.getRoleId());
        user.setRoleName(RoleEnum.USER.getRoleName());
        return userRepository.save(user);
      }
    }
    return null;
  }

  public void signOut(HttpServletRequest request, HttpServletResponse response) {
    Cookie cookie = new Cookie(COOKIE_NAME, null);
    cookie.setMaxAge(0);
    cookie.setPath("/");
    response.addCookie(cookie);

    Optional<User> userOptional = getUserFromSession(request);
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      user.setSessionId(null);
      user.setRoleId(RoleEnum.SIGNED_OUT.getRoleId());
      user.setRoleName(RoleEnum.SIGNED_OUT.getRoleName());
      userRepository.save(user);
    }
  }

  public Optional<User> getUserFromSession(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (COOKIE_NAME.equals(cookie.getName())) {
          String sessionId = cookie.getValue();
          return userRepository.findBySessionId(sessionId);
        }
      }
    }
    return Optional.empty();
  }

  public void makeUserAdmin(HttpServletRequest request) {
    Optional<User> userOptional = getUserFromSession(request);

    if (!userOptional.isPresent()) {
      throw new AccessDeniedException("unauthorized");
    }

    User user = userOptional.get();
    user.setRoleId(RoleEnum.ADMIN.getRoleId());
    user.setRoleName(RoleEnum.ADMIN.getRoleName());
    userRepository.save(user);
  }
   public User getUserById(Long id) {
    return userRepository.findById(id).orElse(null);
  }

  public User updateUser(Long id, UpdateUserDTO updatedDetails) {
    User existingUser = getUserById(id);
    existingUser.setEmail(updatedDetails.getEmail());
    existingUser.setFirstName(updatedDetails.getFirstName());
    existingUser.setLastName(updatedDetails.getLastName());
    return userRepository.save(existingUser);
}

public String deleteUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        userRepository.delete(user);
        return "User with ID " + id + " has been deleted successfully";
    }

public String generateAndSendOtp(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
       

        // User user = userOptional.get();
        // // String otp = RandomString.make(6); 
        User user;

        if (userOptional.isPresent()) {
        user = userOptional.get();
         } else {
        user = new User();
        user.setEmail(email);
        user.setFirstName("User");
        user.setLastName("User");
        user.setEncodedPassword("jfgtrkeygiofklderigtdmf");
         }
        String otp = RandomStringUtils.randomNumeric(6);
        String hashedOtp = passwordEncoder.encode(otp);

        user.setOtpHash(hashedOtp);
        user.setOtpExpiryTime(LocalDateTime.now().plusMinutes(5));
        userRepository.save(user);

        sendOtpEmail(email, otp); 

        return "OTP sent successfully";
    }

    private void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP is: " + otp);
        mailSender.send(message);
    }

    public User verifyOtp(String email, String otp) {
    Optional<User> userOptional = userRepository.findByEmail(email);
    if (!userOptional.isPresent()) {
        throw new RuntimeException("User not found");
    }

    User user = userOptional.get();
    if (user.getOtpExpiryTime() == null || LocalDateTime.now().isAfter(user.getOtpExpiryTime())) {
        throw new RuntimeException("OTP has expired");
    }

    if (!passwordEncoder.matches(otp, user.getOtpHash())) {
        throw new RuntimeException("Invalid OTP");
    }

    
    user.setOtpHash(null);
    user.setOtpExpiryTime(null);
    userRepository.save(user);

    return user;
}


}
