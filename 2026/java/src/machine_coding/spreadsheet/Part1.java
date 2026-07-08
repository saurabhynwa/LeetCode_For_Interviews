package src.machine_coding.spreadsheet;

import java.util.HashMap;
import java.util.Map;

public class Part1 {
    private final Map<String, Integer> cellMap;

    public Part1(){
        this.cellMap = new HashMap<>();
    }

    private void setCellValue(String operation, String cellName, int value){
        if(operation == null || (!operation.equalsIgnoreCase("SET")) || cellName == null ||
                cellName.trim().isEmpty()){
            return;
        }

        cellMap.put(cellName, value);
    }

    private int getCellValue(String operation, String cellName){
        if(operation == null || (!operation.equalsIgnoreCase("GET"))){
            return 0;
        }

        return cellMap.getOrDefault(cellName, 0);
    }

    public static void main(String[] args) {
        Part1 obj = new Part1();

        obj.setCellValue("SET", "A1", 10);
        System.out.println(obj.getCellValue("GET", "A1"));
        obj.setCellValue("SET", "B2", -3);
        System.out.println(obj.getCellValue("GET", "B2"));
        System.out.println(obj.getCellValue("GET", "C1"));
    }
}
