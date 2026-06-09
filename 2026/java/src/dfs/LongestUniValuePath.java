package src.dfs;

/*
Given the root of a binary tree, return the length of the longest path, where each node in the path has the same value.
This path may or may not pass through the root. The length of the path between two nodes is represented by the number of edges between them.

Example 1:
        [5]
       /   \
     [4]    [5]
    /   \      \
  [1]    [1]    [5]
Input: root = [5,4,5,1,1,null,5]
Output: 2
Explanation: The shown image shows that the longest path of the same value (i.e. 5).

Example 2:
            [1]
           /   \
         [4]    [5]
        /   \      \
      [4]    [4]    [5]

Input: root = [1,4,5,4,4,null,5]
Output: 2
Explanation: The shown image shows that the longest path of the same value (i.e. 4).


Constraints:

The number of nodes in the tree is in the range [0, 104].
-1000 <= Node.val <= 1000
The depth of the tree will not exceed 1000.
 */

/*
Time Complexity: O(N)

Space Complexity: O(H)
 */

public class LongestUniValuePath {
    private int maxUniValuePath;

    private int getLongestUniValuePath(TreeNode root){
        // this is a mix of diameter of binary tree and what the current node needs to return. Most important thing
        // mentioned here is that answer doesn't necessarily need to go from root. So we will need a global max variable.
        dfs(root);
        return maxUniValuePath;
    }

    private int dfs(TreeNode currentRoot){
        // a null node cannot have a longest uniValue path downstream. So it returns that path length as zero.
        // Base case: Empty node contributes 0 to the path chain length
        if(currentRoot == null){
            return 0;
        }

        // leaf node can never form uni-value path downstream as they don't have edges below. So we need to have this
        // check. Now imagine we are on the parent node of leaf node. We will get 2 values from left and right side
        // which tells the uniValue path for their subtrees respectively. Now if parent' value is same as left node,
        // then we need to factor in for that edge and extend the answer of left. Same can happen on right. So our
        // parent node can land in a situation where left and right node values are same, so a uniValue path is
        // running through it. We evaluate max here. Now after that the parent needs to bubble up information to its
        // parent. It will send the maximum/best value from it's left and right nodes.
        int leftSubTreeUniValuePath = dfs(currentRoot.left);
        int rightSubTreeUniValuePath = dfs(currentRoot.right);

        int currentUniValuePathOnLeft = 0;
        int currentUniValuePathOnRight = 0;

        if(currentRoot.left != null && currentRoot.val == currentRoot.left.val){
            // extend the path on left side
            currentUniValuePathOnLeft = leftSubTreeUniValuePath + 1;
        }

        if(currentRoot.right != null && currentRoot.val == currentRoot.right.val){
            // extend the path on right side
            currentUniValuePathOnRight = rightSubTreeUniValuePath + 1;
        }

        // now evaluate max
        maxUniValuePath = Math.max(maxUniValuePath, currentUniValuePathOnLeft + currentUniValuePathOnRight);

        // return the longest among left and righ uniValue paths from currentRoot's perspective to its parent
        return Math.max(currentUniValuePathOnLeft, currentUniValuePathOnRight);
    }

    public static void main(String[] args){
        LongestUniValuePath longestUniValuePath = new LongestUniValuePath();
        TreeNode t1 = new TreeNode(1);
        TreeNode t2 = new TreeNode(4);
        TreeNode t3 = new TreeNode(4);
        TreeNode t4 = new TreeNode(4);
        TreeNode t5 = new TreeNode(5);
        TreeNode t6 = new TreeNode(5);

        t1.left = t2;
        t1.right = t5;
        t2.left = t3;
        t2.right = t4;
        t5.right = t6;

        System.out.println(longestUniValuePath.getLongestUniValuePath(t1));
    }
}
