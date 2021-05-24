package me.weekbelt.directorygenerator.persistence.position.repository;

import java.util.Optional;
import me.weekbelt.directorygenerator.persistence.position.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, String> {

    boolean existsByName(String name);

    Optional<Position> findByName(String positionName);
}
