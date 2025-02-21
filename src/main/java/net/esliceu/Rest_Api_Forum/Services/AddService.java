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

import java.util.ArrayList;
import java.util.List;

@Service
public class AddService {
    @Autowired
    private FindService findService;
    @Autowired
    private FindAllService findAllService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private TopicRepo topicRepo;
    @Autowired
    private ReplyRepo replyRepo;
    @Autowired
    private CategoryRepo categoryRepo;

    public Category addCategory(String description, String title) throws ItemNotFoundException {
        Category category = new Category(title, description, title.replace(" ", "_"), "hsl("+ Math.round(Math.random()*360) + ",50%, 50%)", new ArrayList<>());
        return categoryRepo.save(category);
    }
    public Reply addReply(long userId, long topicId, String content) throws ItemNotFoundException {
        User user = findService.getUser(userId);
        Topic topic = findService.getTopic(topicId);
        Reply reply = new Reply(content, topic, user, TimeUtil.getTime(), TimeUtil.getTime());
        return replyRepo.save(reply);
    }
    public User addUser(String email, String name, String role, String password) throws EmailAlreadyInUserException {
        User user = new User(role, name, email, "", HashUtil.hash(password));
        if(findService.checkEmailAvailable(email)) throw new EmailAlreadyInUserException();
        return userRepo.save(user);
    }

    public Topic addTopic(long userId, String categorySlug, String content, String title) throws ItemNotFoundException {
        User user = findService.getUser(userId);
        Category category = findService.getCategoryBySlug(categorySlug);
        Topic topic = new Topic(0, title, content, category, user, TimeUtil.getTime(), TimeUtil.getTime(), new ArrayList<>(), 0);
        return topicRepo.save(topic);
    }
}
