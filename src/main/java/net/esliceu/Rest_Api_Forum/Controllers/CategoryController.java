package net.esliceu.Rest_Api_Forum.Controllers;

import net.esliceu.Rest_Api_Forum.Entities.Category;
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
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    UpdateService updateService;
    @Autowired
    AddService addService;
    @Autowired
    DeleteService deleteService;
    @Autowired
    FindAllService findAllService;
    @Autowired
    PermissionService permissionService;
    @Autowired
    FindService findService;

    @GetMapping("/{slug}")
    public ResponseEntity<Category> getCategory(@RequestHeader("Authorization") String token, @PathVariable String slug){
        if(!JwtUtil.validate(token)) return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        try {

            return new ResponseEntity<>(findService.getCategoryBySlug(slug), HttpStatus.OK);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<Category>> getCategories(@RequestHeader("Authorization") String token){
        if(!JwtUtil.validate(token)) return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(findAllService.getAllCategories(), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Category> addCategory(@RequestHeader("Authorization") String token, @RequestParam String title, @RequestParam String description){
        if(!JwtUtil.validate(token)) return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        try {
            User user = JwtUtil.getUserFromToken(token);
            if(!permissionService.checkAdmin(user.getId())) return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
            return new ResponseEntity<>(addService.addCategory(description, title), HttpStatus.CREATED);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ErrorInJWTException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{slug}")
    public ResponseEntity<Category> updateCategory(@RequestHeader("Authorization") String token, @PathVariable String slug, @RequestParam String title, @RequestParam String description){
        if(!JwtUtil.validate(token)) return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        try {
            User user = JwtUtil.getUserFromToken(token);
            Category category = findService.getCategoryBySlug(slug);
            if(!permissionService.validateCategory(user.getId(), category.getId())) return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
            return new ResponseEntity<>(updateService.updateCategory(slug, description, title), HttpStatus.OK);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ErrorInJWTException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{slug}")
    public ResponseEntity<Boolean> deleteCategory(@RequestHeader("Authorization") String token, @PathVariable String slug){
        if(!JwtUtil.validate(token)) return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
        try {
            User user = JwtUtil.getUserFromToken(token);
            Category category = findService.getCategoryBySlug(slug);
            if(!permissionService.validateCategory(user.getId(), category.getId())) return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
            return new ResponseEntity<>(deleteService.deleteCategory(category.getId()), HttpStatus.OK);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ErrorInJWTException e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/{slug}/topics")
    public ResponseEntity<List<Topic>> getTopics(@RequestHeader("Authorization") String token, @PathVariable String slug){
        if(!JwtUtil.validate(token)) return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        try {
            Category category = findService.getCategoryBySlug(slug);
            return new ResponseEntity<>(findAllService.getAllCategoryTopics(category.getId()), HttpStatus.OK);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
