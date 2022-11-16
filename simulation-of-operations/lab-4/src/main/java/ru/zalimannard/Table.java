package ru.zalimannard;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Table {
    private final String leftTopCorner;
    private final ArrayList<String> departures = new ArrayList<>();
    private final ArrayList<String> arrivals = new ArrayList<>();
    private final Map<Road, Long> edges = new HashMap<>();

    public Table(String leftTopCorner) {
        this.leftTopCorner = leftTopCorner;
    }

    public Table(Table other) {
        leftTopCorner = other.getLeftTopCorner();

        for (String otherDeparture : other.getDepartures()) {
            for (String otherArrival : other.getArrivals()) {
                set(otherDeparture, otherArrival, other.get(otherDeparture, otherArrival));
            }
        }
        departures.clear();
        arrivals.clear();
        departures.addAll(other.getDepartures());
        arrivals.addAll(other.getArrivals());
    }

    public void inc(String departure, String arrival, Long value) {
        try {
            set(departure, arrival, get(departure, arrival) + value);
        } catch (Exception e) {
            return;
        }
        get(departure, arrival);
    }

    public void dec(String departure, String arrival, Long value) {
        inc(departure, arrival, -value);
    }

    public Long getMaxValue() {
        Long answer = Long.MIN_VALUE;

        for (String departure : departures) {
            for (String arrival : arrivals) {
                if (get(departure, arrival) == null) {
                    continue;
                }
                answer = Math.max(answer, get(departure, arrival));
            }
        }

        return answer.equals(Long.MIN_VALUE) ? null : answer;
    }

    public Long getMinInDeparture(String departureName) {
        Long min = Long.MAX_VALUE;
        for (Map.Entry<Road, Long> cell : edges.entrySet()) {
            if (cell.getKey().departure().equals(departureName)) {
                if (cell.getValue() < min) {
                    min = cell.getValue();
                }
            }
        }
        return min.equals(Long.MAX_VALUE) ? 0 : min;
    }

    public Long getMinInArrival(String arrivalName) {
        Long min = Long.MAX_VALUE;

        for (Map.Entry<Road, Long> cell : edges.entrySet()) {
            if (cell.getKey().arrival().equals(arrivalName)) {
                if (cell.getValue() < min) {
                    min = cell.getValue();
                }
            }
        }

        return min.equals(Long.MAX_VALUE) ? 0 : min;
    }

    public String getLeftTopCorner() {
        return leftTopCorner;
    }

    public ArrayList<String> getDepartures() {
        return departures;
    }

    public ArrayList<String> getArrivals() {
        return arrivals;
    }

    public void removeDeparture(String name) {
        for (String arrival : arrivals) {
            set(name, arrival, null);
        }
        departures.remove(name);
    }

    public void removeArrival(String name) {
        for (String departure : departures) {
            set(departure, name, null);
        }
        arrivals.remove(name);
    }

    public void removeAllElements() {
        for (String departure : departures) {
            for (String arrival : arrivals) {
                set(departure, arrival, null);
            }
        }
    }

    public Long get(String departure, String arrival) {
        return edges.get(new Road(departure, arrival));
    }

    public void set(String departure, String arrival, Long value) {
        if (value == null) {
            edges.remove(new Road(departure, arrival));
        } else {
            edges.put(new Road(departure, arrival), value);
            if (!departures.contains(departure)) {
                departures.add(departure);
            }
            if (!arrivals.contains(arrival)) {
                arrivals.add(arrival);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder answer = new StringBuilder();
        int maxItemLength = getMaximumElementLength();
        String spacesForEmptyElement = getSpaces(maxItemLength, 0);
        String horizontalLine = String.join("",
                Collections.nCopies((maxItemLength + 3) * (getArrivals().size() + 1) + 1, "~")) + "\n";

        answer.append(horizontalLine)
                .append("| ")
                .append(getSpaces(maxItemLength, getLeftTopCorner().length()))
                .append(getLeftTopCorner())
                .append(" |");
        for (String arrival : arrivals) {
            answer.append(" ")
                    .append(getSpaces(maxItemLength, arrival.length()))
                    .append(arrival)
                    .append(" |");
        }
        answer.append("\n");
        for (String departure : departures) {
            answer.append(horizontalLine).append("| ")
                    .append(getSpaces(maxItemLength, departure.length()))
                    .append(departure).append(" |");
            for (String arrival : arrivals) {
                answer.append(" ");
                Long value = edges.get(new Road(departure, arrival));
                if (value == null) {
                    answer.append(spacesForEmptyElement);
                } else {
                    answer.append(getSpaces(maxItemLength, value.toString().length()))
                            .append(value);
                }
                answer.append(" |");
            }
            answer.append("\n");
        }
        answer.append(horizontalLine);


        return answer.toString();
    }

    private String getSpaces(int maxItemLength, int elementLength) {
        int copies = Math.max(0, maxItemLength - elementLength);
        return String.join("", Collections.nCopies(copies, " "));
    }

    private int getMaximumElementLength() {
        int maxLength = getLeftTopCorner() == null ? 0 : getLeftTopCorner().length();

        for (Map.Entry<Road, Long> cell : edges.entrySet()) {
            maxLength = Math.max(maxLength, cell.getKey().departure().length());
            maxLength = Math.max(maxLength, cell.getKey().arrival().length());
            maxLength = Math.max(maxLength, cell.getValue().toString().length());
        }

        return maxLength;
    }
}
