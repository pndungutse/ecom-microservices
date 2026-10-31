package com.ecomerce.user.service;


import com.ecomerce.user.dto.AddressDto;
import com.ecomerce.user.dto.UserRequest;
import com.ecomerce.user.dto.UserResponse;
import com.ecomerce.user.model.Address;
import com.ecomerce.user.model.User;
import com.ecomerce.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

//    private List<User> userList = new ArrayList<>();
    private Long nextId = 1L;

    public List<UserResponse> getAllUsers(){
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    public void addUser(UserRequest userRequest){
        User user = new User();
        updateUserFromUserRequest(user, userRequest);
        userRepository.save(user);
    }

    public Optional<UserResponse> fetchUser(Long id) {
        return  userRepository.findById(id)
                .map(this::mapToUserResponse);

    }

    public boolean updateUser(Long id, UserRequest updateUserRequest) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    updateUserFromUserRequest(existingUser, updateUserRequest);
                    userRepository.save(existingUser);
                    return true;
                }).orElse(false);
    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();

        userResponse.setId(user.getId());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhone(user.getPhone());
        userResponse.setRole(user.getRole());

        if (user.getAddress() != null) {
            AddressDto address = new AddressDto();
            address.setCity(user.getAddress().getCity());
            address.setCountry(user.getAddress().getCountry());
            address.setStreet(user.getAddress().getStreet());
            address.setState(user.getAddress().getState());
            address.setZipCode(user.getAddress().getZipCode());

            userResponse.setAddress(address);
        }

        return userResponse;
    }

    private void updateUserFromUserRequest(User user, UserRequest userRequest) {
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        user.setRole(userRequest.getRole());

        if (userRequest.getAddress()!=null) {
            Address address = new Address();
            address.setStreet(userRequest.getAddress().getStreet());
            address.setCountry(userRequest.getAddress().getCountry());
            address.setCity(userRequest.getAddress().getCity());
            address.setState(userRequest.getAddress().getState());
            address.setZipCode(userRequest.getAddress().getZipCode());

            user.setAddress(address);
        }
    }
}
