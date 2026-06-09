package src.dfs;

/*
Given the root of a binary tree and an integer targetSum, return all root-to-leaf paths where the sum of the node values
in the path equals targetSum. Each path should be returned as a list of the node values, not node references. A
root-to-leaf path is a path starting from the root and ending at any leaf node. A leaf is a node with no children.

Example 1:
Input: root = [5,4,8,11,null,13,4,7,2,null,null,5,1], targetSum = 22
            [5]
           /   \
         [4]    [8]
        /      /   \
      [11]    [13]  [4]
     /    \        /   \
   [7]    [2]    [5]    [1]


Output: [[5,4,11,2],[5,8,4,5]]
Explanation: There are two paths whose sum equals targetSum:
5 + 4 + 11 + 2 = 22
5 + 8 + 4 + 5 = 22

Example 2:
Input: root = [1,2,3], targetSum = 5
Output: []

Example 3:
Input: root = [1,2], targetSum = 0
Output: []

Constraints:

The number of nodes in the tree is in the range [0, 5000].
-1000 <= Node.val <= 1000
-1000 <= targetSum <= 1000
 */

import java.util.ArrayList;
import java.util.List;

/*
Time Complexity: O(N^2) in the absolute worst-case scenario (such as a completely balanced tree where every single leaf
node represents a valid path match). Visiting the N nodes takes O(N), but creating a deep copy clone of paths up to
length H (height) adds an O(H) cost per match. For standard trees, it performs at an average speed of O(N).

Space Complexity: O(H) auxiliary memory footprint on the call stack, matching your standard tree stack boundaries.
 */
public class PathSumGetAllValidPaths {
    private List<List<Integer>> getAllValidPaths(TreeNode root, int targetSum){
        // we will use dfs, but there won't be any need of the recursive value. So use void as return type. 2 key things
        // here. Whenever we find a leaf node with the targetSum, we 'deep copy' the active path instance to our result.
        // Because we are going to maintain only a single active path instance. So when we backtrack or bubble up from
        // recursion, we will return the active path as it was received to us from the parent. So if we don't 'deep copy'
        // the instance, backtracking will empty our result.
        List<List<Integer>> allValidPaths = new ArrayList<>();
        List<Integer> activePath = new ArrayList<>();
        dfs(root, targetSum, activePath, allValidPaths);
        return allValidPaths;
    }

    private void dfs(TreeNode root, int targetSum, List<Integer> activePath, List<List<Integer>> allValidPaths){
        // if we hit null node, just return
        if(root == null){
            return;
        }

        // first we add current root's value to the active path
        activePath.add(root.val);
        // update the target
        int updatedTarget = targetSum - root.val;

        // then we check if we are on a leaf node and has the target been achieved ?
        if(root.left == null && root.right == null && updatedTarget == 0){
            // we found a valid path. Deep copy it in the result list.
            allValidPaths.add(new ArrayList<>(activePath));
        } else {
            // either we are not on a leaf node or target hasn't been achieved. So continue traversing down left and
            // right nodes.
            dfs(root.left, updatedTarget, activePath, allValidPaths);
            dfs(root.right, updatedTarget, activePath, allValidPaths);
        }

        // now while backtracking we need to clean up after ourselves. Meaning, we remove the latest node that our
        // function call stack attached to the active path which we received from the parent.
        activePath.remove(activePath.size() - 1);
    }

    private void printResult(List<List<Integer>> result){
        for(List list: result){
            System.out.print(" [");
            for(int i = 0; i < list.size(); i++){
                if(i == list.size() - 1){
                    System.out.print(list.get(i));
                } else {
                    System.out.print(list.get(i) + ", ");
                }
            }
            System.out.print("] ");
        }
    }

    public static void main(String[] args){
        PathSumGetAllValidPaths pathSumGetAllValidPaths = new PathSumGetAllValidPaths();
        TreeNode t1 = new TreeNode(5);
        TreeNode t2 = new TreeNode(4);
        TreeNode t3 = new TreeNode(8);
        TreeNode t4 = new TreeNode(11);
        TreeNode t5 = new TreeNode(7);
        TreeNode t6 = new TreeNode(2);
        TreeNode t7 = new TreeNode(8);
        TreeNode t8 = new TreeNode(13);
        TreeNode t9 = new TreeNode(4);
        TreeNode t10 = new TreeNode(5);
        TreeNode t11 = new TreeNode(1);

        t1.left = t2;
        t1.right = t3;
        t2.left = t4;
        t4.left = t5;
        t4.right = t6;
        t3.left = t8;
        t3.right = t9;
        t9.left = t10;
        t9.right = t11;

        pathSumGetAllValidPaths.printResult(pathSumGetAllValidPaths.getAllValidPaths(t1, 22));
    }
}
