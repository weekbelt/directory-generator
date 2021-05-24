package me.weekbelt.directorygenerator.persistence.Job.repository;

import me.weekbelt.directorygenerator.persistence.Job.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, String> {

    boolean existsByName(String name);
}
