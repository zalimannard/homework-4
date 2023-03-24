import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Random random = new Random();
        StringBuilder answer = new StringBuilder();
        for (int i = 0; i < 524288; ++i) {
            answer.append(random.nextLong(0, Integer.MAX_VALUE)).append(" ");
        }

        String text = answer.toString();
        try (FileOutputStream fos = new FileOutputStream("input.txt")) {
            byte[] buffer = text.getBytes();
            fos.write(buffer, 0, buffer.length);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}