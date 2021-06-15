package cmir2469.backend.controller;

import cmir2469.backend.domain.Tag;
import cmir2469.backend.domain.validators.Validator;
import cmir2469.backend.repository.FriendRepository;
import cmir2469.backend.repository.TagRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@CrossOrigin
@RestController
public class TagController {

    private final TagRepository tagRepository = new TagRepository();
    private final FriendRepository friendRepository = new FriendRepository();

    @PostMapping("/saveTag")
    public ResponseEntity<?> saveTag(@RequestBody Tag tag){
        Tag tagReturned;
        try{
            tagReturned = tagRepository.save(tag);
        }catch (Validator.ValidationException e){
            return new ResponseEntity(e.getMessage(), HttpStatus.EXPECTATION_FAILED);

        }
        if(tagReturned == null)
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @DeleteMapping("/tag/{idTag}")
    public ResponseEntity<?> deleteTag(@PathVariable Integer idTag){
        Tag tag = tagRepository.delete(idTag);
        if(tag == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        friendRepository.findAll().forEach(friend -> {
            if(friend.getID().endsWith(String.valueOf(idTag)))
            {
                friendRepository.delete(friend.getID());
            }
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/updateTag")
    public ResponseEntity<?> updateTag(@RequestBody Tag tag){
        Tag tagReturned;
        Tag tagToAdd = tagRepository.findOne(tag.getID());
        tagToAdd.setName(tag.getName());


        try{
            tagReturned = tagRepository.update(tagToAdd);
        }catch (Validator.ValidationException e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
        if(tagReturned == null)
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @GetMapping("/tag/{idTag}")
    public ResponseEntity<Tag> findOneTag(@PathVariable Integer idTag){
        Tag tag = tagRepository.findOne(idTag);
        if( tag != null)
            return new ResponseEntity<>(tag, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/tags")
    public ResponseEntity<List<Tag>> getTags(){
        List<Tag> result = new ArrayList<>();
        tagRepository.findAll().forEach(tag -> {
            result.add(tag);
        });
        result.sort(Comparator.comparing(Tag::getID));
        if(result.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}