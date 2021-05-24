package me.weekbelt.directorygenerator.persistence.department;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class DepartmentTree {

    @Id
    private String id;

    @Column
    private String ancestor;

    @Column
    private String descendant;

    @Column(nullable = false)
    private int depth;

    @Column(nullable = false)
    private String branchId;

    public void changeDescendantId(String descendant) {
        this.descendant = descendant;
    }

    public void changeAncestorId(String ancestor) {
        this.ancestor = ancestor;
    }
}
