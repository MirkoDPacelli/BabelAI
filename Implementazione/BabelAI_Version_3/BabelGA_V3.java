package BabelAI_Version_3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BabelGA_V3 {
    public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    public static final String VOWELS = "aeiou";
    public static final String CONSONANTS = "bcdfghjklmnpqrstvwxyz";
    public static int WORD_LENGTH = 5;
    public static final int POPULATION_SIZE = 10;
    public static final int MAX_GENERATIONS = 500;
    public static final double MUTATION_RATE = 0.1;
    public static final int TOURNAMENT_SIZE = 2;
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

    /*
    Metodo per calcolare, attraverso una funzione di fitness basata sulla Longest Common Subsequence, il valore massimo di fitness tra una parola e il dataset
     */
    public static int calculateLCSFitness(String word) {
        int fitness = 0;
        for (String dictionaryWord : Dictionary_V3.dictionary) {
            int wordLength = word.length();
            int lcsLength = lcsLength(word, dictionaryWord);
            if (lcsLength == wordLength) {
                Main_V3.endTime = System.currentTimeMillis();
                System.out.println("\nFound real english word '" + word + "' at generation " + Main_V3.CURRENT_GENERATION + " in " + (Main_V3.endTime - Main_V3.startTime) + " ms");
                Main_V3.CONTROL = false;
            }
            fitness = Math.max(fitness, lcsLength);
        }
        return fitness;
    }

    private static int lcsLength(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0 || j == 0) {
                    dp[i][j] = 0;
                } else if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[m][n];
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
            int fitness = calculateLCSFitness(individual);
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
