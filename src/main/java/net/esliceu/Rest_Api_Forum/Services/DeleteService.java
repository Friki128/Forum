package net.esliceu.Rest_Api_Forum.Services;

import net.esliceu.Rest_Api_Forum.Entities.Category;
import net.esliceu.Rest_Api_Forum.Entities.Reply;
import net.esliceu.Rest_Api_Forum.Entities.Topic;
import net.esliceu.Rest_Api_Forum.Exceptions.ItemNotFoundException;
import net.esliceu.Rest_Api_Forum.Repositories.CategoryRepo;
import net.esliceu.Rest_Api_Forum.Repositories.ReplyRepo;
import net.esliceu.Rest_Api_Forum.Repositories.TopicRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteService {
    @Autowired
    private FindService findService;
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private TopicRepo topicRepo;
    @Autowired
    private ReplyRepo replyRepo;

    public boolean deleteReply(long id){
        try {
            Reply reply = findService.getReply(id);
            replyRepo.delete(reply);
            return true;
        } catch (ItemNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean deleteTopic(long id){
        try {
            Topic topic = findService.getTopic(id);
            topicRepo.delete(topic);
            return true;
        } catch (ItemNotFoundException e) {
            return false;
        }
    }
    public boolean deleteCategory(long id){
        try {
            Category category = findService.getCategory(id);
            categoryRepo.delete(category);
            return true;
        } catch (ItemNotFoundException e) {
            return false;
        }
    }
}
