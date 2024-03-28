package csp_solutions;

import core_algorithms.BacktrackingSearch;
import csp_problems.*;
import csp_problems.CSPProblem.Variable;

import java.util.ArrayList;


public class BacktrackingSearch_Sudoku extends BacktrackingSearch<String,Integer>{

    public BacktrackingSearch_Sudoku(Sudoku problem){
        super(problem);
    }

    /**
     * To revise an arc: for each value in tail's domain, there must be a value in head's domain that's different
     *                   if not, delete the value from the tail's domain
     * @param head head of the arc to be revised
     * @param tail tail of the arc to be revised
     * @return true if the tail has been revised (lost some values), false otherwise
     */
    public boolean revise(String head, String tail) {
        boolean isRevised = false;
        Variable<String, Integer> headVariable = getAllVariables().get(head);
        Variable<String, Integer> tailVariable = getAllVariables().get(tail);

        for (Integer tailValue : new ArrayList<>(tailVariable.domain())) {
            boolean isFound = false;
            for (Integer headValue : headVariable.domain()) {
                // Check if tailValue is different from headValue
                if (!tailValue.equals(headValue)) {
                    isFound = true;
                    break;
                }
            }
            if (!isFound) {
                // If no satisfying value found in head domain, remove tailValue from tail's domain
                tailVariable.domain().remove(tailValue);
                isRevised = true;
            }
        }
        return isRevised;
    }

    /**
     * Implementing the minimum-remaining-values(MRV) ordering heuristic.
     * @return the variable with the smallest domain among all the unassigned variables;
     *         null if all variables have been assigned
     */
    public String selectUnassigned(){
        String selectedVariable = null;
        int minDomainSize = Integer.MAX_VALUE;
        for (String variableName : getAllVariables().keySet()) {
            if (!assigned(variableName)) {
                int domainSize = getAllVariables().get(variableName).domain().size();
                if (domainSize < minDomainSize) {
                    minDomainSize = domainSize;
                    selectedVariable = variableName;
                }
            }
        }
        return selectedVariable;
    }

    /**
     * @param args (no command-line argument is needed to run this program)
     */
    public static void main(String[] args) {
        String filename = "src/SudokuTestCases/TestCase9.txt";
        Sudoku problem = new Sudoku(filename);
        BacktrackingSearch_Sudoku agent = new BacktrackingSearch_Sudoku(problem);
        System.out.println("loading puzzle from " + filename + "...");
        problem.printPuzzle(problem.getAllVariables());
        if(agent.initAC3() && agent.search()){
            System.out.println("Solution found:");
            problem.printPuzzle(agent.getAllVariables());
        }else{
            System.out.println("Unable to find a solution.");
        }
    }
}
