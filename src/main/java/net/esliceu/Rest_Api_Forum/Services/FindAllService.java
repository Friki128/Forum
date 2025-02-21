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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindAllService {
    @Autowired
    private FindService findService;
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private TopicRepo topicRepo;
    @Autowired
    private ReplyRepo replyRepo;
    @Autowired
    private UserRepo userRepo;

    public List<Topic> getAllCategoryTopics(long categoryId) throws ItemNotFoundException {
        Category category = findService.getCategory(categoryId);
        return topicRepo.findAllByCategory(category);
    }
    public List<Category> getAllCategories(){
        return categoryRepo.findAll();
    }
    public List<Reply> getAllTopicReplies(long topicId) throws ItemNotFoundException {
        Topic topic = findService.getTopic(topicId);
        return replyRepo.findAllByTopic(topic);
    }

}
