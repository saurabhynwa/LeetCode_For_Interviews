package src.backtracking;

/*
Given an m x n grid of characters board and a string word, return true if word exists in the grid. The word can be
constructed from letters of sequentially adjacent cells, where adjacent cells are horizontally or vertically
neighboring. The same letter cell may not be used more than once.

Example 1:
Input: board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = "ABCCED"
Output: true

Example 2:
Input: board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = "SEE"
Output: true

Example 3:
Input: board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = "ABCB"
Output: false

Constraints:

m == board.length
n = board[i].length
1 <= m, n <= 6
1 <= word.length <= 15
board and word consists of only lowercase and uppercase English letters.

Follow up: Could you use search pruning to make your solution faster with a larger board?
 */

/*
Time Complexity: O(M x N x 3^K) where M × N is the grid size, and K is the length of the target word. We potentially
initiate a search from any of the M × N cells. For each search, the execution tree forks 3-directionally (4 directions,
but we don't step backward onto the # tile we just came from) up to a maximum depth of K.

Space Complexity: O(K). We perform our tile alterations entirely in-place on the input matrix, so we use no extra data
structure storage. The space footprint is bounded solely by the recursive JVM call stack, which reaches a maximum
height equal to the length of the word (K).
 */

public class WordSearch {
    // up, down, left and right
    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    private boolean exists(char[][] board, String word){
        // edge case
        if(board == null || board.length == 0 || board[0].length == 0 || word == null || word.isEmpty()){
            return false;
        }

        // 4 way grid DFS search
        // global variable = define directions grid 2d array
        // helper method = boundary condition check for fast exit
        // method with core logic = backtrack engine.
        // 5 steps in backtrack engine.
        // Step_1: success termination, wordCharIndex == word.length, return true
        // Step_2: fail fast check. Boundary condition violated or char on board doesn't match char on string, return false
        // Step_3: if the code flow comes after step_2, it means the board char and string word char matches. Store the
        // board char in a char variable and then mutate/overwrite it with a special char '#' to avoid loopy cycles
        // during DFS
        // Step_4: iterate over DIRECTION and launch 4 way DFS calling the backtrack engine, if found true, return true
        // Step_5: reverse the mutation and restore the board[row][col] with original matching char for our backtrack
        // algorithm to return. Finally return false;

        int totalRows = board.length;
        int totalCols = board[0].length;

        for(int row = 0; row < totalRows; row++){
            for(int col = 0; col < totalCols; col++){
                // only start backtrack when the first character matches from the word in the grid
                if(board[row][col] == word.charAt(0)){
                    if(backtrackEngine(board, word, row, col, 0)){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean isWithinBoundary(char[][] board, int row, int col){
        return row >= 0 && row < board.length && col >= 0 && col < board[0].length;
    }

    private boolean backtrackEngine(char[][] board, String word, int row, int col, int wordCharIndex){
        // 1. success termination
        if(wordCharIndex == word.length()){
            return true;
        }

        // 2. fail fast: boundary check or char match failure
        if(!isWithinBoundary(board, row, col) || board[row][col] != word.charAt(wordCharIndex)){
            return false;
        }

        // 3. if we have reached here, it means we have found a match. Store that in a char variable
        char matchingChar = board[row][col];
        // mutate the matching char to a special character symbol to avoid cyclic loop during DFS search
        board[row][col] = '#';

        // 4. launch 4 way DFS to continue our search
        for(int[] direction: WordSearch.DIRECTIONS){
            int nextRow = row + direction[0];
            int nextCol = col + direction[1];

            // launch backtrack
            boolean pathFound = backtrackEngine(board, word, nextRow, nextCol, wordCharIndex + 1);

            if(pathFound){
                return true;
            }
        }

        // 5. While coming back from backtracking we should undo the special character modification
        board[row][col] = matchingChar;

        return false;
    }

    public static void main(String[] args) {
        WordSearch wordSearch = new WordSearch();
        char[][] board1 = {{'A','B','C','E'},{'S','F','C','S'},{'A','D','E','E'}};
        String word1 = "ABCCED";
        System.out.println(wordSearch.exists(board1, word1));

        char[][] board2 = {{'A','B','C','E'},{'S','F','C','S'},{'A','D','E','E'}};
        String word2 = "ABCB";
        System.out.println(wordSearch.exists(board2, word2));
    }
}
