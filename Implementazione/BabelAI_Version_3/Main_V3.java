package BabelAI_Version_3;

import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;

public class Main_V3 {
    public static boolean CONTROL = true;
    public static int CURRENT_GENERATION = 0;
    public static long startTime;
    public static long endTime;

    public static void main(String[] args) {
        //Get input value for words length
        System.out.print("Select words length between 2 and 14 (0 to terminate): ");
        Scanner scanner = new Scanner(System.in);
        String inputValueStr = scanner.nextLine();
        int inputValue = Integer.parseInt(inputValueStr);
        if (inputValue < 2 || inputValue > 14)
            exit(1);
        else
            BabelGA_V3.WORD_LENGTH = inputValue;

        //Start
        Dictionary_V3.createDictionary();
        startTime = System.currentTimeMillis();
        List<String> population = BabelGA_V3.generateFirstPopulation();

        for (; CURRENT_GENERATION < BabelGA_V3.MAX_GENERATIONS && CONTROL; CURRENT_GENERATION++) {
            //Evaluation population's fitness values
            for(int i = 0; i < BabelGA_V3.POPULATION_SIZE; i++) {
                BabelGA_V3.calculateLCSFitness(population.get(i));
                if (!CONTROL) //if a Real english word is found, stop execution
                    break;
            }

            if (CONTROL) { //Real english word not found
                //Generate next generation
                population = BabelGA_V3.generateNextGeneration(BabelGA_V3.tournamentSelection(population));

                //Print results of current generation
                System.out.print("Generation " + (CURRENT_GENERATION + 1) + ": [");
                for (int j = 0; j < BabelGA_V3.POPULATION_SIZE-1; j++) {
                    System.out.print(population.get(j) + ", ");
                }
                System.out.println(population.get(BabelGA_V3.POPULATION_SIZE-1) + "]");
            }
        }
        exit(0);
    }
}
