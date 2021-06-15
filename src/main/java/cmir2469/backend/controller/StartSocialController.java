package cmir2469.backend.controller;


import cmir2469.backend.domain.*;
import cmir2469.backend.domain.dtos.*;
import cmir2469.backend.domain.dtos.request.AuthenticationRequest;
import cmir2469.backend.domain.dtos.response.AuthenticationResponse;
import cmir2469.backend.domain.validators.Validator;
import cmir2469.backend.repository.*;
import cmir2469.backend.springSecurity.JwtUtil;
import cmir2469.backend.springSecurity.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@CrossOrigin
@RestController
public class StartSocialController {

    private final JwtUtil jwtUtil = new JwtUtil();
    private final MyUserDetailsService userDetailsService = new MyUserDetailsService();
    private final UserRepository userRepository = new UserRepository();
    private final PostRepository postRepository = new PostRepository();
    private final CommentRepository commentRepository = new CommentRepository();
    private final TagRepository tagRepository = new TagRepository();
    private final UserTagRepository userTagRepository = new UserTagRepository();
    private final UserDetailRepository userDetailRepository= new UserDetailRepository();
    private final ReactionRepository reactionRepository = new ReactionRepository();
    private final FriendRepository friendRepository = new FriendRepository();

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest){
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        User user = userRepository.findOneByUsername(authenticationRequest.getUsername());
        authenticationResponse.setJwt(jwt);

        return ResponseEntity.ok(authenticationResponse);

    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserSignUpDTO userSignUpDTO){
        User userReturned;
        UserTag userTagReturned;
        UserDetail userDetailReturned;


        try{
        User userToSave = new User();
        userToSave.setID(0);
        userToSave.setUsername(userSignUpDTO.getUsername());
        userToSave.setPassword(userSignUpDTO.getPassword());
        userToSave.setEmail(userSignUpDTO.getEmail());
        userToSave.setFollowing(0);
        userToSave.setFollowers(0);
        userReturned = userRepository.save(userToSave);
        for(int index=0;index<userSignUpDTO.getTags().size();index++){
            UserTag userTagToSave = new UserTag();
            userTagToSave.setUserID(userToSave.getID());
            Tag tag = tagRepository.findOneByName(userSignUpDTO.getTags().get(index));
            userTagToSave.setTagID(tag.getID());
            userTagToSave.setID(userToSave.getID()+""+tag.getID());
            userTagReturned = userTagRepository.save(userTagToSave);
            if(userTagReturned!=null)
                return new ResponseEntity<>(HttpStatus.CONFLICT);

        }

        UserDetail userDetailToSave = new UserDetail();
        userDetailToSave.setID(userToSave.getID());
        userDetailToSave.setFirstName(userSignUpDTO.getFirstName());
        userDetailToSave.setLastName(userSignUpDTO.getLastName());
        userDetailToSave.setBirthday(userSignUpDTO.getBirthday());
        userDetailToSave.setAccount(userToSave);
        userDetailReturned = userDetailRepository.save(userDetailToSave);

        } catch (Validator.ValidationException e) {
            return  new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
        if(userReturned == null && userDetailReturned== null )
            return new ResponseEntity<>(HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }



    @PostMapping("/createNewPost")
    public ResponseEntity<?> createNewPost(@RequestBody PostDTO postDTO){

        try {
            Post postReturned ;
            Post postToAdd = new Post();
            User user = userRepository.findOneByUsername(postDTO.getUsernameUser());
            Tag tag = tagRepository.findOneByName(postDTO.getTagName());
            postToAdd.setID(0);
            postToAdd.setUser(user);
            postToAdd.setTagID(tag.getID());
            postToAdd.setCreatedDate(postDTO.getCreatedDate());
            postToAdd.setDescription(postDTO.getDescription());
            postToAdd.setLikes(0);
            postToAdd.setDislikes(0);
            postReturned = postRepository.save(postToAdd);
            if(postReturned == null)
                return new ResponseEntity<>(HttpStatus.OK);

        } catch (Validator.ValidationException e) {
            return  new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);



    }


    @PutMapping("/updateUserDetail")
    public ResponseEntity<?> updateUserDetail(@RequestBody UserDetailDTO userDetailDTO){

        UserDetail userDetailReturned;
        User user = userRepository.findOneByUsername(userDetailDTO.getUsername());
        UserDetail userDetail = userDetailRepository.findOne(user.getID());
        try{
            userDetail.setFirstName(userDetailDTO.getFirstName());
            userDetail.setLastName(userDetailDTO.getLastName());
            userDetail.setDescription(userDetailDTO.getDescription());
            userDetailReturned = userDetailRepository.update(userDetail);

        } catch (Validator.ValidationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
        if(userDetailReturned == null)
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.CONFLICT);

    }

    @DeleteMapping("/deleteUser/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username){
        User user = userRepository.findOneByUsername(username);
        User userReturned = userRepository.delete(user.getID());
        if(userReturned == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/allComments/{idPost}")
    public ResponseEntity<List<CommentDTO>> getAllCommentsFromOnePost(@PathVariable Integer idPost) {
        List<CommentDTO> result = new ArrayList<>();
        List<Comment> comments = commentRepository.findAllCommentOfOnePost(idPost);
        if(comments == null)
            return new ResponseEntity<>( HttpStatus.NOT_FOUND);
        else {
            comments.forEach(comment -> {
                CommentDTO commentDTO = new CommentDTO();
                User user = userRepository.findOne(comment.getUserID());
                commentDTO.setUsernameUser(user.getUsername());
                commentDTO.setPostID(idPost);
                commentDTO.setIdComment(comment.getID());
                commentDTO.setCreatedDate(comment.getCreatedDate());
                commentDTO.setDescription(comment.getDescription());
                result.add(commentDTO);
            });
        }


        result.sort(Comparator.comparing(CommentDTO::getCreatedDate));
        if(result.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/allPostsFromUser/{username}")
    public ResponseEntity<List<PostListViewDTO>> getAllPostFromOneUser(@PathVariable String username) {
        List<PostListViewDTO> result = new ArrayList<>();
        User user = userRepository.findOneByUsername(username);
        List<Post> postUsers = postRepository.findAllPostByOneUser(user.getID());
        if(postUsers == null)
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND);
        postRepository.findAllPostByOneUser(user.getID()).forEach(post -> {
                PostListViewDTO postListViewDTO = new PostListViewDTO();
                postListViewDTO.setIdPost(post.getID());
                postListViewDTO.setUsernameUser(username);
                Tag tag = tagRepository.findOne(post.getTagID());
                postListViewDTO.setTagName(tag.getName());
                postListViewDTO.setCreatedDate(post.getCreatedDate());
                postListViewDTO.setDescription(post.getDescription());
//                postListViewDTO.setLikes(this.getAllLikesToOnePost(postListViewDTO.getIdPost()).getBody());
//                postListViewDTO.setDislikes(this.getAllDislikesToOnePost(postListViewDTO.getIdPost()).getBody());
                postListViewDTO.setLikes(post.getLikes());
                postListViewDTO.setDislikes(post.getDislikes());
                if (this.getAllCommentsFromOnePost(post.getID()).getBody() != null)
                    postListViewDTO.setCommentDTOList(this.getAllCommentsFromOnePost(post.getID()).getBody());
                else
                    postListViewDTO.setCommentDTOList(new ArrayList<>());
                result.add(postListViewDTO);
        });
        if(result.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/addComment/{idPost}")
    public ResponseEntity<?> addComment(@PathVariable Integer idPost, @RequestBody CommentDTO commentDTO ){
        Comment commentToAdd = new Comment();
        Comment commentReturned;
        User user = userRepository.findOneByUsername(commentDTO.getUsernameUser());
        commentToAdd.setID(0);
        Post post = postRepository.findOne(idPost);
        commentToAdd.setPost(post);
        commentToAdd.setUser(user);
        commentToAdd.setDescription(commentDTO.getDescription());
        commentToAdd.setCreatedDate(commentDTO.getCreatedDate());
        try{
            commentReturned = commentRepository.save(commentToAdd);
        } catch (Validator.ValidationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
        if(commentReturned == null){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);

    }

    @DeleteMapping("/deleteComment/{idComment}")
    public ResponseEntity<?> deleteComment(@PathVariable Integer idComment){
        Comment comment = commentRepository.delete(idComment);
        if(comment == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(HttpStatus.OK);

    }


    @DeleteMapping("/deletePost/{idPost}")
    public ResponseEntity<?> deletePost(@PathVariable Integer idPost){
        Post post = postRepository.delete(idPost);
        if(post == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(HttpStatus.OK);

    }


    @GetMapping("/allTags")
    public ResponseEntity<List<String>> getAllTags(){
        List<String> tags = new ArrayList<>();
        tagRepository.findAll().forEach(tag -> {
            tags.add(tag.getName());
        });
        return new ResponseEntity<>(tags, HttpStatus.OK);


    }

    @GetMapping("/allLikes/{idPost}")
    public ResponseEntity<Integer> getAllLikesToOnePost(@PathVariable Integer idPost){
        Integer likes= reactionRepository.findAllLikesToOnePost(idPost);
        return new ResponseEntity<>(likes, HttpStatus.OK);
    }



    @PostMapping("/sentRequest/{username}/{usernameToAdd}")
    public ResponseEntity<?> sentRequestToOneUser(@PathVariable String username, @PathVariable String usernameToAdd){
        User user = userRepository.findOneByUsername(username);
        User userToAdd = userRepository.findOneByUsername(usernameToAdd);
        Integer status = 0;
        Friend friendRequest = new Friend();
        Friend friendReturned ;
        friendRequest.setUser(user);
        friendRequest.setFriendOf(userToAdd);
        friendRequest.setFriendStatusID(status);
        friendRequest.setID(user.getID()+""+userToAdd.getID());
        try{
            friendReturned = friendRepository.save(friendRequest);
        } catch (Validator.ValidationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
        if(friendReturned == null){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PutMapping("/addLike/{username}/{idPost}")
    public  ResponseEntity<?> addLikeToOnePost(@PathVariable String username,@PathVariable Integer idPost){

        Post post = postRepository.findOne(idPost);
        Reaction reaction = new Reaction() ;
        User user = userRepository.findOneByUsername(username);
        post.setLikes(post.getLikes()+1);
        Post postReturned ;
        Reaction reactionReturned;
        try{
            reaction.setUser(user);
            reaction.setPost(post);
            reaction.setStatus("1");
            reaction.setID(user.getID()+""+idPost);
            reactionReturned = reactionRepository.save(reaction);
            postReturned = postRepository.update(post);
        } catch (Validator.ValidationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
        if(postReturned == null && reactionReturned == null){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PutMapping("/addDislike/{username}/{idPost}")
    public ResponseEntity<?> addDislikeToOnePost(@PathVariable String username, @PathVariable Integer idPost){
        Post post = postRepository.findOne(idPost);
        Reaction reaction = new Reaction();
        User user = userRepository.findOneByUsername(username);
        post.setDislikes(post.getDislikes()+1);
        Post postReturned;
        Reaction reactionReturned;
        try{
            reaction.setUser(user);
            reaction.setPost(post);
            reaction.setStatus("-1");
            reaction.setID(user.getID()+""+post.getID());
            reactionReturned = reactionRepository.save(reaction);
            postReturned = postRepository.update(post);
        } catch (Validator.ValidationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
        if(postReturned == null && reactionReturned == null){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);

    }


    @PutMapping("/acceptFriendRequest/{username}/{usernameToAccept}")
    public ResponseEntity<?> acceptFriendRequest(@PathVariable String username, @PathVariable String usernameToAccept){
        User user = userRepository.findOneByUsername(username);
        User userToAdd = userRepository.findOneByUsername(usernameToAccept);
        Friend friendRequest = friendRepository.findOne(user.getID()+""+userToAdd.getID());
        Friend friendRequestReturned ;
        User userReturned;
        User userToAddReturned;
        try{

            user.setFollowers(user.getFollowers()+1);
            userToAdd.setFollowing(userToAdd.getFollowing()+1);
            userReturned = userRepository.update(user);
            userToAddReturned = userRepository.update(userToAdd);
            friendRequest.setFriendStatusID(1);
            friendRequest.setUser(user);
            friendRequest.setFriendOf(userToAdd);
            friendRequestReturned = friendRepository.update(friendRequest);
        } catch (Validator.ValidationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
        if(friendRequestReturned == null && userReturned == null && userToAddReturned == null){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);


    }

    @PutMapping("/declineFriendRequest/{username}/{usernameToDecline}")
    public ResponseEntity<?> declineFriendRequest(@PathVariable String username, @PathVariable String usernameToDecline){
        User user = userRepository.findOneByUsername(username);
        User userToDecline = userRepository.findOneByUsername(usernameToDecline);
        Friend friendRequest = friendRepository.findOne(user.getID()+""+userToDecline.getID());
        Friend friendRequestReturned ;
        try{
            friendRequest.setFriendStatusID(-1);
            friendRequestReturned = friendRepository.update(friendRequest);
        } catch (Validator.ValidationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
        if(friendRequestReturned == null ){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }


    @GetMapping("/allFriendRequests/{username}")
    public ResponseEntity<List<String>> getAllFriendRequestsToOneUser(@PathVariable String username){

        User user = userRepository.findOneByUsername(username);
        List<String> friendRequests = new ArrayList<>();
        List<Friend> friendRequestsList = friendRepository.findAllFriendRequests(user.getID());
        if(friendRequestsList == null){
            return  new ResponseEntity<>(friendRequests, HttpStatus.OK);
        }
        else {
            friendRequestsList.forEach(friend -> {
                User user1 = userRepository.findOne(friend.getUserFriendID());
                friendRequests.add(user1.getUsername());

            });
        }
        if(friendRequests.isEmpty())
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(friendRequests, HttpStatus.OK);

    }


    @GetMapping("/allFriends/{username}")
    public ResponseEntity<List<ListFriend>> getAllFriendsToOneUser(@PathVariable String username){

        User user = userRepository.findOneByUsername(username);
        List<ListFriend> friends = new ArrayList<>();
        List<Integer> friendIDs = new ArrayList<>();
        List<Friend> friendList = friendRepository.findAllFriendsForOneUser(user.getID());
        if(friendList == null){
            return new ResponseEntity<>(friends,HttpStatus.OK);
        }
        else {
            for (Friend friend : friendRepository.findAllFriendsForOneUser(user.getID())) {
                if (friend.getFriendStatusID().equals(1))
                    friendIDs.add(friend.getUserFriendID());
            }
            if (friendIDs.isEmpty())
                return new ResponseEntity<>(friends, HttpStatus.OK);
            else {
                friendIDs.forEach(id -> {
                    User userToAdd = userRepository.findOne(id);
                    UserTag userTagToAdd = userTagRepository.findOne(user.getID()+""+userToAdd.getID());
                    ListFriend listFriend  = new ListFriend();
                    listFriend.setUsernameFriend(userToAdd.getUsername());
                    Set<Tag> tags = userToAdd.getTags();
                    List<String> tagsToAdd = new ArrayList<>();
                    tags.forEach(tag ->{
                        tagsToAdd.add(tag.getName());
                    });
                    listFriend.setTags(tagsToAdd);
                    friends.add(listFriend);
                });
            }
        }

        if(friends.isEmpty())
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(friends, HttpStatus.OK);
    }


    @GetMapping("/allDislikes/{idPost}")
    public ResponseEntity<Integer> getAllDislikesToOnePost(@PathVariable Integer idPost){
        Integer likes= reactionRepository.findAllDislikesToOnePost(idPost);
        return new ResponseEntity<>(likes, HttpStatus.OK);
    }



    @GetMapping("/userDetail/{username}")
    public ResponseEntity<UserDetailDTO> getUserDetail(@PathVariable String username) {
        User user = userRepository.findOneByUsername(username);
        UserDetail userDetail = userDetailRepository.findOne(user.getID());
        if (userDetail == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            UserDetailDTO userDetailDTOReturned = new UserDetailDTO();
            userDetailDTOReturned.setUsername(user.getUsername());
            userDetailDTOReturned.setLastName(userDetail.getLastName());
            userDetailDTOReturned.setFirstName(userDetail.getFirstName());
            userDetailDTOReturned.setDescription(userDetail.getDescription());
            return new ResponseEntity<>(userDetailDTOReturned, HttpStatus.OK);


        }
    }



    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(){
        List<User> result = new ArrayList<>();
        userRepository.findAll().forEach(user -> {
            result.add(user);
        });
        result.sort(Comparator.comparing(User::getID));
        if(result.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }




}
