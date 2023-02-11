package BabelAI_Version_4;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BabelGA_V4 {
    public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    public static final String VOWELS = "aeiou";
    public static final String CONSONANTS = "bcdfghjklmnpqrstvwxyz";
    public static int WORD_LENGTH = 5;
    public static final int POPULATION_SIZE = 70;
    public static final int MAX_GENERATIONS = 500;
    public static final double MUTATION_RATE = 0.05;
    public static final int TOURNAMENT_SIZE = 4;
    private static final Random random = new Random();

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
        char[] wordArray = word.toCharArray();
        for (int i = 0; i < wordArray.length; i++) {
            if (random.nextDouble() < MUTATION_RATE) {
                int index = random.nextInt(ALPHABET.length());
                wordArray[i] = ALPHABET.charAt(index);
            }
        }
        return new String(wordArray);
    }

    private static String crossingOver(String parent1, String parent2) {
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

    public static List<String> generateNextGeneration(List<String> population) {
        List<String> nextGeneration = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
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
        for (String dictionaryWord : Dictionary_V4.dictionary) {
            int length = word.length();
            int match = 0;
            for (int i = 0; i < length; i++) {
                if (word.charAt(i) == dictionaryWord.charAt(i)) {
                    match++;
                }
            }
            if (match == length) {
                Main_V4.endTime = System.currentTimeMillis();
                System.out.println("\nFound real english word '" + word + "' at generation " + Main_V4.CURRENT_GENERATION + " in " + (Main_V4.endTime - Main_V4.startTime) + " ms");
                Main_V4.CONTROL = false;
            }
            fitness = Math.max(fitness, match);
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
