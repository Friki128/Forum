package net.esliceu.Rest_Api_Forum.Controllers;

import net.esliceu.Rest_Api_Forum.Entities.Reply;
import net.esliceu.Rest_Api_Forum.Entities.Topic;
import net.esliceu.Rest_Api_Forum.Entities.User;
import net.esliceu.Rest_Api_Forum.Exceptions.ErrorInJWTException;
import net.esliceu.Rest_Api_Forum.Exceptions.ItemNotFoundException;
import net.esliceu.Rest_Api_Forum.Services.*;
import net.esliceu.Rest_Api_Forum.Utils.JwtUtil;
import net.esliceu.Rest_Api_Forum.Utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    public ResponseEntity<Object> getTopic(@PathVariable long id){
        try {
            updateService.AddView(id);
            return new ResponseEntity<>(findService.getTopic(id), HttpStatus.OK);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(new Message("Not Found"), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Object> addTopic(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> payload){
        if(!JwtUtil.validate(token)) return new ResponseEntity<>(new Message("Could Not Add Topic"), HttpStatus.UNAUTHORIZED);
        try {
            String categorySlug = payload.get("category").toString();
            String title = payload.get("title").toString();
            String content = payload.get("content").toString();
            User user = findService.getUser(JwtUtil.getUserFromToken(token));
            User newUser = findService.getUser(user.getId());
            return new ResponseEntity<>(addService.addTopic(newUser.getId(), categorySlug, content, title), HttpStatus.CREATED);
        } catch (ErrorInJWTException e) {
            return new ResponseEntity<>(new Message("Could Not Add Topic"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(new Message("Could Not Add Topic"), HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTopic(@RequestHeader("Authorization") String token, @PathVariable long id, @RequestBody Map<String, Object> payload){
        if(!JwtUtil.validate(token)) return new ResponseEntity<>(new Message("Could Not Update Topic"), HttpStatus.UNAUTHORIZED);
        try {
            String content = payload.get("content").toString();
            String title = payload.get("title").toString();
            String categorySlug = payload.get("category").toString();
            User user = findService.getUser(JwtUtil.getUserFromToken(token));
            User newUser = findService.getUser(user.getId());
            if(!permissionService.validateTopic(newUser.getId(), id))return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
            return new ResponseEntity<>(updateService.updateTopic(id, categorySlug, content, title), HttpStatus.OK);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(new Message("Could Not Update Topic"), HttpStatus.NOT_FOUND);
        } catch (ErrorInJWTException e) {
            return new ResponseEntity<>(new Message("Could Not Update Topic"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTopic(@RequestHeader("Authorization") String token, @PathVariable long id){
        if(!JwtUtil.validate(token)) return new ResponseEntity<>(new Message("Could Not Delete Topic"), HttpStatus.FORBIDDEN);
        User user = null;
        try {
            user = findService.getUser(JwtUtil.getUserFromToken(token));
            User newUser = findService.getUser(user.getId());
            if(!permissionService.validateTopic(newUser.getId(), id))return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
            return new ResponseEntity<>(deleteService.deleteTopic(id), HttpStatus.OK);
        } catch (ErrorInJWTException e) {
            return new ResponseEntity<>(new Message("Could Not Delete Topic"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(new Message("Could Not Delete Topic"), HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/{id}/replies")
    public ResponseEntity<Object> addReply(@RequestHeader("Authorization") String token, @PathVariable long id, @RequestBody Map<String, Object> payload){
        if(!JwtUtil.validate(token)) return new ResponseEntity<>(new Message("Could Not Add Reply"), HttpStatus.UNAUTHORIZED);
        try {
            String content = payload.get("content").toString();
            User user = findService.getUser(JwtUtil.getUserFromToken(token));
            User newUser = findService.getUser(user.getId());
            return new ResponseEntity<>(addService.addReply(newUser.getId(), id, content), HttpStatus.CREATED);
        } catch (ErrorInJWTException e) {
            return new ResponseEntity<>(new Message("Could Not Add Reply"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(new Message("Could Not Add Reply"), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{topicId}/replies/{id}")
    public ResponseEntity<Object> updateReply(@RequestHeader("Authorization") String token, @PathVariable long id, @PathVariable long topicId, @RequestBody Map<String, Object> payload){
        if(!JwtUtil.validate(token)) return new ResponseEntity<>(new Message("Could Not Update Reply"), HttpStatus.UNAUTHORIZED);
        try {
            String content = payload.get("content").toString();
            User user = findService.getUser(JwtUtil.getUserFromToken(token));
            User newUser = findService.getUser(user.getId());
            if(!permissionService.validateReply(newUser.getId(), id)) return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
            return new ResponseEntity<>(updateService.updateReply(id, content), HttpStatus.OK);
        } catch (ErrorInJWTException e) {
            return new ResponseEntity<>(new Message("Could Not Update Reply"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(new Message("Could Not Update Reply"), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{topicId}/replies/{id}")
    public ResponseEntity<Object> deleteReply(@RequestHeader("Authorization") String token, @PathVariable long id){
        if(!JwtUtil.validate(token)) return new ResponseEntity<>(new Message("Could Not Delete Reply"), HttpStatus.UNAUTHORIZED);
        try {
            User user = findService.getUser(JwtUtil.getUserFromToken(token));
            User newUser = findService.getUser(user.getId());
            if(!permissionService.validateReply(newUser.getId(), id)) return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
            return new ResponseEntity<>(deleteService.deleteReply(id), HttpStatus.OK);
        } catch (ErrorInJWTException e) {
            return new ResponseEntity<>(new Message("Could Not Update Reply"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(new Message("Could Not Update Reply"), HttpStatus.NOT_FOUND);
        }
    }

}
