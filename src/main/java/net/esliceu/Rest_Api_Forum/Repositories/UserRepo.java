package net.esliceu.Rest_Api_Forum.Repositories;

import net.esliceu.Rest_Api_Forum.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndPassword(String email, String hash);

    Optional<User> findByEmail(String email);

    List<User> findAllByRoleEquals(String admin);
}
