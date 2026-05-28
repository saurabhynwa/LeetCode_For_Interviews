package src.binarySearch;

/*
You are given an m x n integer matrix 'matrix' with the following two properties: Each row is sorted in non-decreasing
order. The first integer of each row is greater than the last integer of the previous row. Given an integer target,
return true if target is in matrix or false otherwise.

You must write a solution in O(log(m * n)) time complexity.

Example 1:
Input: matrix = [[1,3,5,7],[10,11,16,20],[23,30,34,60]], target = 3
Output: true

Example 2:
Input: matrix = [[1,3,5,7],[10,11,16,20],[23,30,34,60]], target = 13
Output: false

Constraints:

m == matrix.length
n == matrix[i].length
1 <= m, n <= 100
-104 <= matrix[i][j], target <= 104
 */

/*
Time Complexity:
M = number of rows
N = number of columns

Below are two solutions. Highly optimized O(log M x N). Second version (still optimized) O(M log N)

Space Complexity: O(1)
 */
public class Search2DMatrix {

    public boolean searchMatrixLogMxN(int[][] matrix, int target) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return false;
        }

        int numRows = matrix.length;
        int numColumns = matrix[0].length;

        /*
        Because the problem states that the first integer of each row is greater than the last integer of the previous
        row, the entire matrix is actually one giant, continuously sorted 1D array wrapped into a 2D grid. We can
        flatten it conceptually and search it in a single binary search pass!
         */

        // Virtual 1D bounds
        int left = 0;
        int right = (numRows * numColumns) - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2; // Integer overflow guard

            // Core Mapping Formula: Turn 1D index into 2D coordinates
            // row = mid / numColumns (tells us how many full rows we have passed)
            // col = mid % numColumns (tells us the remaining offset in the current row)
            int midValue = matrix[mid / numColumns][mid % numColumns];

            if (midValue == target) {
                return true;
            } else if (midValue < target) {
                left = mid + 1; // Search right
            } else {
                right = mid - 1; // Search left
            }
        }

        return false;
    }


    public boolean searchMatrixMLogN(int[][] matrix, int target) {
        // given that rows of matrix are sorted, we will run binary search
        int numRows = matrix.length;
        int numColumns = matrix[0].length;

        // let's go row wise
        for(int row = 0; row < numRows; row++){
            int left = 0;
            int right = numColumns - 1;

            while(left <= right){
                // optimization: if the largest element in the row is smaller than target, skip that row
                if(matrix[row][right] < target){
                    break;
                }

                int mid = left + (right - left) / 2;

                if(matrix[row][mid] == target){
                    return true;
                } else if(matrix[row][mid] < target){
                    // search right of mid
                    left = mid + 1;
                } else {
                    // search left of mid
                    right = mid - 1;
                }
            }
        }

        return false;
    }

    public static void main(String[] args){
        Search2DMatrix search2DMatrix = new Search2DMatrix();
        int[][] matrix1 = new int[][]{{1,3,5,7},{10,11,16,20},{23,30,34,60}};
        int target1 = 3;

        System.out.println(search2DMatrix.searchMatrixLogMxN(matrix1, target1));
        System.out.println(search2DMatrix.searchMatrixMLogN(matrix1, target1));

        int[][] matrix2 = new int[][]{{1,3,5,7},{10,11,16,20},{23,30,34,60}};
        System.out.println(search2DMatrix.searchMatrixLogMxN(matrix1, 13));
        System.out.println(search2DMatrix.searchMatrixMLogN(matrix1, 13));
    }
}
