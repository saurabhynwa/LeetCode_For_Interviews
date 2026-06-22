package src.matrix;

/*
Given an m x n integer matrix 'matrix', if an element is 0, set its entire row and column to 0's. You must do it in
place.

Example 1:
Input: matrix = [[1,1,1],[1,0,1],[1,1,1]]
Output: [[1,0,1],[0,0,0],[1,0,1]]

Example 2:
Input: matrix = [[0,1,2,0],[3,4,5,2],[1,3,1,5]]
Output: [[0,0,0,0],[0,4,5,0],[0,3,1,0]]

Constraints:

m == matrix.length
n == matrix[0].length
1 <= m, n <= 200
-231 <= matrix[i][j] <= 231 - 1

Follow up:

A straightforward solution using O(mn) space is probably a bad idea.
A simple improvement uses O(m + n) space, but still not the best solution.
Could you devise a constant space solution?
 */

/*
Time Complexity: O(Rows x Columns)
Space Complexity: O(1)
 */

public class SetMatrixZeroes {
    private int[][] setZeroes(int[][] matrix){
        // edge case
        if(matrix == null || matrix.length == 0 || matrix[0].length == 0){
            return matrix;
        }

        // Logic: Here zero represents 2 states: original and mutated. For every original zero that we detect, we want
        // that row and column to be mutated to zero. For the zeroth row and column, first loop over them separately and
        // in a boolean flag capture whether their exists an original zero in them or not. Because afterward they might
        // have mutated zeros from the inner matrix cell, so it would be helpful w.r.t flag to tell them apart. For the
        // inner matrix, we first loop and for each original zero we mark that cell's row and col start as zero. In next
        // walk for these markers, we mutate the cells in their path to zero. At last, we check for the boolean flag for
        // starting row and column and mutate if needed.

        boolean startingRowHasOriginalZero = false;
        boolean startingColHasOriginalZero = false;

        // first walk the zeroth row and check for presence of original zero
        for(int col = 0; col < matrix[0].length; col++){
            if(matrix[0][col] == 0){
                startingRowHasOriginalZero = true;
                break;
            }
        }

        // first walk the zeroth col and check for presence of original zero
        for(int row = 0; row < matrix.length; row++){
            if(matrix[row][0] == 0){
                startingColHasOriginalZero = true;
                break;
            }
        }

        // inner matrix walk, detect original zero and mark the starting row & col for that as zero
        for(int row = 1; row < matrix.length; row++){
            for(int col = 1; col < matrix[0].length; col++){
                if(matrix[row][col] == 0){
                    // update the starting row and col for that cell as zero
                    matrix[0][col] = 0;
                    matrix[row][0] = 0;
                }
            }
        }

        // for inner matrix, if the cell is in range of mutated zero, mark them zero
        for(int row = 1; row < matrix.length; row++){
            for(int col = 1; col < matrix[0].length; col++){
                if(matrix[0][col] == 0 || matrix[row][0] == 0){
                    matrix[row][col] = 0;
                }
            }
        }

        // walk through the original row and check for mutation
        if(startingRowHasOriginalZero){
            for(int col = 0; col < matrix[0].length; col++){
                matrix[0][col] = 0;
            }
        }

        if(startingColHasOriginalZero){
            for(int row = 0; row < matrix.length; row++){
                matrix[row][0] = 0;
            }
        }
        return matrix;
    }

    private void printMatrix(int[][] matrix){
        if(matrix == null || matrix.length == 0 || matrix[0].length == 0){
            return;
        }

        for(int row = 0; row < matrix.length; row++){
            System.out.print("[");
            for(int col = 0; col < matrix[0].length; col++){
                if(col == matrix[0].length - 1) {
                    System.out.print(matrix[row][col] + "]");
                } else {
                    System.out.print(matrix[row][col] + " ");
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args){
        SetMatrixZeroes setMatrixZeroes = new SetMatrixZeroes();
        int[][] matrix = new int[][]{{1,1,1},{1,0,1},{1,1,1}};
        setMatrixZeroes.printMatrix(matrix);
        System.out.println();
        setMatrixZeroes.printMatrix(setMatrixZeroes.setZeroes(matrix));
    }
}
