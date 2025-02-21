package net.esliceu.Rest_Api_Forum.Services;

import net.esliceu.Rest_Api_Forum.Entities.Category;
import net.esliceu.Rest_Api_Forum.Entities.Reply;
import net.esliceu.Rest_Api_Forum.Entities.Topic;
import net.esliceu.Rest_Api_Forum.Entities.User;
import net.esliceu.Rest_Api_Forum.Exceptions.ItemNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {
    @Autowired
    FindService findService;
    public boolean validateCategory(int userId, int categoryId) throws ItemNotFoundException {
        User user = findService.getUser(userId);
        Category category = findService.getCategory(categoryId);
        if(user.getRole().equals("admin")) return true;
        else return category.getModerators().contains(user);
    }
    public boolean validateReply(int userId, int replyId) throws ItemNotFoundException {
        User user = findService.getUser(userId);
        Reply reply = findService.getReply(replyId);
        if(user.getRole().equals("admin"))return true;
        if(reply.getTopic().getCategory().getModerators().contains(user))return true;
        else return reply.getUser().equals(user);
    }
    public boolean validateTopic(int userId, int topicId) throws ItemNotFoundException {
        User user = findService.getUser(userId);
        Topic topic = findService.getTopic(topicId);
        if(user.getRole().equals("admin"))return true;
        else if(topic.getCategory().getModerators().contains(user))return true;
        else return topic.getUser().equals(user);
    }
}
