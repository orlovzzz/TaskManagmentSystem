package org.example.repository;

import org.example.entity.Com;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentsRepository extends JpaRepository<Com, Integer> {
}
