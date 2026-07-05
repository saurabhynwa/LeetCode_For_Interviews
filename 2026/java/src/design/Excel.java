package src.design;

/*
Design the basic function of Excel and implement the function of the sum formula.

Implement the Excel class:

Excel(int height, char width) Initializes the object with the height and the width of the sheet. The sheet is an integer matrix mat of size height x width with the row index in the range [1, height] and the column index in the range ['A', width]. All the values should be zero initially.
void set(int row, char column, int val) Changes the value at mat[row][column] to be val.
int get(int row, char column) Returns the value at mat[row][column].
int sum(int row, char column, List<String> numbers) Sets the value at mat[row][column] to be the sum of cells represented by numbers and returns the value at mat[row][column]. This sum formula should exist until this cell is overlapped by another value or another sum formula. numbers[i] could be on the format:
"ColRow" that represents a single cell.
For example, "F7" represents the cell mat[7]['F'].
"ColRow1:ColRow2" that represents a range of cells. The range will always be a rectangle where "ColRow1" represent the position of the top-left cell, and "ColRow2" represents the position of the bottom-right cell.
For example, "B3:F7" represents the cells mat[i][j] for 3 <= i <= 7 and 'B' <= j <= 'F'.
Note: You could assume that there will not be any circular sum reference.

For example, mat[1]['A'] == sum(1, "B") and mat[1]['B'] == sum(1, "A").


Example 1:

Input
["Excel", "set", "sum", "set", "get"]
[[3, "C"], [1, "A", 2], [3, "C", ["A1", "A1:B2"]], [2, "B", 2], [3, "C"]]
Output
[null, null, 4, null, 6]

Explanation
Excel excel = new Excel(3, "C");
 // construct a 3*3 2D array with all zero.
 //   A B C
 // 1 0 0 0
 // 2 0 0 0
 // 3 0 0 0
excel.set(1, "A", 2);
 // set mat[1]["A"] to be 2.
 //   A B C
 // 1 2 0 0
 // 2 0 0 0
 // 3 0 0 0
excel.sum(3, "C", ["A1", "A1:B2"]); // return 4
 // set mat[3]["C"] to be the sum of value at mat[1]["A"] and the values sum of the rectangle range whose top-left cell is mat[1]["A"] and bottom-right cell is mat[2]["B"].
 //   A B C
 // 1 2 0 0
 // 2 0 0 0
 // 3 0 0 4
excel.set(2, "B", 2);
 // set mat[2]["B"] to be 2. Note mat[3]["C"] should also be changed.
 //   A B C
 // 1 2 0 0
 // 2 0 2 0
 // 3 0 0 6
excel.get(3, "C"); // return 6


Constraints:

1 <= height <= 26
'A' <= width <= 'Z'
1 <= row <= height
'A' <= column <= width
-100 <= val <= 100
1 <= numbers.length <= 5
numbers[i] has the format "ColRow" or "ColRow1:ColRow2".
At most 100 calls will be made to set, get, and sum.
 */

import java.util.*;

/*
Time Complexity:
1. get() -> O(1)
2. set() & sum() -> O(K x N) where K = number of cells in the formula range N = total downstream dependent cells
affected in the graph.

Space Complexity: O (R x C + E) where R x C = size of the grid matrix & E = total number of dependency edges tracked
inside our forward and reverse graph maps.
 */

public class Excel {

    private final int[][] valueGrid;

    // Forward Graph: Cell -> Map of (Dependency Cell -> How many times it is referenced)
    private final Map<String, Map<String, Integer>> forwardDependencies;

    // Reverse Graph: Cell -> Set of cells that depend on this cell for their calculation
    private final Map<String, Set<String>> reverseDependents;

    public Excel(int height, char width) {
        int columnsCount = width - 'A' + 1;
        this.valueGrid = new int[height + 1][columnsCount]; // 1-based indexing for rows
        this.forwardDependencies = new HashMap<>();
        this.reverseDependents = new HashMap<>();
    }

    /**
     * Set a raw integer value into a specific cell coordinate slot.
     */
    public void set(int row, char column, int val) {
        String cellKey = encodeCellKey(row, column);

        // 1. Break old bonds: Overwriting a cell completely clears its existing formula dependencies
        clearCellDependencies(cellKey);

        // 2. Write the raw value into our grid storage cache
        valueGrid[row][column - 'A'] = val;

        // 3. Proactive Propagation: Recursively update any downstream cells depending on this change
        propagateUpdates(cellKey);
    }

    // makes 1, A to A1
    private String encodeCellKey(int row, char col) {
        return col + String.valueOf(row);
    }

    private void clearCellDependencies(String cellKey) {
        if (!forwardDependencies.containsKey(cellKey)) {
            return;
        }

        // Remove this cell from the reverse dependents list of all its old dependencies
        Map<String, Integer> oldDependencies = forwardDependencies.remove(cellKey);
        for (String oldDependencyKey : oldDependencies.keySet()) {
            if (reverseDependents.containsKey(oldDependencyKey)) {
                reverseDependents.get(oldDependencyKey).remove(cellKey);
            }
        }
    }

