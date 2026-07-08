package src.machine_coding.symbol_expressions;

import java.util.*;

public class SymbolCycleDetector {

    // Graph coloring states for clean cycle validation
    private enum NodeColor { WHITE, GRAY, BLACK }

    private final Map<String, String> expressionsRegistry;
    private final Map<String, Double> evaluationCache;
    private final Map<String, NodeColor> colorMap;

    public SymbolCycleDetector(Map<String, String> rawInputMap) {
        this.expressionsRegistry = rawInputMap != null ? rawInputMap : new HashMap<>();
        this.evaluationCache = new HashMap<>();
        this.colorMap = new HashMap<>();

        // Initialize all nodes as completely Unvisited (WHITE)
        for (String symbol : expressionsRegistry.keySet()) {
            colorMap.put(symbol, NodeColor.WHITE);
        }
    }

    /**
     * CORE ENTRY POINT FOR PART 2:
     * Simply returns whether a cycle exists anywhere in the defined symbols registry.
     * Reuses our exact tokenization core from Part 1.
     */
    public boolean hasCircularDependency() {
        // Reset state tracker before a fresh scan pass
        for (String symbol : expressionsRegistry.keySet()) {
            colorMap.put(symbol, NodeColor.WHITE);
        }

        for (String symbol : expressionsRegistry.keySet()) {
            if (colorMap.get(symbol) == NodeColor.WHITE) {
                if (dfsCheckForCycle(symbol)) {
                    return true; // Cycle found! Intercept and return early.
                }
            }
        }
        return false; // Pristine, acyclic topology chart.
    }

    /**
     * CORE ENTRY POINT FOR PART 1:
     * Evaluates everything down to final values. Runs cycle detection first to fail fast.
     */
    public Map<String, Double> evaluateAllSymbols() {
        // Part 2 Rule Integration: Validate graph integrity before touching data caches
        if (hasCircularDependency()) {
            throw new ValueErrorException("Circular dependency detected during pre-flight scan!");
        }

        Map<String, Double> finalResultMap = new HashMap<>();
        for (String symbol : expressionsRegistry.keySet()) {
            double calculatedValue = resolveSymbolValueDFS(symbol);
            finalResultMap.put(symbol, calculatedValue);
        }
        return finalResultMap;
    }

    private boolean dfsCheckForCycle(String activeSymbol) {
        colorMap.put(activeSymbol, NodeColor.GRAY); // In-progress tracking tag

        String rawExpression = expressionsRegistry.get(activeSymbol);
        List<String> tokens = tokenizeExpression(rawExpression); // Reuse Tokenizer from Part 1!

        for (String token : tokens) {
            // Part 2 Invariant: Only defined dictionary keys create active graph edges
            if (isSymbolName(token) && expressionsRegistry.containsKey(token)) {
                NodeColor parentColor = colorMap.get(token);

                if (parentColor == NodeColor.GRAY) {
                    return true; // Cycle Intercept: Trapped a loop back to an active gray node!
                }

                if (parentColor == NodeColor.WHITE) {
                    if (dfsCheckForCycle(token)) {
                        return true;
                    }
                }
            }
        }

        colorMap.put(activeSymbol, NodeColor.BLACK); // Safe node confirmation tag
        return false;
    }

    private double resolveSymbolValueDFS(String activeSymbol) {
        if (!expressionsRegistry.containsKey(activeSymbol)) {
            throw new ValueErrorException("Unknown symbol: " + activeSymbol);
        }

        // Memoization Win: If already calculated, pull from cache in O(1) time
        if (evaluationCache.containsKey(activeSymbol)) {
            return evaluationCache.get(activeSymbol);
        }

        String rawExpression = expressionsRegistry.get(activeSymbol);
        List<String> tokensList = tokenizeExpression(rawExpression);

        // Substitute word symbol tokens with their actual underlying numeric valuations
        for (int i = 0; i < tokensList.size(); i++) {
            String token = tokensList.get(i);
            if (isSymbolName(token)) {
                double resolvedParentValue = resolveSymbolValueDFS(token);
                tokensList.set(i, String.valueOf(resolvedParentValue));
            }
        }

        // Run the math calculations over the fully substituted token collection
        double finalEvaluatedValue = computeMathExpression(tokensList);
        evaluationCache.put(activeSymbol, finalEvaluatedValue);

        return finalEvaluatedValue;
    }

    private List<String> tokenizeExpression(String inputExpression) {
        List<String> tokens = new ArrayList<>();
        if (inputExpression == null) return tokens;

        int length = inputExpression.length();
        int index = 0;

        while (index < length) {
            char activeChar = inputExpression.charAt(index);

            if (Character.isWhitespace(activeChar)) {
                index++;
                continue;
            }

            if ("+-*/()".indexOf(activeChar) != -1) {
                tokens.add(String.valueOf(activeChar));
                index++;
                continue;
            }

            StringBuilder wordAccumulator = new StringBuilder();
            while (index < length && !Character.isWhitespace(inputExpression.charAt(index)) &&
                    "+-*/()".indexOf(inputExpression.charAt(index)) == -1) {
                wordAccumulator.append(inputExpression.charAt(index));
                index++;
            }
            tokens.add(wordAccumulator.toString());
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
                operatorsCacheStack.pop();
            } else if (token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/")) {
                char currentOp = token.charAt(0);
                while (!operatorsCacheStack.isEmpty() && getOperatorPrecedence(operatorsCacheStack.peek()) >= getOperatorPrecedence(currentOp)) {
                    executeTopOperationStack(valuesCacheStack, operatorsCacheStack);
                }
                operatorsCacheStack.push(currentOp);
            } else {
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
            case '+': values.push(leftOperand + rightOperand); break;
            case '-': values.push(leftOperand - rightOperand); break;
            case '*': values.push(leftOperand * rightOperand); break;
            case '/': values.push(leftOperand / rightOperand); break;
        }
    }

