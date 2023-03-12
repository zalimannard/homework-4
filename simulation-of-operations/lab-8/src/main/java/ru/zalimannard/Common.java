package ru.zalimannard;

import java.util.ArrayList;
import java.util.List;

public abstract class Common {

    public static String input(Graph graph) {
        List<String> answer = new ArrayList<>(graph.allNodeNames());

        for (int i = answer.size() - 1; i >= 0; --i) {
            if (graph.allIncoming(answer.get(i)).size() > 0) {
                answer.remove(i);
            }
        }

        if (answer.size() > 1) {
            throw new RuntimeException("Должен быть один вход");
        }
        return answer.get(0);
    }

    public static String output(Graph graph) {
        List<String> answer = new ArrayList<>(graph.allNodeNames());
        for (int i = answer.size() - 1; i >= 0; --i) {
            if (graph.allOutgoing(answer.get(i)).size() > 0) {
                answer.remove(i);
            }
        }

        if (answer.size() > 1) {
            throw new RuntimeException("Должен быть один выход");
        }
        return answer.get(0);
    }

}
