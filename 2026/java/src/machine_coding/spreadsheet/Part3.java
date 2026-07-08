package src.machine_coding.spreadsheet;

import java.util.*;

public class Part3 {
    private final Map<String, Integer> cellMap; // cache
    // forward dependencies - from current cell perspective, it means which cells I need to look to get updated
    private final Map<String, String> formulaRegistry; // stores raw formulas against the cell
    // reverse dependencies - cell to list of cell mapping, which is used to establish dependencies in the formula
    // basically, for the current cell it would tell which all columns it needs to notify if it changed
    private final Map<String, Set<String>> reverseDependents;

    public Part3(){
        this.cellMap = new HashMap<>();
        this.formulaRegistry = new HashMap<>();
        this.reverseDependents = new HashMap<>();
    }

    private int getCellValue(String operation, String cellName){
        if(operation == null || (!operation.equalsIgnoreCase("GET"))){
            return 0;
        }

        return cellMap.getOrDefault(cellName, 0);
    }

    private void setCellValue(String operation, String cellName, int value){
        if(operation == null || (!operation.equalsIgnoreCase("SET")) || cellName == null ||
                cellName.trim().isEmpty()){
            return;
        }

        setCellValue(operation, cellName, String.valueOf(value));
    }

    private void setCellValue(String operation, String cellName, String valuePayLoad){
        // sanity checks
        if(operation == null || (!operation.equalsIgnoreCase("SET")) || cellName == null ||
                cellName.trim().isEmpty()){
            return;
        }

        // first we need to erase the existing formula. For the current cell and any links from the notifications list,
        // where our current cell might be registered from the old formula cellNames
        clearOldDependencies(cellName);

        int value = 0;

        // process the new state
        if(valuePayLoad.startsWith("=")){
            // update the formula to the formula registry
            formulaRegistry.put(cellName, valuePayLoad);

            // evaluate formula and update the notifications list
            value = evaluateFormulaAndRegisterLinks(cellName, valuePayLoad);
        } else {
            // pure integer literal
            value = isIntegerLiteral(valuePayLoad) ? Integer.parseInt(valuePayLoad) : 0;
        }

        // update cache
        cellMap.put(cellName, value);

        // propagate updates to the cells that depend on the current cell
        propagateUpdates(cellName);
    }

    private void propagateUpdates(String cellName){
        // first let's get list of cells who are dependent on the current cell
        if(!reverseDependents.containsKey(cellName)){
            return;
        }

        Set<String> dependentCells = reverseDependents.get(cellName);

        // use BFS to traverse and update
        Queue<String> dependentQueue = new LinkedList<>(dependentCells);

        while(!dependentQueue.isEmpty()){
            String currentDependentCell = dependentQueue.poll();

            // fetch the older value for this
            int olderValue = cellMap.get(currentDependentCell);
            // now this cell could also have an entry in the formula registry
            String olderFormula = formulaRegistry.get(currentDependentCell);
            // now need to re-evaluate the older formula
            int newerValue = evaluateFormulaOnly(olderFormula);

            // update cache
            cellMap.put(currentDependentCell, newerValue);

            if(olderValue != newerValue && reverseDependents.containsKey(currentDependentCell)){
                // need to add the dependents of the currentDependent cell to the queue
                Set<String> dependentCellSet = reverseDependents.get(currentDependentCell);
                dependentQueue.addAll(dependentCellSet);
            }
        }
    }

    private int evaluateFormulaOnly(String formula){
        // formulas are stored in raw form
        String expression = formula.substring(1);

        if(expression.trim().isEmpty()){
            return 0;
        }

        // it can be cell or integer literal
        String[] tokens = expression.split("\\+");
        int accumulatedSum = 0;

        for(String token: tokens){
            if(token.trim().isEmpty()){
                continue;
            }

            // cell or integer literal
            if(isIntegerLiteral(token)){
                accumulatedSum += Integer.parseInt(token);
            } else {
                // get value from cache
                accumulatedSum += cellMap.getOrDefault(token, 0);
            }
        }
        return accumulatedSum;
    }

    private int evaluateFormulaAndRegisterLinks(String cellName, String valuePayLoad){
        String newFormula = valuePayLoad.substring(1);

        if(newFormula.trim().isEmpty()){
            return 0;
        }

        String[] newFormulaTokens = newFormula.split("\\+");
        int accumulatedSum = 0;

        for(String token: newFormulaTokens){
            if(token.trim().isEmpty()){
                continue;
            }

            // it can be cellName or integer literal
            if(isIntegerLiteral(token)){
                accumulatedSum += Integer.parseInt(token);
            } else {
                // add/update the current cell to the notification list
                reverseDependents.computeIfAbsent(token, k -> new HashSet<>()).add(cellName);
                accumulatedSum += cellMap.getOrDefault(token, 0);
            }
        }

        return accumulatedSum;
    }

    // we are removing the reference of the current cell from the list where other cells might have it listed as the
    // notification subscriber
    private void clearOldDependencies(String cellName){
        // check for previous formula presence for the current cell and remove those links
        if(!formulaRegistry.containsKey(cellName)){
            return;
        }

        String oldFormula = formulaRegistry.get(cellName);
        String oldExpression = oldFormula.substring(1);
        String[] oldTokens = oldExpression.split("\\+");

        for(String token: oldTokens){
            // it can be a cellName or integer literal. We act only on cellName.
            if(token.trim().isEmpty()){
                continue;
            }

            if(!isIntegerLiteral(token)){
                // from reverse dependent notification list we need to scrub our current cell
                reverseDependents.get(token).remove(cellName);
            }
        }
    }

    private boolean isIntegerLiteral(String token){
        if(token == null || token.trim().isEmpty()){
            return false;
        }

        // using regex to detect
        // - means -ve, ? optional, \\d is 0 to 9 and + means one or more
        return token.matches("-?\\d+");
    }

    public static void main(String[] args) {
        Part3 obj = new Part3();

        obj.setCellValue("SET", "B1", 10);
        obj.setCellValue("SET", "A1", "=B1+5");
        System.out.println(obj.getCellValue("GET", "A1"));
        obj.setCellValue("SET", "B1", 20);
        System.out.println(obj.getCellValue("GET", "A1"));

        obj.setCellValue("SET", "A1", 1);
        obj.setCellValue("SET", "B1", "=A1+2");
        obj.setCellValue("SET", "C1", "=B1+3");
        System.out.println(obj.getCellValue("GET", "C1"));
        obj.setCellValue("SET", "A1", 10);
        System.out.println(obj.getCellValue("GET", "C1"));
    }
}
