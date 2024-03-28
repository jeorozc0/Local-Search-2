
package optimization_solutions;

import core_algorithms.GeneticAlgorithm;
import core_algorithms.Individual;
import optimization_problems.TSP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm_TSP extends GeneticAlgorithm<Integer> {
    private final TSP problem;
    private final Random random;

    public GeneticAlgorithm_TSP(int maxGen, double mRate, double elitism, TSP problem) {
        super(maxGen, mRate, elitism);
        this.problem = problem;
        this.random = new Random();
    }

    public double calcFitnessScore(List<Integer> chromosome) {
        return 1 / problem.cost(chromosome);
    }

    public Individual<Integer> reproduce(Individual<Integer> p1, Individual<Integer> p2) {
        List<Integer> offspring = new ArrayList<>(Collections.nCopies(p1.getChromosome().size(), -1));
        int start = random.nextInt(p1.getChromosome().size());
        int end = start + random.nextInt(p1.getChromosome().size() - start);

        for (int i = start; i <= end; i++) {
            offspring.set(i, p1.getChromosome().get(i));
        }

        int currentPos = (end + 1) % offspring.size();
        for (Integer city : p2.getChromosome()) {
            if (!offspring.contains(city)) {
                offspring.set(currentPos, city);
                currentPos = (currentPos + 1) % offspring.size();
            }
        }

        return new Individual<>(offspring, calcFitnessScore(offspring));
    }

    public Individual<Integer> mutate(Individual<Integer> individual) {
        List<Integer> chromosome = new ArrayList<>(individual.getChromosome());
        int i1 = random.nextInt(chromosome.size());
        int i2 = random.nextInt(chromosome.size());
        while (i1 == i2) {
            i2 = random.nextInt(chromosome.size());
        }
        Collections.swap(chromosome, i1, i2);
        return new Individual<>(chromosome, calcFitnessScore(chromosome));
    }

    public List<Individual<Integer>> generateInitPopulation(int popSize, int numCities) {
        List<Individual<Integer>> population = new ArrayList<>(popSize);
        for (int i = 0; i < popSize; i++) {
            List<Integer> chromosome = new ArrayList<>(numCities);
            for (int j = 0; j < numCities; j++) {
                chromosome.add(j);
            }
            Collections.shuffle(chromosome);
            Individual<Integer> individual = new Individual<>(chromosome, calcFitnessScore(chromosome));
            population.add(individual);
        }
        return population;
    }

    public static void main(String[] args) {
        int MAX_GEN = 200;
        double MUTATION_RATE = 0.05;
        int POPULATION_SIZE = 1000;
        int NUM_CITIES = 6; // choose from 5, 6, 17, 26
        double ELITISM = 0.2;

        TSP problem = new TSP(NUM_CITIES);

        GeneticAlgorithm_TSP agent = new GeneticAlgorithm_TSP(MAX_GEN, MUTATION_RATE, ELITISM, problem);

        Individual<Integer> best = agent.evolve(agent.generateInitPopulation(POPULATION_SIZE, NUM_CITIES));

        System.out.println("This is the best " + best);
        System.out.println("This is the cost " + problem.cost(best.getChromosome()));
    }

}
