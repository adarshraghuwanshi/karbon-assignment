package com.example.blog.controller;

import com.example.blog.dto.SignInDTO;
import com.example.blog.dto.SignUpDTO;
import com.example.blog.dto.UpdateUserDTO;
import com.example.blog.entity.User;
import com.example.blog.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private AuthService authService;

  @PostMapping("/sign-up")
  public ResponseEntity<User> signUp(@RequestBody SignUpDTO signUpDTO) {
    User user = authService.signUp(signUpDTO);
    return new ResponseEntity<>(user, HttpStatus.CREATED);
  }

  @PostMapping("/sign-in")
  public ResponseEntity<?> signIn(@RequestBody SignInDTO signInDTO, HttpServletResponse response) {
    User user = authService.signIn(signInDTO, response);
    if (user == null) {
      return new ResponseEntity<>("Invalid email or password", HttpStatus.UNAUTHORIZED);
    }
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @PostMapping("/sign-out")
  public ResponseEntity<String> signOut(HttpServletRequest request, HttpServletResponse response) {
    authService.signOut(request, response);
    return new ResponseEntity<>("Successfully signed out", HttpStatus.OK);
  }

  @PutMapping("/make-user-admin")
  public ResponseEntity<Void> makeUserAdmin(HttpServletRequest request) {
    authService.makeUserAdmin(request);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PutMapping("/{id}")
   public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UpdateUserDTO updateUserDTO) {
    User updated = authService.updateUser(id, updateUserDTO);
    return new ResponseEntity<>(updated, HttpStatus.OK);
}

@DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            String message = authService.deleteUser(id);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/generate-otp")
public ResponseEntity<String> generateOtp(@RequestParam String email) {
    try {
        String message = authService.generateAndSendOtp(email);
        return new ResponseEntity<>(message, HttpStatus.OK);
    } catch (RuntimeException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
@PostMapping("/verify-otp")
public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String otp) {
    try {
        User user = authService.verifyOtp(email, otp);
        return new ResponseEntity<>(user, HttpStatus.OK); 
    } catch (RuntimeException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}

}

