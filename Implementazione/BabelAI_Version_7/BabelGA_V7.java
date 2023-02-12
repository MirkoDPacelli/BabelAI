package BabelAI_Version_7;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BabelGA_V7 {
    public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    public static final String VOWELS = "aeiou";
    public static final String CONSONANTS = "bcdfghjklmnpqrstvwxyz";
    public static int WORD_LENGTH = 5;
    public static final int POPULATION_SIZE = 70;
    public static final int MAX_GENERATIONS = 500;
    public static final double MUTATION_RATE = 0.2;
    public static final double CROSSOVER_RATE = 0.85;
    public static final int TOURNAMENT_SIZE = 4;
    private static final Random random = new Random();
    private static int ELITISM_FITNESS = 0;
    private static String ELITE_WORD = "";

    private static String generateValidRandomWord() {
        StringBuilder sb = new StringBuilder();
        int vowelCount = 0;
        int consonantCount = 0;
        char c;
        for (int i = 0; i < WORD_LENGTH; i++) {
            if (consonantCount == 3) {
                c = VOWELS.charAt(random.nextInt(VOWELS.length()));
                vowelCount++;
                consonantCount = 0;
                sb.append(c);
            }
            else if (vowelCount == 2){
                c = CONSONANTS.charAt(random.nextInt(CONSONANTS.length()));
                consonantCount++;
                vowelCount = 0;
                sb.append(c);
            }
            else {
                c = ALPHABET.charAt(random.nextInt(ALPHABET.length()));
                if (isVowel(c)) {
                    vowelCount++;
                    consonantCount = 0;
                }
                else {
                    consonantCount++;
                    vowelCount = 0;
                }
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static List<String> generateFirstPopulation() {
        List<String> population = new ArrayList<>();
        System.out.print("\nStarting words: [");
        for (int i = 0; i < POPULATION_SIZE; i++) {
            String generatedWord = generateValidRandomWord();
            if (i<POPULATION_SIZE-1)
                System.out.print(generatedWord + ", ");
            else
                System.out.println(generatedWord + "]");
            population.add(generatedWord);
        }
        return population;
    }

    private static String mutation(String word) {
        if (random.nextDouble() < MUTATION_RATE) {
            char[] wordArray = word.toCharArray();
            int wordLength = wordArray.length;

            int index = random.nextInt(ALPHABET.length());
            wordArray[random.nextInt(wordLength)] = ALPHABET.charAt(index);

            return new String(wordArray);
        }
        return word;
    }

    private static String crossingOver(String parent1, String parent2) {
        if (random.nextDouble() < CROSSOVER_RATE) {
            char[] parent1Array = parent1.toCharArray();
            char[] parent2Array = parent2.toCharArray();
            int crossoverPoint = random.nextInt(WORD_LENGTH);
            for (int i = crossoverPoint; i < WORD_LENGTH; i++) {
                char temp = parent1Array[i];
                parent1Array[i] = parent2Array[i];
                parent2Array[i] = temp;
            }
            return new String(parent1Array);
        }
        else if (random.nextInt(2) == 0)
            return parent1;
        else
            return parent2;
    }

    public static List<String> generateNextGeneration(List<String> population) {
        List<String> nextGeneration = new ArrayList<>();
        //Insert ELITE_WORD in population
        nextGeneration.add(ELITE_WORD);
        //Create next generation
        for (int i = 0; i < POPULATION_SIZE-1; i++) {
            String word1 = population.get(random.nextInt(POPULATION_SIZE));
            String word2 = population.get(random.nextInt(POPULATION_SIZE));
            String newWord = crossingOver(word1, word2);
            newWord = mutation(newWord);
            nextGeneration.add(newWord);
        }
        return nextGeneration;
    }

    public static int calculateFitness(String word) {
        int fitness = 0;
        for (String dictionaryWord : Dictionary_V7.dictionary) {
            int length = word.length();
            int match = 0;
            for (int i = 0; i < length; i++) {
                if (word.charAt(i) == dictionaryWord.charAt(i)) {
                    match++;
                }
            }
            if (match == length) {
                Main_V7.endTime = System.currentTimeMillis();
                System.out.println("\nFound real english word '" + word + "' at generation " + Main_V7.CURRENT_GENERATION + " in " + (Main_V7.endTime - Main_V7.startTime) + " ms");
                Main_V7.CONTROL = false;
            }
            fitness = Math.max(fitness, match);
        }
        if (ELITISM_FITNESS < fitness) {
            ELITISM_FITNESS = fitness;
            ELITE_WORD = word;
        }
        return fitness;
    }

    public static List<String> tournamentSelection(List<String> population) {
        List<String> selectionPopulation = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            List<String> tournamentParticipants = new ArrayList<>();
            for (int j = 0; j < TOURNAMENT_SIZE; j++) {
                int randomIndex = (int) (Math.random() * POPULATION_SIZE);
                tournamentParticipants.add(population.get(randomIndex));
            }
            String winner = getFittestIndividual(tournamentParticipants);
            selectionPopulation.add(winner);
        }
        return selectionPopulation;
    }

    private static String getFittestIndividual(List<String> population) {
        int maxFitness = Integer.MIN_VALUE;
        String fittest = null;
        for (String individual : population) {
            int fitness = calculateFitness(individual);
            if (fitness > maxFitness) {
                maxFitness = fitness;
                fittest = individual;
            }
        }
        return fittest;
    }

    private static boolean isVowel(char character) {
        char[] vowelsArray = VOWELS.toCharArray();
        for (char c : vowelsArray) {
            if (c == character)
                return true;
        }
        return false;
    }

}
