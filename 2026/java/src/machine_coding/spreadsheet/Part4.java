package src.machine_coding.spreadsheet;

import java.util.*;

public class Part4 {
    private final Map<String, Integer> cellMap;
    // current cell can have formula. So it will depend on the cells in its formula to calculate the value for itself
    private final Map<String, String> formulaRegistry;
    // on our current cell there would be other cells that are dependent.
    private final Map<String, Set<String>> reverseDependents;

    public Part4(){
        this.cellMap = new HashMap<>();
        this.formulaRegistry = new HashMap<>();
        this.reverseDependents = new HashMap<>();
    }

    private boolean setCellValue(String operation, String cellName, int value){
        // sanity checks
        if(operation == null || !operation.equalsIgnoreCase("SET") || cellName == null ||
                cellName.trim().isEmpty()){
            return false;
        }

        return setCellValue(operation, cellName, String.valueOf(value));
    }

    private boolean setCellValue(String operation, String cellName, String valuePayload){
        // sanity checks
        if(operation == null || !operation.equalsIgnoreCase("SET") || cellName == null ||
                cellName.trim().isEmpty()){
            return false;
        }

        if(valuePayload.trim().isEmpty()){
            cellMap.put(cellName, 0);
            return true;
        }

        // add a pre-flight guardrail to detect cycle.
        boolean isCircularDependency = wouldCreateCircularDependency(cellName, valuePayload);

        if(isCircularDependency){
            return false;
        }

        // first erase the older formula links for our current cell
        clearOldDependencies(cellName);

        int value = 0;

        // valuePayload can be either a formula or an integer literal
        if(valuePayload.startsWith("=")){
            // update the formula Registry with the new raw formula
            formulaRegistry.put(cellName, valuePayload);

            // for the new cells in the formula, we need to add the current cell to its notification list
            value = registerAndEvaluateFormula(cellName, valuePayload);
        } else {
            value = isIntegerLiteral(valuePayload) ? Integer.parseInt(valuePayload) : 0;
        }

        // updated the cache
        cellMap.put(cellName, value);

        // need to propagate updates for the current cell
        propagateUpdates(cellName);

        return true;
    }

    private boolean wouldCreateCircularDependency(String target, String valuePayload){
        if(valuePayload.startsWith("=")){
            String formula = valuePayload.substring(1);

            if(formula.trim().isEmpty()){
                return false;
            }

            String[] tokens = formula.split("\\+");

            // from these tokens we are only interested in the cellNames.
            Set<String> immediateDependencies = new HashSet<>();

            for(String token: tokens){
                if(token.trim().isEmpty()){
                    continue;
                }

                if(!isIntegerLiteral(token)){
                    immediateDependencies.add(token);
                }
            }

            // first let's check for self dependency loop
            for(String dependentCellName: immediateDependencies){
                if(dependentCellName.equals(target)){
                    // self dependency detected
                    return true;
                }
            }

            // indirect dependency, use BFS traversal
            Queue<String> trackingQueue = new LinkedList<>(immediateDependencies);
            Set<String> visitedElements = new HashSet<>(immediateDependencies);

            while(!trackingQueue.isEmpty()){
                String activeNode = trackingQueue.poll();

                // target check
                if(activeNode.equals(target)){
                    return true;
                }

                // let's check for the active node if there is any existing formula
                String formulaForActiveNode = formulaRegistry.getOrDefault(activeNode, "");

                if(formulaForActiveNode.trim().isEmpty()){
                    continue;
                }

                // extract any cells from this formula
                String expression = formulaForActiveNode.substring(1);

                if(expression.trim().isEmpty()){
                    continue;
                }

                String[] expressionTokens = expression.split("\\+");

                for(String token: expressionTokens){
                    if(!token.trim().isEmpty() && !isIntegerLiteral(token)){
                        if(!visitedElements.contains(token)){
                            visitedElements.add(token);
                            trackingQueue.add(token);
                        }
                    }
                }
            }
        }
        return false;
    }

