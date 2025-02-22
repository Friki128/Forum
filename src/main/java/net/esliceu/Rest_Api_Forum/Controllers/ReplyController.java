package net.esliceu.Rest_Api_Forum.Controllers;

import net.esliceu.Rest_Api_Forum.Entities.Reply;
import net.esliceu.Rest_Api_Forum.Entities.User;
import net.esliceu.Rest_Api_Forum.Exceptions.ErrorInJWTException;
import net.esliceu.Rest_Api_Forum.Exceptions.ItemNotFoundException;
import net.esliceu.Rest_Api_Forum.Services.*;
import net.esliceu.Rest_Api_Forum.Utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/replies")
public class ReplyController {
    @Autowired
    PermissionService permissionService;
    @Autowired
    DeleteService deleteService;
    @Autowired
    AddService addService;
    @Autowired
    UpdateService updateService;
    @Autowired
    FindService findService;

    @PutMapping("/{id}")
    public ResponseEntity<Reply> updateReply(@RequestHeader("Authorization") String token, @PathVariable long id, @RequestParam String content){
        if(!JwtUtil.validate(token)) return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        try {
            User user = JwtUtil.getUserFromToken(token);
            User newUser = findService.getUser(user.getId());
            if(!permissionService.validateReply(newUser.getId(), id)) return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
            return new ResponseEntity<>(updateService.updateReply(id, content), HttpStatus.OK);
        } catch (ErrorInJWTException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteReply(@RequestHeader("Authorization") String token, @PathVariable long id){
        if(!JwtUtil.validate(token)) return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
        try {
            User user = JwtUtil.getUserFromToken(token);
            User newUser = findService.getUser(user.getId());
            if(!permissionService.validateReply(newUser.getId(), id)) return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
            return new ResponseEntity<>(deleteService.deleteReply(id), HttpStatus.OK);
        } catch (ErrorInJWTException e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
