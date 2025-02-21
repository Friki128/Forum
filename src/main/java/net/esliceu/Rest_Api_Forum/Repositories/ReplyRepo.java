package net.esliceu.Rest_Api_Forum.Repositories;

import net.esliceu.Rest_Api_Forum.Entities.Reply;
import net.esliceu.Rest_Api_Forum.Entities.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepo extends JpaRepository<Reply, Long> {
    List<Reply> findAllByTopic(Topic topic);
}
