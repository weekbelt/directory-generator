package me.weekbelt.directorygenerator.persistence.delta;

import lombok.Getter;

@Getter
public enum EntityType {
    DEPARTMENT,
    DEPARTMENT_TREE,
    STAFFER,
    STAFFER_JOB,
    STAFFER_POSITION,
    JOB,
    POSITION,
    SHORTCUT;

    public static boolean contains(String name) {
        return DEPARTMENT.name().equalsIgnoreCase(name) | STAFFER.name().equalsIgnoreCase(name) | JOB.name().equalsIgnoreCase(name) |
            POSITION.name().equalsIgnoreCase(name) | SHORTCUT.name().equalsIgnoreCase(name);
    }
}
