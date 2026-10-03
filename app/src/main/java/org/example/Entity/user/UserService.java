package org.example.Entity.user;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    
    /*this is a constructor for the UserService class that take a UserRepository object as a parameter 
    and assigns it to the userRepository field.
    This allows the UserService class to use the UserRepository to perform 
    database operations related to User activities. */
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User createUser(String username){
        User user = new User(username);
        return userRepository.save(user);
    }
    public List<User> getUsers(){
        return userRepository.findAll();
    }

}
