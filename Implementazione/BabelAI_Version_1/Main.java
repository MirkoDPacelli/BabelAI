package BabelAI_Version_1;

import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;

public class Main {
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
            BabelGA.WORD_LENGTH = inputValue;

        //Start
        Dictionary.createDictionary();
        startTime = System.currentTimeMillis();
        List<String> population = BabelGA.generateFirstPopulation();

        for (; CURRENT_GENERATION < BabelGA.MAX_GENERATIONS && CONTROL; CURRENT_GENERATION++) {
            //Evaluation population's fitness values
            for(int i = 0; i < BabelGA.POPULATION_SIZE; i++) {
                BabelGA.calculateFitness(population.get(i));
                if (!CONTROL) //if a Real english word is found, stop execution
                    break;
            }

            if (CONTROL) { //Real english word not found
                //Generate next generation
                population = BabelGA.generateNextGeneration(BabelGA.tournamentSelection(population));

                //Print results of current generation
                System.out.print("Generation " + (CURRENT_GENERATION + 1) + ": [");
                for (int j = 0; j < BabelGA.POPULATION_SIZE-1; j++) {
                    System.out.print(population.get(j) + ", ");
                }
                System.out.println(population.get(BabelGA.POPULATION_SIZE-1) + "]");
            }
        }
        exit(0);
    }
}
