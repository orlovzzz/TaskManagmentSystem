package org.example.repository;

import org.example.entity.Account;
import org.example.entity.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Tasks, Integer> {
    List<Tasks> findByAuthor(Account author);
    List<Tasks> findByExecutors(Account executor);
}
