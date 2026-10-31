package com.ecomerce.user.controller;

import com.ecomerce.user.dto.UserRequest;
import com.ecomerce.user.dto.UserResponse;
import com.ecomerce.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id){
//        User user = userService.fetchUser(id);
//        if (user==null) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(user);

        return userService.fetchUser(id)
                .map(ResponseEntity::ok)
                .orElseGet(() ->ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody UserRequest userRequest){
        userService.addUser(userRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody UserRequest updateUserRequest){
        boolean updated = userService.updateUser(id, updateUserRequest);

        if (updated) {
            return new ResponseEntity<>("User updated successfully.", HttpStatus.CREATED);
        }

        return ResponseEntity.notFound().build();

    }
}
