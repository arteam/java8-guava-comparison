package com.github.arteam.jgcompare;

import java.util.Objects;

/**
 * Date: 4/19/15
 * Time: 10:28 PM
 *
 * @author Artem Prigoda
 */
public class Team {

    private final long id;
    private final String name;

    public Team(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    private String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Team) {
            Team that = (Team) o;
            return Objects.equals(id, that.id) && Objects.equals(name, that.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