    private void propagateUpdates(String cellKey) {
        if (!reverseDependents.containsKey(cellKey)) {
            return;
        }

        // Queue-based BFS traversal ensures deterministic updates down the dependency tree
        Queue<String> updateQueue = new LinkedList<>();
        updateQueue.addAll(reverseDependents.get(cellKey));

        while (!updateQueue.isEmpty()) {
            String currentDependentCell = updateQueue.poll();

            int previousCachedValue = fetchValueByCellKey(currentDependentCell);
            recomputeCellValue(currentDependentCell);
            int newUpdatedValue = fetchValueByCellKey(currentDependentCell);

            // If the recomputation actually altered the value, push updates further downstream
            if (previousCachedValue != newUpdatedValue && reverseDependents.containsKey(currentDependentCell)) {
                updateQueue.addAll(reverseDependents.get(currentDependentCell));
            }
        }
    }

    private int[] decodeCellKey(String cellKey) {
        int colIndex = cellKey.charAt(0) - 'A';
        int rowIndex = Integer.parseInt(cellKey.substring(1));
        return new int[]{rowIndex, colIndex};
    }

    private int fetchValueByCellKey(String cellKey) {
        int[] coords = decodeCellKey(cellKey);
        return valueGrid[coords[0]][coords[1]];
    }

    private void recomputeCellValue(String cellKey) {
        // If it's a raw value cell (no formula), its value remains unchanged
        if (!forwardDependencies.containsKey(cellKey)) {
            return;
        }

        int totalAccumulatedSum = 0;
        Map<String, Integer> components = forwardDependencies.get(cellKey);

        // Sum up the values: Component Value * Reference Frequency
        for (Map.Entry<String, Integer> entry : components.entrySet()) {
            String dependencyKey = entry.getKey();
            int occurrenceFrequency = entry.getValue();

            totalAccumulatedSum += fetchValueByCellKey(dependencyKey) * occurrenceFrequency;
        }

        // Cache the newly computed result directly back into our matrix grid
        int[] coordinates = decodeCellKey(cellKey);
        valueGrid[coordinates[0]][coordinates[1]] = totalAccumulatedSum;
    }

    /**
     * Get the current precomputed value of a cell instantly in O(1) constant time.
     */
    public int get(int row, char column) {
        return valueGrid[row][column - 'A'];
    }

    /**
     * Assigns a dynamic sum formula to a target cell coordinate.
     */
    public int sum(int row, char column, List<String> numbers) {
        String targetCellKey = encodeCellKey(row, column);

        // Clear any old historical formula bonds out of the graphs first
        clearCellDependencies(targetCellKey);

        Map<String, Integer> currentFormulaDependencies = new HashMap<>();

        // Parse the input tokens (handles single cell lookups like "A1" and range pairs like "A1:B2")
        for (String token : numbers) {
            if (!token.contains(":")) {
                currentFormulaDependencies.put(token, currentFormulaDependencies.getOrDefault(token, 0) + 1);
            } else {
                String[] rangeBoundaries = token.split(":");
                expandAndRegisterRange(rangeBoundaries[0], rangeBoundaries[1], currentFormulaDependencies);
            }
        }

        // Register the new forward formula lookup context
        forwardDependencies.put(targetCellKey, currentFormulaDependencies);

        // Link the reverse dependencies so any change upstream correctly propagates down to us
        for (String dependencyKey : currentFormulaDependencies.keySet()) {
            reverseDependents.computeIfAbsent(dependencyKey, k -> new HashSet<>()).add(targetCellKey);
        }

        // Compute the initial value of our new sum formula context
        recomputeCellValue(targetCellKey);

        // Propagate the change downstream to anything that relies on this sum cell
        propagateUpdates(targetCellKey);

        return valueGrid[row][column - 'A'];
    }

    private void expandAndRegisterRange(String topLeft, String bottomRight, Map<String, Integer> dependencies) {
        int startRow = Integer.parseInt(topLeft.substring(1));
        char startCol = topLeft.charAt(0);

        int endRow = Integer.parseInt(bottomRight.substring(1));
        char endCol = bottomRight.charAt(0);

        for (int r = startRow; r <= endRow; r++) {
            for (char c = startCol; c <= endCol; c++) {
                String cellKey = encodeCellKey(r, c);
                dependencies.put(cellKey, dependencies.getOrDefault(cellKey, 0) + 1);
            }
        }
    }

    public static void main(String[] args) {
        Excel obj = new Excel(3, 'C');
        obj.set(1, 'A', 2);
        System.out.println(obj.sum(3, 'C', List.of("A1", "A1:B2")));
        obj.set(2, 'B', 2);
        System.out.println(obj.get(3, 'C'));
    }
}

