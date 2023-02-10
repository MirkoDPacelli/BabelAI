package BabelAI_Version_1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Dictionary {
    public static ArrayList<String> dictionary = new ArrayList<>();
    public static int dictionarySize = 0;

    public static void createDictionary() {
        String inputFile = selectDictionary();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                dictionary.add(line);
                dictionarySize++;
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static String selectDictionary() {
        String dictionaryFile = BabelGA.WORD_LENGTH + "LettersDictionary.txt";
        return "Dataset/" + dictionaryFile;
    }
}
