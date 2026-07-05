package src.machine_coding.spreadsheet;

import java.util.HashMap;
import java.util.Map;

public class IntegerOnlySpreadsheetCells {
    private final Map<String, Integer> cells;

    public IntegerOnlySpreadsheetCells(){
        this.cells = new HashMap<>();
    }

    private void set(String operation, String cellName, int value){
        if(operation == null || (!operation.equalsIgnoreCase("SET")) || cellName == null ||
                cellName.trim().isEmpty()){
            return;
        }

        cells.put(cellName, value);
    }

    private int get(String operation, String cellName){
        if(operation == null || (!operation.equalsIgnoreCase("GET"))){
            return 0;
        }

        return cells.getOrDefault(cellName, 0);
    }

    public static void main(String[] args) {
        IntegerOnlySpreadsheetCells obj = new IntegerOnlySpreadsheetCells();
        obj.set("SET", "A1", 10);
        System.out.println(obj.get("GET", "A1"));
        obj.set("SET", "B2", -3);
        System.out.println(obj.get("GET", "B2"));
        System.out.println(obj.get("GET", "C1"));
    }
}
