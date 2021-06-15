package cmir2469.backend.controller;


import cmir2469.backend.domain.FriendStatus;
import cmir2469.backend.domain.User;
import cmir2469.backend.domain.validators.Validator;
import cmir2469.backend.repository.FriendStatusRepository;
import cmir2469.backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@CrossOrigin
@RestController
public class FriendStatusController {

    private final FriendStatusRepository friendStatusRepository = new FriendStatusRepository();

    @PostMapping("/saveFriendStatus")
    public ResponseEntity<?> saveFriendStatus(@RequestBody FriendStatus friendStatus){
        FriendStatus friendStatusReturned;
        try{
            friendStatusReturned = friendStatusRepository.save(friendStatus);
        }catch (Validator.ValidationException e){
            return new ResponseEntity(e.getMessage(), HttpStatus.EXPECTATION_FAILED);

        }
        if(friendStatusReturned == null)
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @DeleteMapping("/friendStatus/{idFriendStatus}")
    public ResponseEntity<?> deleteFriendStatus(@PathVariable Integer idFriendStatus){
        FriendStatus friendStatus = friendStatusRepository.delete(idFriendStatus);
        if(friendStatus == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/updateFriendStatus")
    public ResponseEntity<?> updateFriendStatus(@RequestBody FriendStatus friendStatus){
        FriendStatus friendStatusReturned;
        FriendStatus friendStatusToAdd = friendStatusRepository.findOne(friendStatus.getID());
        friendStatusToAdd.setStatus(friendStatus.getStatus());

        try{
            friendStatusReturned = friendStatusRepository.update(friendStatusToAdd);
        }catch (Validator.ValidationException e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
        if(friendStatusReturned == null)
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @GetMapping("/friendStatus/{idFriendStatus}")
    public ResponseEntity<FriendStatus> findOneFriendStatus(@PathVariable Integer idFriendStatus){
        FriendStatus friendStatus = friendStatusRepository.findOne(idFriendStatus);
        if( friendStatus != null)
            return new ResponseEntity<>(friendStatus, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/friendStatuses")
    public ResponseEntity<List<FriendStatus>> getFriendStatuses(){
        List<FriendStatus> result = new ArrayList<>();
        friendStatusRepository.findAll().forEach(friendStatus -> {
            result.add(friendStatus);
        });
        result.sort(Comparator.comparing(FriendStatus::getID));
        if(result.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
