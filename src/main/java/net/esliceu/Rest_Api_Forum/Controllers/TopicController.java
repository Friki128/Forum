package net.esliceu.Rest_Api_Forum.Controllers;

import net.esliceu.Rest_Api_Forum.Entities.Reply;
import net.esliceu.Rest_Api_Forum.Entities.Topic;
import net.esliceu.Rest_Api_Forum.Entities.User;
import net.esliceu.Rest_Api_Forum.Exceptions.ErrorInJWTException;
import net.esliceu.Rest_Api_Forum.Exceptions.ItemNotFoundException;
import net.esliceu.Rest_Api_Forum.Services.*;
import net.esliceu.Rest_Api_Forum.Utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topics")
public class TopicController {
    @Autowired
    PermissionService permissionService;
    @Autowired
    AddService addService;
    @Autowired
    FindService findService;
    @Autowired
    FindAllService findAllService;
    @Autowired
    UpdateService updateService;
    @Autowired
    DeleteService deleteService;

    @GetMapping("/{id}")
    public ResponseEntity<Topic> getTopic(@RequestHeader("Authorization") String token, @PathVariable long id){
        if(!JwtUtil.validate(token)) return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        try {
            updateService.AddView(id);
            return new ResponseEntity<>(findService.getTopic(id), HttpStatus.OK);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/")
    public ResponseEntity<Topic> addTopic(@RequestHeader("Authorization") String token, @RequestParam String categorySlug, @RequestParam String content, @RequestParam String title){
        if(!JwtUtil.validate(token)) return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        try {
            User user = JwtUtil.getUserFromToken(token);
            User newUser = findService.getUser(user.getId());
            return new ResponseEntity<>(addService.addTopic(newUser.getId(), categorySlug, content, title), HttpStatus.CREATED);
        } catch (ErrorInJWTException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<Topic> updateTopic(@RequestHeader("Authorization") String token, @PathVariable long id, @RequestParam String title, @RequestParam String content, @RequestParam String categorySlug){
        if(!JwtUtil.validate(token)) return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        try {
            User user = JwtUtil.getUserFromToken(token);
            User newUser = findService.getUser(user.getId());
            if(!permissionService.validateTopic(newUser.getId(), id))return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
            return new ResponseEntity<>(updateService.updateTopic(id, categorySlug, content, title), HttpStatus.OK);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ErrorInJWTException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteTopic(@RequestHeader("Authorization") String token, @PathVariable long id){
        if(!JwtUtil.validate(token)) return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
        User user = null;
        try {
            user = JwtUtil.getUserFromToken(token);
            User newUser = findService.getUser(user.getId());
            if(!permissionService.validateTopic(newUser.getId(), id))return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
            return new ResponseEntity<>(deleteService.deleteTopic(id), HttpStatus.OK);
        } catch (ErrorInJWTException e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/{id}/replies")
    public ResponseEntity<Reply> addReply(@RequestHeader("Authorization") String token, @PathVariable long id, @RequestParam String content){
        if(!JwtUtil.validate(token)) return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        try {
            User user = JwtUtil.getUserFromToken(token);
            User newUser = findService.getUser(user.getId());
            return new ResponseEntity<>(addService.addReply(newUser.getId(), id, content), HttpStatus.CREATED);
        } catch (ErrorInJWTException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
