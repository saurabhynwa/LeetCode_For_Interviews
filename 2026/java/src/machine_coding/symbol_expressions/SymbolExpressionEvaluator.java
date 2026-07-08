package src.machine_coding.symbol_expressions;

import java.util.*;

public class SymbolExpressionEvaluator {

    // Tracking states for our topological graph traversal and cycle interceptor
    private enum NodeState {UNVISITED, VISITING, EVALUATED}

    private final Map<String, String> expressionsRegistry;
    private final Map<String, Double> evaluationCache;
    private final Map<String, NodeState> visitedTracker;

    public SymbolExpressionEvaluator(Map<String, String> rawInputMap) {
        this.expressionsRegistry = rawInputMap != null ? rawInputMap : new HashMap<>();
        this.evaluationCache = new HashMap<>();
        this.visitedTracker = new HashMap<>();

        // Initialize all input keys to unvisited baseline nodes
        for (String symbol : expressionsRegistry.keySet()) {
            visitedTracker.put(symbol, NodeState.UNVISITED);
        }
    }

    /**
     * Core Entry Point: Evaluates every single symbol inside the registry.
     *
     * @return Map linking every symbol string to its final calculated double valuation.
     */
    public Map<String, Double> evaluateAllSymbols() {
        Map<String, Double> finalResultMap = new HashMap<>();

        for (String symbol : expressionsRegistry.keySet()) {
            double calculatedValue = resolveSymbolValueDFS(symbol);
            finalResultMap.put(symbol, calculatedValue);
        }

        return finalResultMap;
    }

    private double resolveSymbolValueDFS(String activeSymbol) {
        // Validation Guardrail: Unknown Symbol Key Intercept
        if (!expressionsRegistry.containsKey(activeSymbol)) {
            throw new ValueErrorException("Unknown symbol: " + activeSymbol);
        }

        NodeState currentState = visitedTracker.getOrDefault(activeSymbol, NodeState.UNVISITED);

        // 🛡️ MEMOIZATION CHECK: If we already locked down the answer, return it instantly
        if (currentState == NodeState.EVALUATED) {
            return evaluationCache.get(activeSymbol);
        }

        // 🛡️ CYCLE PROTECTION GUARDRAIL: If we hit a node that is currently in-progress, a loop is live!
        if (currentState == NodeState.VISITING) {
            throw new ValueErrorException("Circular dependency detected at: " + activeSymbol);
        }

        // Lock the node state as actively processing
        visitedTracker.put(activeSymbol, NodeState.VISITING);

        String rawExpression = expressionsRegistry.get(activeSymbol);
        List<String> tokensList = tokenizeExpression(rawExpression);

        // Parse through the expression tokens to recursively resolve parent dependencies first
        for (int i = 0; i < tokensList.size(); i++) {
            String token = tokensList.get(i);

            // If the token is a word variable character layout (and not a number or operator), it's a symbol!
            if (isSymbolName(token)) {
                double resolvedParentValue = resolveSymbolValueDFS(token);
                // Substitute the variable string with its actual numeric value string payload
                tokensList.set(i, String.valueOf(resolvedParentValue));
            }
        }

        // Now that all sub-dependencies are completely resolved, calculate the math expression
        double finalEvaluatedValue = computeMathExpression(tokensList);

        // Save the result to cache and mark the node state complete
        evaluationCache.put(activeSymbol, finalEvaluatedValue);
        visitedTracker.put(activeSymbol, NodeState.EVALUATED);

        return finalEvaluatedValue;
    }

    private List<String> tokenizeExpression(String inputExpression) {
        List<String> tokens = new ArrayList<>();
        int length = inputExpression.length();
        int index = 0;

        while (index < length) {
            char activeChar = inputExpression.charAt(index);

            if (Character.isWhitespace(activeChar)) {
                index++;
                continue;
            }

            // Group operators and parentheses individually
            if (activeChar == '+' || activeChar == '-' || activeChar == '*' || activeChar == '/' || activeChar == '(' ||
                    activeChar == ')') {
                tokens.add(String.valueOf(activeChar));
                index++;
                continue;
            }

            // Extract numeric literals or symbol string tokens sequentially
            StringBuilder contentAccumulator = new StringBuilder();
            while (index < length && !Character.isWhitespace(inputExpression.charAt(index)) &&
                    "+-*/()".indexOf(inputExpression.charAt(index)) == -1) {
                contentAccumulator.append(inputExpression.charAt(index));
                index++;
            }
            tokens.add(contentAccumulator.toString());
        }

        return tokens;
    }

    private double computeMathExpression(List<String> tokens) {
        Stack<Double> valuesCacheStack = new Stack<>();
        Stack<Character> operatorsCacheStack = new Stack<>();

        for (String token : tokens) {
            if (token.equals("(")) {
                operatorsCacheStack.push('(');
            } else if (token.equals(")")) {
                while (!operatorsCacheStack.isEmpty() && operatorsCacheStack.peek() != '(') {
                    executeTopOperationStack(valuesCacheStack, operatorsCacheStack);
                }
                operatorsCacheStack.pop(); // Remove '(' boundary
            } else if (token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/")) {
                char currentOp = token.charAt(0);
                while (!operatorsCacheStack.isEmpty() && getOperatorPrecedence(operatorsCacheStack.peek()) >= getOperatorPrecedence(currentOp)) {
                    executeTopOperationStack(valuesCacheStack, operatorsCacheStack);
                }
                operatorsCacheStack.push(currentOp);
            } else {
                // Must be a numeric string constant at this point
                valuesCacheStack.push(Double.parseDouble(token));
            }
        }

        while (!operatorsCacheStack.isEmpty()) {
            executeTopOperationStack(valuesCacheStack, operatorsCacheStack);
        }

        return valuesCacheStack.isEmpty() ? 0.0 : valuesCacheStack.pop();
    }

