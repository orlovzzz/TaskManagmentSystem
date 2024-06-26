package org.example.repository;

import org.example.entity.ExecutorsTasks;
import org.example.entity.ExecutorsTasksId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExecutorsTaskRepository extends JpaRepository<ExecutorsTasks, ExecutorsTasksId> {
    @Query(value = "SELECT COUNT(*) FROM executors_tasks WHERE task_id = ?1", nativeQuery = true)
    Integer countExecutors(UUID id);

    @Query(value = "SELECT * FROM executors_tasks WHERE task_id = ?1", nativeQuery = true)
    List<ExecutorsTasks> findByTaskId(UUID id);

    @Query(value = "SELECT * FROM executors_tasks WHERE executor_id = ?1", nativeQuery = true)
    List<ExecutorsTasks> findByExecutorId(UUID id);
}