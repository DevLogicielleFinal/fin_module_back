package com.example.project.Repository;

import com.example.project.Entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByProjectId(Long projectId);

    // ou selon vos besoins, vous pouvez déclarer d'autres méthodes custom
}
