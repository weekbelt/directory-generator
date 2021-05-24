package me.weekbelt.directorygenerator.persistence.job.repository;

import me.weekbelt.directorygenerator.persistence.job.JobType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobTypeRepository extends JpaRepository<JobType, String> {

}
