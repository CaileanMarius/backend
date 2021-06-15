package cmir2469.backend.controller;


import cmir2469.backend.domain.Reaction;
import cmir2469.backend.domain.validators.Validator;
import cmir2469.backend.repository.FriendRepository;
import cmir2469.backend.repository.ReactionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@CrossOrigin
@RestController
public class ReactionController {

    private final ReactionRepository reactionRepository = new ReactionRepository();
    private final FriendRepository friendRepository = new FriendRepository();

    @PostMapping("/saveReaction")
    public ResponseEntity<?> saveReaction(@RequestBody Reaction reaction){
        Reaction reactionReturned;
        try{
            reactionReturned = reactionRepository.save(reaction);
        }catch (Validator.ValidationException e){
            return new ResponseEntity(e.getMessage(), HttpStatus.EXPECTATION_FAILED);

        }
        if(reactionReturned == null)
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @DeleteMapping("/reaction/{idReaction}")
    public ResponseEntity<?> deleteReaction(@PathVariable String idReaction){
        Reaction reaction = reactionRepository.delete(idReaction);
        if(reaction == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        friendRepository.findAll().forEach(friend -> {
            if(friend.getID().endsWith(String.valueOf(idReaction)))
            {
                friendRepository.delete(friend.getID());
            }
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/updateReaction")
    public ResponseEntity<?> updateReaction(@RequestBody Reaction reaction){
        Reaction reactionReturned;
        Reaction reactionToAdd = reactionRepository.findOne(reaction.getID());
        reactionToAdd.setPostID(reaction.getPostID());
        reactionToAdd.setUserID(reaction.getUserID());
        reactionToAdd.setStatus(reaction.getStatus());

        try{
            reactionReturned = reactionRepository.update(reactionToAdd);
        }catch (Validator.ValidationException e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
        if(reactionReturned == null)
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @GetMapping("/reaction/{idReaction}")
    public ResponseEntity<Reaction> findOneReaction(@PathVariable String idReaction){
        Reaction reaction = reactionRepository.findOne(idReaction);
        if( reaction != null)
            return new ResponseEntity<>(reaction, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/reactions")
    public ResponseEntity<List<Reaction>> getReactions(){
        List<Reaction> result = new ArrayList<>();
        reactionRepository.findAll().forEach(reaction -> {
            result.add(reaction);
        });
        result.sort(Comparator.comparing(Reaction::getID));
        if(result.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}