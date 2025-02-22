package net.esliceu.Rest_Api_Forum.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.esliceu.Rest_Api_Forum.Entities.User;
import net.esliceu.Rest_Api_Forum.Exceptions.EmailAlreadyInUserException;
import net.esliceu.Rest_Api_Forum.Exceptions.ErrorInJWTException;
import net.esliceu.Rest_Api_Forum.Exceptions.ItemNotFoundException;
import net.esliceu.Rest_Api_Forum.Services.AddService;
import net.esliceu.Rest_Api_Forum.Services.FindAllService;
import net.esliceu.Rest_Api_Forum.Services.FindService;
import net.esliceu.Rest_Api_Forum.Services.UpdateService;
import net.esliceu.Rest_Api_Forum.Utils.JwtUtil;
import net.esliceu.Rest_Api_Forum.Utils.UserIat;
import net.esliceu.Rest_Api_Forum.Utils.UserPermissions;
import net.esliceu.Rest_Api_Forum.Utils.UserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class UserController {
    @Autowired
    UpdateService updateService;
    @Autowired
    FindService findService;
    @Autowired
    AddService addService;
    @Autowired
    FindAllService findAllService;

    @PutMapping("/password")
    public ResponseEntity<Boolean> updatePassword(@RequestHeader("Authorization") String token, @RequestParam String currentPassword, @RequestParam String newPassword){
        if(!JwtUtil.validate(token)) return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
        try {
            User user = JwtUtil.getUserFromToken(token);
            Boolean result = updateService.updatePassword(user.getEmail(), currentPassword, newPassword);
            if(result) return new ResponseEntity<>(true, HttpStatus.OK);
            return new ResponseEntity<>(false, HttpStatus.CONFLICT);
        } catch (ErrorInJWTException e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<UserToken> updateProfile(@RequestHeader("Authorization") String token, @RequestParam String avatar, @RequestParam String email, @RequestParam String name){
        if(!JwtUtil.validate(token)) return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        try {
            User user = JwtUtil.getUserFromToken(token);
            User newUser = updateService.updateUser(user.getId(), avatar, email, name);
            UserPermissions userPermissions = new UserPermissions(newUser, findAllService.getAllCategoriesWhereUserModerates(newUser.getId()));
            return new ResponseEntity<>(new UserToken(userPermissions, JwtUtil.getToken(newUser)), HttpStatus.OK);

        } catch (ErrorInJWTException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EmailAlreadyInUserException e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserToken> login(@RequestParam String email, @RequestParam String password){
        User user = findService.login(email, password);
        try {
            UserPermissions userPermissions = new UserPermissions(user, findAllService.getAllCategoriesWhereUserModerates(user.getId()));
            return new ResponseEntity<>(new UserToken(userPermissions, JwtUtil.getToken(user)), HttpStatus.OK);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerWithModerate(@RequestParam String email, @RequestParam(required = false) String moderateCategory, @RequestParam String name, @RequestParam String password, @RequestParam String role){
        try {
            User user = addService.addUser(email, name, role, password);
            if(moderateCategory != null) updateService.addModerator(user, moderateCategory);
            return new ResponseEntity<>("Ok", HttpStatus.OK);
        } catch (EmailAlreadyInUserException e) {
            return new ResponseEntity<>("Email in use", HttpStatus.CONFLICT);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getProfile")
    public ResponseEntity<UserIat> getProfile(@RequestHeader("Authorization") String token){
        if(!JwtUtil.validate(token)) return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        try {
            User user = JwtUtil.getUserFromToken(token);
            User newUser = findService.getUser(user.getId());
            UserPermissions userPermissions = new UserPermissions(newUser, findAllService.getAllCategoriesWhereUserModerates(newUser.getId()));
            return new ResponseEntity<>(new UserIat(userPermissions, JwtUtil.getIat(token)), HttpStatus.OK);
        } catch (ErrorInJWTException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
