package me.weekbelt.directorygenerator.persistence.job.repository;

import java.util.Optional;
import me.weekbelt.directorygenerator.persistence.job.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, String> {

    boolean existsByName(String name);

    Optional<Job> findByName(String name);
}
