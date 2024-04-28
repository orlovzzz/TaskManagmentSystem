package org.example.repository;

import org.example.entity.ExecutorsTasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExecutorsTaskRepository extends JpaRepository<ExecutorsTasks, UUID> {
    @Query(value = "SELECT * FROM executors_tasks WHERE task_id = ?1", nativeQuery = true)
    List<ExecutorsTasks> findByTaskId(UUID id);
}