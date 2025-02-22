package net.esliceu.Rest_Api_Forum.Repositories;

import net.esliceu.Rest_Api_Forum.Entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
    Optional<Category> findBySlug(String slug);

    List<Category> findAllByModeratorsId(long id);
}
