package net.esliceu.Rest_Api_Forum.Services;

import net.esliceu.Rest_Api_Forum.Entities.*;
import net.esliceu.Rest_Api_Forum.Exceptions.ItemNotFoundException;
import net.esliceu.Rest_Api_Forum.Repositories.*;
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
    @Autowired
    private ImageRepo imageRepo;

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
    public User login(String email, String password) throws ItemNotFoundException {
        Optional<User> user = userRepo.findByEmailAndPassword(email, HashUtil.hash(password));
        if(user.isEmpty()) throw new ItemNotFoundException();
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

    public Image getImg(long id) throws ItemNotFoundException {
        Optional<Image> image = imageRepo.findById(id);
        if(image.isEmpty()) throw new ItemNotFoundException();
        return image.get();
    }
    public Image getImgByUser(long userId) throws ItemNotFoundException {
        Optional<Image> image = imageRepo.findByUserId(userId);
        return image.orElse(null);
    }
}