    private void executeTopOperationStack(Stack<Double> values, Stack<Character> operators) {
        if (values.size() < 2 || operators.isEmpty()) return;
        double rightOperand = values.pop();
        double leftOperand = values.pop();
        char activeOp = operators.pop();

        switch (activeOp) {
            case '+':
                values.push(leftOperand + rightOperand);
                break;
            case '-':
                values.push(leftOperand - rightOperand);
                break;
            case '*':
                values.push(leftOperand * rightOperand);
                break;
            case '/':
                values.push(leftOperand / rightOperand);
                break; // Standard floating-point split
        }
    }

    private int getOperatorPrecedence(char operator) {
        if (operator == '*' || operator == '/') return 2;
        if (operator == '+' || operator == '-') return 1;
        return 0; // Boundary level for brackets
    }

    private boolean isSymbolName(String token) {
        // If a string token starts with an alphabetical letter character layout, it's a variable symbol name
        return Character.isLetter(token.charAt(0));
    }

    // Custom runtime unchecked validation rule framework matched to prompt expectations
    public static class ValueErrorException extends RuntimeException {
        public ValueErrorException(String alertMessage) {
            super(alertMessage);
        }
    }

    /**
     * Helper Tool: Prints the floating-point results ledger cleanly.
     */
    private static void printResultsMap(Map<String, Double> results) {
        System.out.print("{ ");
        int counter = 0;
        for (Map.Entry<String, Double> entry : results.entrySet()) {
            double rawVal = entry.getValue();

            // Formatting trick: If the number is a whole integer (like 20.0),
            // display it as an integer (20) to match the prompt's aesthetic layout rules.
            if (rawVal == (long) rawVal) {
                System.out.print(entry.getKey() + ": " + (long) rawVal);
            } else {
                System.out.print(entry.getKey() + ": " + rawVal);
            }

            if (counter < results.size() - 1) {
                System.out.print(", ");
            }
            counter++;
        }
        System.out.println(" }");
    }

    public static void main(String[] args) {
        // --------------------------------------------------
        // TEST CASE 1: Simple Dependency Chain Loop
        // --------------------------------------------------
        System.out.println("--- [TEST 1] Executing Example 1 ---");
        Map<String, String> input1 = new HashMap<>();
        input1.put("a", "3");
        input1.put("b", "a + 2");
        input1.put("c", "b * 4");
        input1.put("d", "c - a");

        SymbolExpressionEvaluator evaluator1 = new SymbolExpressionEvaluator(input1);
        try {
            Map<String, Double> results1 = evaluator1.evaluateAllSymbols();
            printResultsMap(results1);
        } catch (SymbolExpressionEvaluator.ValueErrorException e) {
            System.out.println("EXCEPTION TRAPPED: " + e.getMessage());
        }

        // --------------------------------------------------
        // TEST CASE 2: Parentheses & Floating Point Division
        // --------------------------------------------------
        System.out.println("\n--- [TEST 2] Executing Example 2 ---");
        Map<String, String> input2 = new HashMap<>();
        input2.put("x", "8");
        input2.put("y", "(x + 4) / 3");
        input2.put("z", "y * 2");

        SymbolExpressionEvaluator evaluator2 = new SymbolExpressionEvaluator(input2);
        try {
            Map<String, Double> results2 = evaluator2.evaluateAllSymbols();
            printResultsMap(results2);
        } catch (SymbolExpressionEvaluator.ValueErrorException e) {
            System.out.println("🚨 EXCEPTION TRAPPED: " + e.getMessage());
        }

        // --------------------------------------------------
        // TEST CASE 3: Guardrail Check - Unknown Symbol Key
        // --------------------------------------------------
        System.out.println("\n--- [TEST 3] Verifying Unknown Symbol Intercept ---");
        Map<String, String> input3 = new HashMap<>();
        input3.put("a", "10");
        input3.put("b", "a + unknown_node"); // 'unknown_node' is not defined in the map!

        SymbolExpressionEvaluator evaluator3 = new SymbolExpressionEvaluator(input3);
        try {
            evaluator3.evaluateAllSymbols();
        } catch (SymbolExpressionEvaluator.ValueErrorException e) {
            System.out.println("SUCCESS: Caught expected error -> " + e.getMessage());
        }

        // --------------------------------------------------
        // TEST CASE 4: Guardrail Check - Circular Loop
        // --------------------------------------------------
        System.out.println("\n--- [TEST 4] Verifying Circular Dependency Intercept ---");
        Map<String, String> input4 = new HashMap<>();
        input4.put("a", "b + 2");
        input4.put("b", "c * 3");
        input4.put("c", "a - 1"); // 'c' points back to 'a', triggering a 3-node cycle!

        SymbolExpressionEvaluator evaluator4 = new SymbolExpressionEvaluator(input4);
        try {
            evaluator4.evaluateAllSymbols();
        } catch (SymbolExpressionEvaluator.ValueErrorException e) {
            System.out.println("SUCCESS: Caught expected error -> " + e.getMessage());
        }
    }
}

