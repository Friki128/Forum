package net.esliceu.Rest_Api_Forum.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.esliceu.Rest_Api_Forum.Entities.Image;
import net.esliceu.Rest_Api_Forum.Entities.User;
import net.esliceu.Rest_Api_Forum.Exceptions.EmailAlreadyInUserException;
import net.esliceu.Rest_Api_Forum.Exceptions.ErrorInJWTException;
import net.esliceu.Rest_Api_Forum.Exceptions.ItemNotFoundException;
import net.esliceu.Rest_Api_Forum.Services.AddService;
import net.esliceu.Rest_Api_Forum.Services.FindAllService;
import net.esliceu.Rest_Api_Forum.Services.FindService;
import net.esliceu.Rest_Api_Forum.Services.UpdateService;
import net.esliceu.Rest_Api_Forum.Utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

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

    @Value("${page.url}")
    private String page;

    @PutMapping("/profile/password")
    public ResponseEntity<Object> updatePassword(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> payload){
        if(!JwtUtil.validate(token)) return new ResponseEntity<>(new Message("Could Not Update Password"), HttpStatus.UNAUTHORIZED);
        try {
            String currentPassword = payload.get("currentPassword").toString();
            String newPassword = payload.get("newPassword").toString();
            User user = findService.getUser(JwtUtil.getUserFromToken(token));
            Boolean result = updateService.updatePassword(user.getEmail(), currentPassword, newPassword);
            if(result) return new ResponseEntity<>(true, HttpStatus.OK);
            return new ResponseEntity<>(new Message("Could Not Update Password"), HttpStatus.CONFLICT);
        } catch (ErrorInJWTException e) {
            return new ResponseEntity<>(new Message("Could Not Update Password"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(new Message("Could Not Update Password"), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<Object> updateProfile(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> payload){
        if(!JwtUtil.validate(token)) return new ResponseEntity<>(new Message("Could Not Update Profile"), HttpStatus.UNAUTHORIZED);
        try {
            String email = payload.get("email").toString();
            String avatar = "";
            String name = payload.get("name").toString();
            User user = findService.getUser(JwtUtil.getUserFromToken(token));
            if(payload.get("avatar") != null){
                byte[] image = Base64.getDecoder().decode(payload.get("avatar").toString().split(",")[1]);
                Image newImage = findService.getImgByUser(user.getId());
                if(newImage != null) newImage = updateService.updateImage(newImage.getId(), image);
                else newImage = addService.addImage(user.getId(), image);
                avatar = page + "/img/" + newImage.getId();

            }
            User newUser = updateService.updateUser(user.getId(), avatar, email, name);
            UserPermissions userPermissions = new UserPermissions(newUser, findAllService.getAllCategoriesWhereUserModerates(newUser.getId()));
            return new ResponseEntity<>(new UserToken(userPermissions, JwtUtil.getToken(userPermissions)), HttpStatus.OK);

        } catch (ErrorInJWTException e) {
            return new ResponseEntity<>(new Message("Could Not Update Profile"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EmailAlreadyInUserException e) {
            return new ResponseEntity<>(new Message("Could Not Update Profile"), HttpStatus.CONFLICT);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(new Message("Could Not Update Profile"), HttpStatus.NOT_FOUND);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>(new Message("Could Not Update Profile"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            return new ResponseEntity<>(new Message("Could Not Update Profile"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Cacheable
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Map<String, Object> payload){
        String email = payload.get("email").toString();
        String password = payload.get("password").toString();
        try {
            User user = findService.login(email, password);
            UserPermissions userPermissions = new UserPermissions(user, findAllService.getAllCategoriesWhereUserModerates(user.getId()));
            return new ResponseEntity<>(new UserToken(userPermissions, JwtUtil.getToken(userPermissions)), HttpStatus.OK);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(new Message("Incorrect login"), HttpStatus.BAD_REQUEST);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>(new Message("Could Not Login"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody Map<String, Object> payload){
        try {
            String email = payload.get("email").toString();
            String name = payload.get("name").toString();
            String password = payload.get("password").toString();
            String role = payload.get("role").toString();
            String moderateCategory = payload.get("moderateCategory").toString();
            User user = addService.addUser(email, name, role, password);
            if(!moderateCategory.isEmpty()) updateService.addModerator(user, moderateCategory);
            return new ResponseEntity<>(new Message("ok"), HttpStatus.OK);
        } catch (EmailAlreadyInUserException e) {
            return new ResponseEntity<>(new Message("Email in use"), HttpStatus.CONFLICT);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(new Message("Could Not Register"), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getprofile")
    public ResponseEntity<Object> getProfile(@RequestHeader("Authorization") String token){
        if(!JwtUtil.validate(token)) return new ResponseEntity<>(new Message("Profile Not Found"), HttpStatus.UNAUTHORIZED);
        try {
            User user = findService.getUser(JwtUtil.getUserFromToken(token));
            User newUser = findService.getUser(user.getId());
            UserPermissions userPermissions = new UserPermissions(newUser, findAllService.getAllCategoriesWhereUserModerates(newUser.getId()));
            return new ResponseEntity<>(new UserIat(userPermissions, JwtUtil.getIat(token)), HttpStatus.OK);
        } catch (ErrorInJWTException e) {
            return new ResponseEntity<>(new Message("Profile Not Found"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(new Message("Profile Not Found"), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/img/{id}")
    public ResponseEntity<Object> getImg(@PathVariable long id){
        try {
            Image image = findService.getImg(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(image.getImageData(), headers, HttpStatus.OK);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(new Message("Image Not Found"), HttpStatus.NOT_FOUND);
        }
    }

}
