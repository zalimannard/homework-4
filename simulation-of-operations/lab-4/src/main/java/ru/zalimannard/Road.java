package ru.zalimannard;

import java.util.Objects;

public record Road(String departure, String arrival) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Road road = (Road) o;
        return Objects.equals(departure, road.departure) && Objects.equals(arrival, road.arrival);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departure, arrival);
    }

    @Override
    public String toString() {
        return "Node{" +
                "departure='" + departure + '\'' +
                ", arrival='" + arrival + '\'' +
                '}';
    }
}
