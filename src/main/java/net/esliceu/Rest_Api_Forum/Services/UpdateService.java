package net.esliceu.Rest_Api_Forum.Services;

import net.esliceu.Rest_Api_Forum.Entities.Category;
import net.esliceu.Rest_Api_Forum.Entities.Reply;
import net.esliceu.Rest_Api_Forum.Entities.Topic;
import net.esliceu.Rest_Api_Forum.Entities.User;
import net.esliceu.Rest_Api_Forum.Exceptions.EmailAlreadyInUserException;
import net.esliceu.Rest_Api_Forum.Exceptions.ItemNotFoundException;
import net.esliceu.Rest_Api_Forum.Repositories.CategoryRepo;
import net.esliceu.Rest_Api_Forum.Repositories.ReplyRepo;
import net.esliceu.Rest_Api_Forum.Repositories.TopicRepo;
import net.esliceu.Rest_Api_Forum.Repositories.UserRepo;
import net.esliceu.Rest_Api_Forum.Utils.HashUtil;
import net.esliceu.Rest_Api_Forum.Utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateService {
    @Autowired
    private FindService findService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private TopicRepo topicRepo;
    @Autowired
    private ReplyRepo replyRepo;

    public Reply updateReply(long id, String content) throws ItemNotFoundException {
        Reply reply = findService.getReply(id);
        reply.setContent(content);
        reply.setUpdatedAt(TimeUtil.getTime());
        return replyRepo.save(reply);
    }
    public Category updateCategory(String categorySlug, String Description, String title) throws ItemNotFoundException {
        Category category = findService.getCategoryBySlug(categorySlug);
        category.setTitle(title);
        category.setDescription(Description);
        return categoryRepo.save(category);
    }
    public Topic updateTopic(long id, String categorySlug, String content, String title) throws ItemNotFoundException {
        Topic topic = findService.getTopic(id);
        Category category = findService.getCategoryBySlug(categorySlug);
        topic.setContent(content);
        topic.setTitle(title);
        topic.setCategory(category);
        topic.setUpdatedAt(TimeUtil.getTime());
        return  topicRepo.save(topic);
    }
    public Boolean updatePassword(String email, String password, String newPassword) {
        User user = findService.login(email, password);
        if(user == null) return false;
        user.setPassword(HashUtil.hash(newPassword));
        userRepo.save(user);
        return true;
    }
    public User updateUser(long id, String avatar, String email, String name) throws ItemNotFoundException, EmailAlreadyInUserException {
        User user = findService.getUser(id);
        if(!user.getEmail().equals(email)){
            if(findService.checkEmailAvailable(email)) throw new EmailAlreadyInUserException();
            user.setEmail(email);
        }
        user.setName(name);
        user.setImageUrl(avatar);
       return userRepo.save(user);
    }
    public void addModerator(User user, String categorySlug) throws ItemNotFoundException {
        Category category = findService.getCategoryBySlug(categorySlug);
        category.getModerators().add(user);
        categoryRepo.save(category);
    }
}
