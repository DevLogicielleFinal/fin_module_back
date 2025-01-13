package com.example.project.Repository;

import com.example.project.Entity.Project;
import com.example.project.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByMembersContains(User user);
}
