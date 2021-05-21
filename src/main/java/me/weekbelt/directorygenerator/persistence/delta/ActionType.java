package me.weekbelt.directorygenerator.persistence.delta;

import lombok.Getter;

@Getter
public enum ActionType {
    CREATE,
    UPDATE,
    DELETE,
    UNKNOWN
}
