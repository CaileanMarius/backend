package cmir2469.backend.controller;


import cmir2469.backend.domain.UserTag;
import cmir2469.backend.domain.validators.Validator;
import cmir2469.backend.repository.FriendRepository;
import cmir2469.backend.repository.UserTagRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@CrossOrigin
@RestController
public class UserTagController {

    private final UserTagRepository userTagRepository = new UserTagRepository();
    private final FriendRepository friendRepository = new FriendRepository();

    @PostMapping("/saveUserTag")
    public ResponseEntity<?> saveUserTag(@RequestBody UserTag userTag){
        UserTag userTagReturned;
        try{
            userTagReturned = userTagRepository.save(userTag);
        }catch (Validator.ValidationException e){
            return new ResponseEntity(e.getMessage(), HttpStatus.EXPECTATION_FAILED);

        }
        if(userTagReturned == null)
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @DeleteMapping("/userTag/{idUserTag}")
    public ResponseEntity<?> deleteUserTag(@PathVariable String idUserTag){
        UserTag userTag = userTagRepository.delete(idUserTag);
        if(userTag == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        friendRepository.findAll().forEach(friend -> {
            if(friend.getID().endsWith(String.valueOf(idUserTag)))
            {
                friendRepository.delete(friend.getID());
            }
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/updateUserTag")
    public ResponseEntity<?> updateUserTag(@RequestBody UserTag userTag){
        UserTag userTagReturned;
        UserTag userTagToAdd = userTagRepository.findOne(userTag.getID());
        userTagToAdd.setUserID(userTag.getUserID());
        userTagToAdd.setTagID(userTag.getTagID());
        try{
            userTagReturned = userTagRepository.update(userTagToAdd);
        }catch (Validator.ValidationException e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
        if(userTagReturned == null)
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @GetMapping("/userTag/{idUserTag}")
    public ResponseEntity<UserTag> findOneUserTag(@PathVariable String idUserTag){
        UserTag userTag = userTagRepository.findOne(idUserTag);
        if( userTag != null)
            return new ResponseEntity<>(userTag, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//    @GetMapping("/userTags")
//    public ResponseEntity<List<UserTag>> getUserTags(){
//        List<UserTag> result = new ArrayList<>();
//        userTagRepository.findAll().forEach(userTag -> {
//            result.add(userTag);
//        });
//        result.sort(Comparator.comparing(UserTag::getID));
//        if(result.isEmpty())
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }

}