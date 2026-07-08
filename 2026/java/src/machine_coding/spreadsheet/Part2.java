package src.machine_coding.spreadsheet;

import java.util.HashMap;
import java.util.Map;

public class Part2 {
    private final Map<String, Integer> cellMap;

    public Part2(){
        this.cellMap = new HashMap<>();
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

    private void setCellValue(String operation, String cellName, String valuePayload){
        if(operation == null || (!operation.equalsIgnoreCase("SET")) || cellName == null ||
                cellName.trim().isEmpty()){
            return;
        }

        // check for value being empty
        if(valuePayload.trim().isEmpty()){
            cellMap.put(cellName, 0);
            return;
        }

        int value = 0;

        // now value can be a formula or integer literal
        if(valuePayload.startsWith("=")){
            value = evaluateFormula(cellName, valuePayload);
        } else {
            value = isIntegerLiteral(valuePayload) ? Integer.parseInt(valuePayload) : 0;
        }

        cellMap.put(cellName, value);
    }

    private int evaluateFormula(String cellName, String valuePayload){
        String expression = valuePayload.substring(1);

        if(expression.trim().isEmpty()){
            return 0;
        }

        String[] tokens = expression.split("\\+");
        int accumulatedSum = 0;

        for(String token: tokens){
            if(token.trim().isEmpty()){
                continue;
            }

            // else token is either an integer literal or a cell
            if(isIntegerLiteral(token)){
                accumulatedSum += Integer.parseInt(token);
            } else {
                // get the value from cellMap
                accumulatedSum += cellMap.getOrDefault(token, 0);
            }
        }

        return accumulatedSum;
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
        Part2 obj = new Part2();
        obj.setCellValue("SET", "B1", 10);
        obj.setCellValue("SET", "A1", "=B1+5");
        System.out.println(obj.getCellValue("GET", "A1"));
        obj.setCellValue("SET", "A1", "=2+3+4");
        System.out.println(obj.getCellValue("GET", "A1"));
    }
}
