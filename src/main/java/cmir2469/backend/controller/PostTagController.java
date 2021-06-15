package cmir2469.backend.controller;


import cmir2469.backend.domain.PostTag;
import cmir2469.backend.domain.validators.Validator;
import cmir2469.backend.repository.FriendRepository;
import cmir2469.backend.repository.PostTagRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@CrossOrigin
@RestController
public class PostTagController {

    private final PostTagRepository postTagRepository = new PostTagRepository();
    private final FriendRepository friendRepository = new FriendRepository();

    @PostMapping("/savePostTag")
    public ResponseEntity<?> savePostTag(@RequestBody PostTag postTag){
        PostTag postTagReturned;
        try{
            postTagReturned = postTagRepository.save(postTag);
        }catch (Validator.ValidationException e){
            return new ResponseEntity(e.getMessage(), HttpStatus.EXPECTATION_FAILED);

        }
        if(postTagReturned == null)
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @DeleteMapping("/postTag/{idPostTag}")
    public ResponseEntity<?> deletePostTag(@PathVariable Integer idPostTag){
        PostTag postTag = postTagRepository.delete(idPostTag);
        if(postTag == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        friendRepository.findAll().forEach(friend -> {
            if(friend.getID().endsWith(String.valueOf(idPostTag)))
            {
                friendRepository.delete(friend.getID());
            }
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/updatePostTag")
    public ResponseEntity<?> updatePostTag(@RequestBody PostTag postTag){
        PostTag postTagReturned;
        PostTag postTagToAdd = postTagRepository.findOne(postTag.getPostID());
        postTagToAdd.setTagID(postTag.getTagID());


        try{
            postTagReturned = postTagRepository.update(postTagToAdd);
        }catch (Validator.ValidationException e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
        if(postTagReturned == null)
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @GetMapping("/postTag/{idPostTag}")
    public ResponseEntity<PostTag> findOnePostTag(@PathVariable Integer idPostTag){
        PostTag postTag = postTagRepository.findOne(idPostTag);
        if( postTag != null)
            return new ResponseEntity<>(postTag, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/postTags")
    public ResponseEntity<List<PostTag>> getPostTags(){
        List<PostTag> result = new ArrayList<>();
        postTagRepository.findAll().forEach(postTag -> {
            result.add(postTag);
        });
        result.sort(Comparator.comparing(PostTag::getPostID));
        if(result.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}