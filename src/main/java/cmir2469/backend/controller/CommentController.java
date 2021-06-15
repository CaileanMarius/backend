package cmir2469.backend.controller;


import cmir2469.backend.domain.Comment;
import cmir2469.backend.domain.validators.Validator;
import cmir2469.backend.repository.FriendRepository;
import cmir2469.backend.repository.CommentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@CrossOrigin
@RestController
public class CommentController {

    private final CommentRepository commentRepository = new CommentRepository();
    private final FriendRepository friendRepository = new FriendRepository();

    @PostMapping("/saveComment")
    public ResponseEntity<?> saveComment(@RequestBody Comment comment){
        Comment commentReturned;
        try{
            commentReturned = commentRepository.save(comment);
        }catch (Validator.ValidationException e){
            return new ResponseEntity(e.getMessage(), HttpStatus.EXPECTATION_FAILED);

        }
        if(commentReturned == null)
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @DeleteMapping("/comment/{idComment}")
    public ResponseEntity<?> deleteComment(@PathVariable Integer idComment){
        Comment comment = commentRepository.delete(idComment);
        if(comment == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        friendRepository.findAll().forEach(friend -> {
            if(friend.getID().endsWith(String.valueOf(idComment)))
            {
                friendRepository.delete(friend.getID());
            }
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/updateComment")
    public ResponseEntity<?> updateComment(@RequestBody Comment comment){
        Comment commentReturned;
        Comment commentToAdd = commentRepository.findOne(comment.getID());
        commentToAdd.setUserID(comment.getUserID());
        commentToAdd.setPostID(comment.getPostID());
        commentToAdd.setDescription(comment.getDescription());
        commentToAdd.setCreatedDate(comment.getCreatedDate());
        try{
            commentReturned = commentRepository.update(commentToAdd);
        }catch (Validator.ValidationException e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
        if(commentReturned == null)
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @GetMapping("/comment/{idComment}")
    public ResponseEntity<Comment> findOneComment(@PathVariable Integer idComment){
        Comment comment = commentRepository.findOne(idComment);
        if( comment != null)
            return new ResponseEntity<>(comment, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/comments")
    public ResponseEntity<List<Comment>> getComments(){
        List<Comment> result = new ArrayList<>();
        commentRepository.findAll().forEach(comment -> {
            result.add(comment);
        });
        result.sort(Comparator.comparing(Comment::getID));
        if(result.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}