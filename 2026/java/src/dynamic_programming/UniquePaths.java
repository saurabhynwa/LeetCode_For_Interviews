package src.dynamic_programming;

/*
There is a robot on an m x n grid. The robot is initially located at the top-left corner (i.e., grid[0][0]). The robot
tries to move to the bottom-right corner (i.e., grid[m - 1][n - 1]). The robot can only move either down or right at any
point in time.

Given the two integers m and n, return the number of possible unique paths that the robot can take to reach the
bottom-right corner.

The test cases are generated so that the answer will be less than or equal to 2 * 109.
 */

import java.util.Arrays;

/*
Time Complexity = O(M x N)
Space Complexity = O(N)
 */

public class UniquePaths {
    public int uniquePaths(int m, int n) {
        // Edge Case Validation
        if (m <= 0 || n <= 0) {
            return 0;
        }

        int totalRows = m;
        int totalCols = n;

        // Space Optimization: We only need a single 1D array representing
        // the path counts of the current row partition.
        int[] rowPathLedger = new int[totalCols];

        // Base Case Invariant: For the very first row, there is exactly 1 way
        // to reach any cell (by moving strictly right continuously).
        Arrays.fill(rowPathLedger, 1);

        // Process the remaining rows iteratively from top to bottom
        for (int row = 1; row < totalRows; row++) {

            // Step across columns from left to right.
            // Index 0 represents the first column, which always stays 1
            // because you can only reach it by moving strictly down.
            for (int col = 1; col < totalCols; col++) {

                // Mathematical Invariant Equation:
                // New Value = Old Value (Paths from Above) + Previous Element (Paths from Left)
                rowPathLedger[col] = rowPathLedger[col] + rowPathLedger[col - 1];
            }
        }

        // The final slot contains the accumulated unique paths to the bottom-right corner
        return rowPathLedger[n - 1];
    }
}
