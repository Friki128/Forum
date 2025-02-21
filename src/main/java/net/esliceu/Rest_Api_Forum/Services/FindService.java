package net.esliceu.Rest_Api_Forum.Services;

import net.esliceu.Rest_Api_Forum.Entities.Category;
import net.esliceu.Rest_Api_Forum.Entities.Reply;
import net.esliceu.Rest_Api_Forum.Entities.Topic;
import net.esliceu.Rest_Api_Forum.Entities.User;
import net.esliceu.Rest_Api_Forum.Exceptions.ItemNotFoundException;
import net.esliceu.Rest_Api_Forum.Repositories.CategoryRepo;
import net.esliceu.Rest_Api_Forum.Repositories.ReplyRepo;
import net.esliceu.Rest_Api_Forum.Repositories.TopicRepo;
import net.esliceu.Rest_Api_Forum.Repositories.UserRepo;
import net.esliceu.Rest_Api_Forum.Utils.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FindService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private TopicRepo topicRepo;
    @Autowired
    private ReplyRepo replyRepo;

    public User getUser(long id) throws ItemNotFoundException {
        Optional<User> user = userRepo.findById(id);
        if(user.isEmpty()) throw new ItemNotFoundException();
        return user.get();
    }
    public Category getCategory(long id) throws ItemNotFoundException {
        Optional<Category> category = categoryRepo.findById(id);
        if(category.isEmpty()) throw new ItemNotFoundException();
        return category.get();
    }

    public Reply getReply(long id) throws ItemNotFoundException {
        Optional<Reply> reply = replyRepo.findById(id);
        if(reply.isEmpty()) throw new ItemNotFoundException();
        return reply.get();
    }

    public Topic getTopic(long id) throws ItemNotFoundException {
        Optional<Topic> topic = topicRepo.findById(id);
        if(topic.isEmpty()) throw new ItemNotFoundException();
        return topic.get();
    }
    public User login(String email, String password){
        Optional<User> user = userRepo.findByEmailAndPassword(email, HashUtil.hash(password));
        return user.get();
    }
    public Category getCategoryBySlug(String slug) throws ItemNotFoundException {
        Optional<Category> category = categoryRepo.findBySlug(slug);
        if(category.isEmpty()) throw new ItemNotFoundException();
        return category.get();
    }
    public Boolean checkEmailAvailable(String email){
        Optional<User> user = userRepo.findByEmail(email);
        return user.isEmpty();
    }
}
