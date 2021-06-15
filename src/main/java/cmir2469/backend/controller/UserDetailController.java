package cmir2469.backend.controller;


import cmir2469.backend.domain.UserDetail;
import cmir2469.backend.domain.validators.Validator;
import cmir2469.backend.repository.FriendRepository;
import cmir2469.backend.repository.UserDetailRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@CrossOrigin
@RestController
public class UserDetailController {

    private final UserDetailRepository userDetailRepository = new UserDetailRepository();
    private final FriendRepository friendRepository = new FriendRepository();

    @PostMapping("/saveUserDetail")
    public ResponseEntity<?> saveUserDetail(@RequestBody UserDetail userDetail){
        UserDetail userDetailReturned;
        try{
            userDetailReturned = userDetailRepository.save(userDetail);
        }catch (Validator.ValidationException e){
            return new ResponseEntity(e.getMessage(), HttpStatus.EXPECTATION_FAILED);

        }
        if(userDetailReturned == null)
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @DeleteMapping("/userDetail/{idUserDetail}")
    public ResponseEntity<?> deleteUserDetail(@PathVariable Integer idUserDetail){
        UserDetail userDetail = userDetailRepository.delete(idUserDetail);
        if(userDetail == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        friendRepository.findAll().forEach(friend -> {
            if(friend.getID().endsWith(String.valueOf(idUserDetail)))
            {
                friendRepository.delete(friend.getID());
            }
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @PutMapping("/updateUserDetail")
//    public ResponseEntity<?> updateUserDetail(@RequestBody UserDetail userDetail){
//        UserDetail userDetailReturned;
//        UserDetail userDetailToAdd = userDetailRepository.findOne(userDetail.getID());
//        userDetailToAdd.setFirstName(userDetail.getFirstName());
//        userDetailToAdd.setLastName(userDetail.getLastName());
//        userDetailToAdd.setPhoto(userDetail.getPhoto());
//        userDetailToAdd.setBirthday(userDetail.getBirthday());
//        userDetailToAdd.setDescription(userDetail.getDescription());
//        try{
//            userDetailReturned = userDetailRepository.update(userDetailToAdd);
//        }catch (Validator.ValidationException e)
//        {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
//        }
//        if(userDetailReturned == null)
//            return new ResponseEntity<>(HttpStatus.OK);
//        return new ResponseEntity<>(HttpStatus.CONFLICT);
//    }

//    @GetMapping("/userDetail/{idUserDetail}")
//    public ResponseEntity<UserDetail> findOneUserDetail(@PathVariable Integer idUserDetail){
//        UserDetail userDetail = userDetailRepository.findOne(idUserDetail);
//        if( userDetail != null)
//            return new ResponseEntity<>(userDetail, HttpStatus.OK);
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }

    @GetMapping("/userDetails")
    public ResponseEntity<List<UserDetail>> getUserDetails(){
        List<UserDetail> result = new ArrayList<>();
        userDetailRepository.findAll().forEach(userDetail -> {
            result.add(userDetail);
        });
        result.sort(Comparator.comparing(UserDetail::getID));
        if(result.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}