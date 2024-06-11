import java.io.*;

public class SoundLevelMonitor {
    public static void main(String[] args) {

        String filepath1 = "/soundlevel/soundlevel.txt";
        String filepath2 = "/app/noisepollution/noisepollution.txt";

        try {
            String lowSoundLevel = System.getenv("LOW_SOUNDLEVEL");
            String mediumSoundLevel = System.getenv("MEDIUM_SOUNDLEVEL");
            String highSoundLevel = System.getenv("HIGH_SOUNDLEVEL");

            if (lowSoundLevel == null || mediumSoundLevel == null || highSoundLevel == null) {
                System.err.println("Environment variables not set properly.");
                System.exit(1);
            }

            double lowThreshold = Double.parseDouble(lowSoundLevel);
            double mediumThreshold = Double.parseDouble(mediumSoundLevel);
            double highThreshold = Double.parseDouble(highSoundLevel);

            FileReader fileReader = new FileReader(filepath1);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            FileWriter fileWriter = new FileWriter(filepath2, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            while (true) {
                int sum = 0;
                int count = 0;
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    int number = Integer.parseInt(line.trim());
                    sum += number;
                    count++;
                }

                double average = (sum * 1.0) / count;

                if (average >= lowThreshold && average < mediumThreshold) {
                    bufferedWriter.write(String.valueOf(average) + " - " + "Low\n");
                } else if (average >= mediumThreshold && average < highThreshold) {
                    bufferedWriter.write(String.valueOf(average) + " - " + "Medium\n");
                } else {
                    bufferedWriter.write(String.valueOf(average) + " - " + "High\n");
                }

                bufferedWriter.flush();
                Thread.sleep(30000);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
