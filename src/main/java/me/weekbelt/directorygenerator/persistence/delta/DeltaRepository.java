package me.weekbelt.directorygenerator.persistence.delta;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeltaRepository extends JpaRepository<Delta, String>, DeltaRepositoryCustom {

    List<Delta> findByIsReport(boolean isReport);

}
