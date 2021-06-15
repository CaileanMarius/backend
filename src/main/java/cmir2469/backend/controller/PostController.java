package cmir2469.backend.controller;

import cmir2469.backend.domain.Post;
import cmir2469.backend.domain.validators.Validator;
import cmir2469.backend.repository.FriendRepository;
import cmir2469.backend.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@CrossOrigin
@RestController
public class PostController {

    private final PostRepository postRepository = new PostRepository();
    private final FriendRepository friendRepository = new FriendRepository();

    @PostMapping("/savePost")
    public ResponseEntity<?> savePost(@RequestBody Post post){
        Post postReturned;
        try{
            postReturned = postRepository.save(post);
        }catch (Validator.ValidationException e){
            return new ResponseEntity(e.getMessage(), HttpStatus.EXPECTATION_FAILED);

        }
        if(postReturned == null)
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @DeleteMapping("/post/{idPost}")
    public ResponseEntity<?> deletePost(@PathVariable Integer idPost){
        Post post = postRepository.delete(idPost);
        if(post == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        friendRepository.findAll().forEach(friend -> {
            if(friend.getID().endsWith(String.valueOf(idPost)))
            {
                friendRepository.delete(friend.getID());
            }
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/updatePost")
    public ResponseEntity<?> updatePost(@RequestBody Post post){
        Post postReturned;
        Post postToAdd = postRepository.findOne(post.getID());
        postToAdd.setUserID(post.getUserID());
        postToAdd.setCreatedDate(post.getCreatedDate());
        postToAdd.setPhoto(post.getPhoto());
        postToAdd.setDescription(post.getDescription());
        postToAdd.setLikes(post.getLikes());
        postToAdd.setDislikes(post.getDislikes());

        try{
            postReturned = postRepository.update(postToAdd);
        }catch (Validator.ValidationException e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
        if(postReturned == null)
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @GetMapping("/post/{idPost}")
    public ResponseEntity<Post> findOnePost(@PathVariable Integer idPost){
        Post post = postRepository.findOne(idPost);
        if( post != null)
            return new ResponseEntity<>(post, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getPosts(){
        List<Post> result = new ArrayList<>();
        postRepository.findAll().forEach(post -> {
            result.add(post);
        });
        result.sort(Comparator.comparing(Post::getID));
        if(result.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}