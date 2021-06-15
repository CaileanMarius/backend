package cmir2469.backend.controller;

import cmir2469.backend.domain.Friend;
import cmir2469.backend.domain.validators.Validator;
import cmir2469.backend.repository.FriendRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@CrossOrigin
@RestController
public class FriendController {

    private final FriendRepository friendRepository = new FriendRepository();

    @PostMapping("/saveFriend")
    public ResponseEntity<?> saveFriend(@RequestBody Friend friend){
        Friend friendReturned;
        try{
            friendReturned = friendRepository.save(friend);
        }catch (Validator.ValidationException e){
            return new ResponseEntity(e.getMessage(), HttpStatus.EXPECTATION_FAILED);

        }
        if(friendReturned == null)
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @DeleteMapping("/friend/{idFriend}")
    public ResponseEntity<?> deleteFriend(@PathVariable String idFriend){
        Friend friend = friendRepository.delete(idFriend);
        if(friend == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/updateFriend")
    public ResponseEntity<?> updateFriend(@RequestBody Friend friend){
        Friend friendReturned;
        Friend friendToAdd = friendRepository.findOne(friend.getID());
        friendToAdd.setUserID(friend.getUserID());
        friendToAdd.setUserFriendID(friend.getUserFriendID());
        friendToAdd.setFriendStatusID(friend.getFriendStatusID());

        try{
            friendReturned = friendRepository.update(friendToAdd);
        }catch (Validator.ValidationException e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
        if(friendReturned == null)
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @GetMapping("/friend/{idFriend}")
    public ResponseEntity<Friend> findOneFriend(@PathVariable String idFriend){
        Friend friend = friendRepository.findOne(idFriend);
        if( friend != null)
            return new ResponseEntity<>(friend, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/friends")
    public ResponseEntity<List<Friend>> getFriends(){
        List<Friend> result = new ArrayList<>();
        friendRepository.findAll().forEach(friend -> {
            result.add(friend);
        });
        result.sort(Comparator.comparing(Friend::getID));
        if(result.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}