    private int getOperatorPrecedence(char operator) {
        if (operator == '*' || operator == '/') return 2;
        if (operator == '+' || operator == '-') return 1;
        return 0;
    }

    private boolean isSymbolName(String token) {
        if (token == null || token.isEmpty()) return false;
        return Character.isLetter(token.charAt(0));
    }

    public static class ValueErrorException extends RuntimeException {
        public ValueErrorException(String alertMessage) {
            super(alertMessage);
        }
    }

    /**
     * Formatting Utility: Cleans up trailing '.0' whole integers to match example outputs.
     */
    private static void printCleanOutputMap(Map<String, Double> results) {
        System.out.print("{ ");
        int counter = 0;
        for (Map.Entry<String, Double> entry : results.entrySet()) {
            double rawVal = entry.getValue();
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
        // -----------------------------------------------------------------
        // TEST CASE 1: Standard Acyclic Evaluation Tree (Part 1 + Part 2 Verification)
        // -----------------------------------------------------------------
        System.out.println("--- [TEST 1] Running Clean Acyclic Tree ---");
        Map<String, String> acyclicGraph = new HashMap<>();
        acyclicGraph.put("a", "3");
        acyclicGraph.put("b", "a + 2");
        acyclicGraph.put("c", "b * 4");
        acyclicGraph.put("d", "c - a");

        SymbolCycleDetector pipeline1 = new SymbolCycleDetector(acyclicGraph);

        // Check Part 2 Rule: Confirm no cycle exists
        System.out.println("  Graph Has Cycle? -> " + pipeline1.hasCircularDependency()); // Expected: false

        // Check Part 1 Rule: Evaluate values
        Map<String, Double> results1 = pipeline1.evaluateAllSymbols();
        System.out.print("  Calculated Layout: ");
        printCleanOutputMap(results1); // Expected: { a: 3, b: 5, c: 20, d: 17 }


        // -----------------------------------------------------------------
        // TEST CASE 2: Complex Parentheses & Float Division Evaluation
        // -----------------------------------------------------------------
        System.out.println("\n--- [TEST 2] Running Math & Parentheses Evaluation ---");
        Map<String, String> mathGraph = new HashMap<>();
        mathGraph.put("x", "8");
        mathGraph.put("y", "(x + 4) / 3");
        mathGraph.put("z", "y * 2");

        SymbolCycleDetector pipeline2 = new SymbolCycleDetector(mathGraph);
        System.out.println("  Graph Has Cycle? -> " + pipeline2.hasCircularDependency()); // Expected: false

        Map<String, Double> results2 = pipeline2.evaluateAllSymbols();
        System.out.print("  Calculated Layout: ");
        printCleanOutputMap(results2); // Expected: { x: 8, y: 4.0, z: 8.0 }


        // -----------------------------------------------------------------
        // TEST CASE 3: Standard Part 2 Edge Case -> Undefined Symbols
        // Rule: Cycle detector MUST ignore it. Evaluation engine MUST catch it.
        // -----------------------------------------------------------------
        System.out.println("\n--- [TEST 3] Running Part 2 Edge Case (Undefined Symbols) ---");
        Map<String, String> undefinedReferencesGraph = new HashMap<>();
        undefinedReferencesGraph.put("x", "y + 5"); // 'y' is completely undefined in the map!
        undefinedReferencesGraph.put("z", "10");

        SymbolCycleDetector pipeline3 = new SymbolCycleDetector(undefinedReferencesGraph);

        // 1. Part 2 Check: Cycle detector MUST return false because 'y' doesn't form a defined edge loop!
        System.out.println("  Step A (Cycle Check) -> Has Cycle? -> " + pipeline3.hasCircularDependency()); // Expected: false

        // 2. Part 1 Check: Trying to evaluate this MUST throw an Unknown Symbol ValueErrorException
        try {
            System.out.println("  Step B (Evaluation Pass) -> Attempting Full Evaluation...");
            pipeline3.evaluateAllSymbols();
        } catch (SymbolCycleDetector.ValueErrorException e) {
            System.out.println("  Step B Success -> Intercepted Error: " + e.getMessage()); // Expected: Unknown symbol: y
        }


        // -----------------------------------------------------------------
        // TEST CASE 4: The 3-Node Circular Loop Trap
        // -----------------------------------------------------------------
        System.out.println("\n--- [TEST 4] Running Part 2 Multi-Node Loop Intercept ---");
        Map<String, String> cyclicGraph = new HashMap<>();
        cyclicGraph.put("a", "b + 1");
        cyclicGraph.put("b", "c + 1");
        cyclicGraph.put("c", "a + 1"); // 'c' points back to 'a', throwing a 3-node cycle loop!

        SymbolCycleDetector pipeline4 = new SymbolCycleDetector(cyclicGraph);

        // 1. Part 2 Check: Confirm cycle is caught immediately
        System.out.println("  Step A (Cycle Check) -> Has Cycle? -> " + pipeline4.hasCircularDependency()); // Expected: true

        // 2. Part 1 Check: Pre-flight validator should immediately abort before calculations
        try {
            System.out.println("  Step B (Evaluation Pass) -> Attempting Full Evaluation...");
            pipeline4.evaluateAllSymbols();
        } catch (SymbolCycleDetector.ValueErrorException e) {
            System.out.println("  Step B Success -> Intercepted Error: " + e.getMessage()); // Expected: Circular dependency detected during pre-flight scan!
        }
    }
}


