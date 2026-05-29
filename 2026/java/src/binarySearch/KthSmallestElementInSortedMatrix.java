package src.binarySearch;

/*
Given an n x n matrix where each of the rows and columns is sorted in ascending order, return the kth smallest element
in the matrix. Note that it is the kth smallest element in the sorted order, not the kth distinct element. You must find
a solution with a memory complexity better than O(n ^ 2).


Example 1:
Input: matrix = [[1,5,9],[10,11,13],[12,13,15]], k = 8
Output: 13
Explanation: The elements in the matrix are [1,5,9,10,11,12,13,13,15], and the 8th smallest number is 13

Example 2:
Input: matrix = [[-5]], k = 1
Output: -5

Constraints:

n == matrix.length == matrix[i].length
1 <= n <= 300
-109 <= matrix[i][j] <= 109
All the rows and columns of matrix are guaranteed to be sorted in non-decreasing order.
1 <= k <= n2

 */

/*
Time Complexity: O(N log(Max - Min)) where N is the matrix dimension. The value-range search takes a maximum of
log(Range) steps (around 32 iterations for standard integers). In each step, your staircase helper completes a flawless
linear O(N) scan. This is a massive production-grade upgrade over the standard Min-Heap approach which consumes O(N)
auxiliary memory.

Space Complexity: O(1) perfect constant space. It strictly scans coordinate pointers in-flight on the stack layout
without allocating any heap memory arrays.
 */

// Trick: Learn the staircase pattern and counting of smaller elements for a given target (mid). Only then the question
// becomes intuitive to solve.
public class KthSmallestElementInSortedMatrix {
    private int getKthSmallestElementFromSortedMatrix(int[][] matrix, int k){
        // given matrix is n x n, rows and columns are sorted.
        int n = matrix.length;
        int low = matrix[0][0]; // first element
        int high = matrix[n - 1][n - 1]; // last element
        int result = high;

        // Since rows and columns are sorted, we will use binary search. But it will on the actual values rather than
        // matrix coordinates. We will choose a 'mid' and check how many elements are smaller than that. If that count
        // is >= 'k', it means we have a potential answer. We will then tighten it by moving left of 'mid' because
        // anything right of that potential 'mid' will always be greater than 'k'.

        // To calculate the smaller elements than 'mid', we will move in the matrix in the staircase pattern, and we
        // start at the left bottom matrix position always. Why this pattern ? rows are sorted and so are column. So
        // let's say matrix[row][col] <= 'mid', it means all the elements in that column and top of that position are
        // also smaller. So we add that to the count. Now we should check for the next bigger element that can be
        // greater than 'mid' and that value will always be right of our current matrix position as rows are also
        // sorted. So we do col++ on the same row. Now if the given matrix[row][col] > 'mid', then we need to check for
        // a smaller element. So we move 1 layer up, that is we do row--.

        while(low <= high){
            int mid = low + (high - low) / 2;

            // check if 'mid' has smaller elements >= k
            if(countOfSmallerElementsThanMid(matrix, mid) >= k){
                // it means this 'mid' is our potential answer. Record it.
                result = mid;
                // now let's tighten the result by searching towards left of 'mid'. Those are the smaller values. We
                // will check if they can fit the current requirement.
                high = mid - 1;
            } else {
                // our current mid is small, we need a bigger mid. Move right.
                low = mid + 1;
            }

        }

        return result;
    }

    private int countOfSmallerElementsThanMid(int[][] matrix, int target){
        int n = matrix.length;
        // For staircase search we always start at left bottom matrix position which is matrix[n - 1][0]
        int row = n - 1;
        int col = 0;
        int count = 0;

        while(row >= 0 && col < n){
            // check if the current element is smaller than the given target
            if(matrix[row][col] <= target){
                // it means that all the values in that column above that position are also smaller than the target.
                // add them to the count
                count += (row + 1);
                // now we search for a bigger value to test the target
                col++;
            } else {
                // our current matrix element is bigger than the target, so we will try with a smaller element. Jump up.
                row--;
            }
        }

        return count;
    }

    public static void main(String[] args) {
        KthSmallestElementInSortedMatrix obj = new KthSmallestElementInSortedMatrix();
        System.out.println(obj.getKthSmallestElementFromSortedMatrix(new int[][]{{1,5,9},{10,11,13},{12,13,15}}, 8));
        System.out.println(obj.getKthSmallestElementFromSortedMatrix(new int[][]{{-5}}, 1));
    }
}
