package com.github.arteam.jgcompare.domain;

/**
 * Date: 5/6/15
 * Time: 9:14 PM
 *
 * @author Artem Prigoda
 */
public record TeamDivision(String name, String division) {
    public static TeamDivision of(String name, String division) {
        return new TeamDivision(name, division);
    }
}