    private void propagateUpdates(String cellName){
        if(!reverseDependents.containsKey(cellName)){
            return;
        }

        Set<String> dependentCells = reverseDependents.get(cellName);

        // BFS traversal to update the dependent cell values
        Queue<String> trackingQueue = new LinkedList<>(dependentCells);

        while(!trackingQueue.isEmpty()){
            String dependentCellName = trackingQueue.poll();

            // let's fetch the existing value for this cell
            int oldValue = cellMap.getOrDefault(dependentCellName, 0);
            // fetch the formula for this cell from registry
            String formula = formulaRegistry.get(dependentCellName);
            // let's re-evaluate this formula
            int newValue = evaluateFormula(formula);

            // we need to update the cache
            cellMap.put(dependentCellName, newValue);

            if(oldValue != newValue && reverseDependents.containsKey(dependentCellName)){
                trackingQueue.addAll(reverseDependents.get(dependentCellName));
            }
        }
    }

    private int registerAndEvaluateFormula(String cellName, String valuePayload){
        String formula = valuePayload.substring(1);

        if(formula.trim().isEmpty()){
            return 0;
        }

        String[] tokens = formula.split("\\+");
        int accumulatedSum = 0;

        for(String token: tokens){
            if(token.trim().isEmpty()){
                continue;
            }

            if(isIntegerLiteral(token)){
                accumulatedSum += Integer.parseInt(token);
            } else {
                // register the current cell to the token's notification list
                reverseDependents.computeIfAbsent(token, k -> new HashSet<>()).add(cellName);

                // fetch from cellMap the value for current token
                accumulatedSum += cellMap.getOrDefault(token, 0);
            }
        }

        return accumulatedSum;
    }

    private void clearOldDependencies(String cellName){
        if(!formulaRegistry.containsKey(cellName)){
            return;
        }

        String olderFormula = formulaRegistry.get(cellName);

        if(olderFormula.startsWith("=")){
            String expression = olderFormula.substring(1);

            if(expression.trim().isEmpty()){
                return;
            }

            String[] tokens = expression.split("\\+");

            for(String token: tokens){
                if(token.trim().isEmpty()){
                    continue;
                }

                // we are interested in token which is cellName
                if(!isIntegerLiteral(token) && reverseDependents.containsKey(token)){
                    reverseDependents.get(token).remove(cellName);
                }
            }
        }
    }

    private int evaluateFormula(String valuePayload){
        String formula = valuePayload.substring(1);

        if(formula.trim().isEmpty()){
            return 0;
        }

        String[] tokens = formula.split("\\+");
        int accumulatedSum = 0;

        for(String token: tokens){
            if(token.trim().isEmpty()){
                continue;
            }

            // token is either a cellName or integer literal
            if(isIntegerLiteral(token)){
                accumulatedSum += Integer.parseInt(token);
            } else {
                // check from the cellMap about the value for that column
                accumulatedSum += cellMap.getOrDefault(token, 0);
            }
        }

        return accumulatedSum;
    }

    private boolean isIntegerLiteral(String token){
        if(token == null || token.trim().isEmpty()){
            return false;
        }

        // using regex to determine the integer literal
        // - means -ve, ? optional, \\d 0 to 9 and + means one or more
        return token.matches("-?\\d+");
    }

    private int getCellValue(String operation, String cellName){
        // sanity check
        if(operation == null || !operation.equalsIgnoreCase("GET") || cellName == null ||
                cellName.trim().isEmpty()){
            return 0;
        }

        return cellMap.getOrDefault(cellName, 0);
    }

    public static void main(String[] args){
        Part4 obj = new Part4();

        System.out.println(obj.setCellValue("SET", "A1", "=A1+1"));
        System.out.println(obj.getCellValue("GET", "A1"));

        System.out.println(obj.setCellValue("SET", "A1", "=B1+1"));
        System.out.println(obj.setCellValue("SET", "B1", "=A1+1"));
        System.out.println(obj.getCellValue("GET", "A1"));
        System.out.println(obj.getCellValue("GET", "B1"));
    }
}
