package net.esliceu.Rest_Api_Forum.Controllers;

import net.esliceu.Rest_Api_Forum.Entities.Category;
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

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Object> getCategory(@PathVariable String slug){
        try {

            return new ResponseEntity<>(findService.getCategoryBySlug(slug), HttpStatus.OK);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(new Message("Not Found"), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<Category>> getCategories(){
        return new ResponseEntity<>(findAllService.getAllCategories(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> addCategory(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> payload){
        if(!JwtUtil.validate(token)) return new ResponseEntity<>(new Message("Could Not Add Category"), HttpStatus.UNAUTHORIZED);
        try {
            String title = payload.get("title").toString();
            String description = payload.get("description").toString();
            User user = findService.getUser(JwtUtil.getUserFromToken(token));
            if(!permissionService.checkAdmin(user.getId())) return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
            return new ResponseEntity<>(addService.addCategory(description, title), HttpStatus.CREATED);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(new Message("Could Not Add Category"), HttpStatus.NOT_FOUND);
        } catch (ErrorInJWTException e) {
            return new ResponseEntity<>(new Message("Could Not Add Category"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{slug}")
    public ResponseEntity<Object> updateCategory(@RequestHeader("Authorization") String token, @PathVariable String slug, @RequestBody Map<String, Object> payload){
        if(!JwtUtil.validate(token)) return new ResponseEntity<>(new Message("Could Not Update Category"), HttpStatus.UNAUTHORIZED);
        try {
            String description = payload.get("description").toString();
            String title = payload.get("title").toString();
            User user = findService.getUser(JwtUtil.getUserFromToken(token));
            Category category = findService.getCategoryBySlug(slug);
            if(!permissionService.validateCategory(user.getId(), category.getId())) return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
            return new ResponseEntity<>(updateService.updateCategory(slug, description, title), HttpStatus.OK);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(new Message("Could Not Update Category"), HttpStatus.NOT_FOUND);
        } catch (ErrorInJWTException e) {
            return new ResponseEntity<>(new Message("Could Not Update Category"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{slug}")
    public ResponseEntity<Object> deleteCategory(@RequestHeader("Authorization") String token, @PathVariable String slug){
        if(!JwtUtil.validate(token)) return new ResponseEntity<>(new Message("Could Not Delete Category"), HttpStatus.UNAUTHORIZED);
        try {
            User user = findService.getUser(JwtUtil.getUserFromToken(token));
            Category category = findService.getCategoryBySlug(slug);
            if(!permissionService.validateCategory(user.getId(), category.getId())) return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
            return new ResponseEntity<>(deleteService.deleteCategory(category.getId()), HttpStatus.OK);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(new Message("Could Not Delete Category"), HttpStatus.NOT_FOUND);
        } catch (ErrorInJWTException e) {
            return new ResponseEntity<>(new Message("Could Not Delete Category"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/{slug}/topics")
    public ResponseEntity<Object> getTopics(@PathVariable String slug){
        try {
            Category category = findService.getCategoryBySlug(slug);
            return new ResponseEntity<>(findAllService.getAllCategoryTopics(category.getId()), HttpStatus.OK);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(new Message("Not Found"), HttpStatus.NOT_FOUND);
        }

    }
}
