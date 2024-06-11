import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class SoundLevelSensor {

    public static void main(String[] args) {

        String filePath = "/app/soundlevel/soundlevel.txt";
        Random random = new Random();

        try (FileWriter fileWriter = new FileWriter(filePath, true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            while (true) {
                for (int i = 0; i < 10; i++) {
                    int randomNumber = random.nextInt(61) + 40;
                    bufferedWriter.write(String.valueOf(randomNumber));
                    bufferedWriter.newLine();
                }

                bufferedWriter.flush();
                Thread.sleep(20000);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
