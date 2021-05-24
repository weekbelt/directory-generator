package me.weekbelt.directorygenerator.persistence.job;

import java.util.ArrayList;
import java.util.UUID;
import me.weekbelt.directorygenerator.persistence.staffer.OldStafferJson;

public class JobConverter {

    public static NewJobJson convertToNewJobJsonFromJob(Job job) {
        return NewJobJson.builder()
            .id(UUID.randomUUID().toString())
            .name(job.getName().trim())
            .depth(1)
            .branchId(job.getBranchId())
            .jobType(job.getJobType().getName())
            .synonyms(new ArrayList<>())
            .build();
    }

    public static Job convertToJob(OldStafferJson stafferJson, String job) {
        return Job.builder()
            .id(UUID.randomUUID().toString())
            .name(job)
            .depth(1)
            .branchId(stafferJson.getBchID())
            .build();
    }

}
