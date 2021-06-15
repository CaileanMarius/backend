package cmir2469.backend.controller;


import cmir2469.backend.domain.User;
import cmir2469.backend.domain.validators.Validator;
import cmir2469.backend.repository.FriendRepository;
import cmir2469.backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@CrossOrigin
@RestController
public class UserController {

    private final UserRepository userRepository = new UserRepository();
    private final FriendRepository friendRepository = new FriendRepository();

    @PostMapping("/saveUser")
    public ResponseEntity<?> saveUser(@RequestBody User user){
        User userReturned;
        try{
            userReturned = userRepository.save(user);
        }catch (Validator.ValidationException e){
            return new ResponseEntity(e.getMessage(), HttpStatus.EXPECTATION_FAILED);

        }
        if(userReturned == null)
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }



    @PutMapping("/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody User user){
        User userReturned;
        User userToAdd = userRepository.findOne(user.getID());
        userToAdd.setUsername(user.getUsername());
        userToAdd.setPassword(user.getPassword());
        userToAdd.setEmail(user.getEmail());
        userToAdd.setFollowers(user.getFollowers());
        userToAdd.setFollowing(user.getFollowing());
        try{
            userReturned = userRepository.update(userToAdd);
        }catch (Validator.ValidationException e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
        if(userReturned == null)
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @GetMapping("/user/{idUser}")
    public ResponseEntity<User> findOneUser(@PathVariable Integer idUser){
        User user = userRepository.findOne(idUser);
        if( user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<User> findOneUser(@PathVariable String username){
        User user = userRepository.findOneByUsername(username);
        if( user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//    @GetMapping("/users")
//    public ResponseEntity<List<User>> getUsers(){
//        List<User> result = new ArrayList<>();
//        userRepository.findAll().forEach(user -> {
//            result.add(user);
//        });
//        result.sort(Comparator.comparing(User::getID));
//        if(result.isEmpty())
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }

}
