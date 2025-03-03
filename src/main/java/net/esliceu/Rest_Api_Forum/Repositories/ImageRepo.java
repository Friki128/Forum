package net.esliceu.Rest_Api_Forum.Repositories;

import net.esliceu.Rest_Api_Forum.Entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepo extends JpaRepository<Image, Long> {
    Optional<Image> findByUserId(long userId);
}
