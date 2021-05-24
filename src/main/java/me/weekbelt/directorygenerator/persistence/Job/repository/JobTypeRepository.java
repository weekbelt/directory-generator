package me.weekbelt.directorygenerator.persistence.Job.repository;

import me.weekbelt.directorygenerator.persistence.Job.JobType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobTypeRepository extends JpaRepository<JobType, String> {

}
