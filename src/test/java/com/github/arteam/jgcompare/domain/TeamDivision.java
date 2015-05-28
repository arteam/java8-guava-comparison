package com.github.arteam.jgcompare.domain;

import com.google.common.base.MoreObjects;

import java.util.Objects;

/**
 * Date: 5/6/15
 * Time: 9:14 PM
 *
 * @author Artem Prigoda
 */
public class TeamDivision {
    private final String name;
    private final String division;

    private TeamDivision(String name, String division) {
        this.name = name;
        this.division = division;
    }

    public static TeamDivision of(String name, String division){
        return new TeamDivision(name, division);
    }

    public String getName() {
        return name;
    }

    public String getDivision() {
        return division;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamDivision that = (TeamDivision) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(division, that.division);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, division);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("division", division)
                .toString();
    }
}
