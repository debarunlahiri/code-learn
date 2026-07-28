import fs from "node:fs";
import path from "node:path";

const directory = path.dirname(new URL(import.meta.url).pathname);

const bruteForceSolutions = {
    "21-combination-sum-iv.md": `public int combinationSum4(int[] nums, int target) {
    return countSequences(nums, target);
}

private int countSequences(int[] nums, int remaining) {
    if (remaining == 0) {
        return 1;
    }

    if (remaining < 0) {
        return 0;
    }

    int total = 0;
    for (int number : nums) {
        total += countSequences(nums, remaining - number);
    }
    return total;
}`,
    "27-clone-graph.md": `public Node cloneGraph(Node node) {
    if (node == null) {
        return null;
    }

    List<Node> originalNodes = new ArrayList<>();
    List<Node> clonedNodes = new ArrayList<>();
    return cloneWithLinearLookup(node, originalNodes, clonedNodes);
}

private Node cloneWithLinearLookup(
        Node node,
        List<Node> originalNodes,
        List<Node> clonedNodes
) {
    for (int i = 0; i < originalNodes.size(); i++) {
        if (originalNodes.get(i) == node) {
            return clonedNodes.get(i);
        }
    }

    Node copy = new Node(node.val, new ArrayList<>());
    originalNodes.add(node);
    clonedNodes.add(copy);

    for (Node neighbor : node.neighbors) {
        copy.neighbors.add(
                cloneWithLinearLookup(neighbor, originalNodes, clonedNodes)
        );
    }
    return copy;
}`,
    "30-number-of-islands.md": `public int numIslands(char[][] grid) {
    int rows = grid.length;
    int columns = grid[0].length;
    boolean[][] visited = new boolean[rows][columns];
    int islands = 0;

    for (int row = 0; row < rows; row++) {
        for (int column = 0; column < columns; column++) {
            if (grid[row][column] == '1' && !visited[row][column]) {
                islands++;
                markIsland(grid, visited, row, column);
            }
        }
    }
    return islands;
}

private void markIsland(
        char[][] grid,
        boolean[][] visited,
        int row,
        int column
) {
    if (row < 0
            || row >= grid.length
            || column < 0
            || column >= grid[0].length
            || grid[row][column] == '0'
            || visited[row][column]) {
        return;
    }

    visited[row][column] = true;
    markIsland(grid, visited, row + 1, column);
    markIsland(grid, visited, row - 1, column);
    markIsland(grid, visited, row, column + 1);
    markIsland(grid, visited, row, column - 1);
}`,
    "32-alien-dictionary.md": `public String alienOrder(String[] words) {
    Set<Character> characterSet = new TreeSet<>();
    for (String word : words) {
        for (char character : word.toCharArray()) {
            characterSet.add(character);
        }
    }

    List<Character> characters = new ArrayList<>(characterSet);
    boolean[] used = new boolean[characters.size()];
    StringBuilder candidate = new StringBuilder();
    return findValidOrder(words, characters, used, candidate);
}

private String findValidOrder(
        String[] words,
        List<Character> characters,
        boolean[] used,
        StringBuilder candidate
) {
    if (candidate.length() == characters.size()) {
        String order = candidate.toString();
        return wordsFollowOrder(words, order) ? order : "";
    }

    for (int i = 0; i < characters.size(); i++) {
        if (!used[i]) {
            used[i] = true;
            candidate.append(characters.get(i));

            String answer = findValidOrder(
                    words,
                    characters,
                    used,
                    candidate
            );
            if (!answer.isEmpty()) {
                return answer;
            }

            candidate.deleteCharAt(candidate.length() - 1);
            used[i] = false;
        }
    }
    return "";
}

private boolean wordsFollowOrder(String[] words, String order) {
    int[] rank = new int[26];
    for (int i = 0; i < order.length(); i++) {
        rank[order.charAt(i) - 'a'] = i;
    }

    for (int i = 1; i < words.length; i++) {
        if (compare(words[i - 1], words[i], rank) > 0) {
            return false;
        }
    }
    return true;
}

private int compare(String first, String second, int[] rank) {
    int commonLength = Math.min(first.length(), second.length());
    for (int i = 0; i < commonLength; i++) {
        char left = first.charAt(i);
        char right = second.charAt(i);
        if (left != right) {
            return Integer.compare(rank[left - 'a'], rank[right - 'a']);
        }
    }
    return Integer.compare(first.length(), second.length());
}`,
    "33-graph-valid-tree.md": `public boolean validTree(int n, int[][] edges) {
    if (edges.length != n - 1) {
        return false;
    }

    boolean[][] connected = new boolean[n][n];
    for (int[] edge : edges) {
        connected[edge[0]][edge[1]] = true;
        connected[edge[1]][edge[0]] = true;
    }

    boolean[] visited = new boolean[n];
    visit(0, connected, visited);

    for (boolean wasVisited : visited) {
        if (!wasVisited) {
            return false;
        }
    }
    return true;
}

private void visit(int node, boolean[][] connected, boolean[] visited) {
    visited[node] = true;
    for (int neighbor = 0; neighbor < connected.length; neighbor++) {
        if (connected[node][neighbor] && !visited[neighbor]) {
            visit(neighbor, connected, visited);
        }
    }
}`,
    "34-number-of-connected-components.md": `public int countComponents(int n, int[][] edges) {
    boolean[][] connected = new boolean[n][n];
    for (int[] edge : edges) {
        connected[edge[0]][edge[1]] = true;
        connected[edge[1]][edge[0]] = true;
    }

    boolean[] visited = new boolean[n];
    int components = 0;
    for (int node = 0; node < n; node++) {
        if (!visited[node]) {
            components++;
            visitComponent(node, connected, visited);
        }
    }
    return components;
}

private void visitComponent(
        int node,
        boolean[][] connected,
        boolean[] visited
) {
    visited[node] = true;
    for (int neighbor = 0; neighbor < connected.length; neighbor++) {
        if (connected[node][neighbor] && !visited[neighbor]) {
            visitComponent(neighbor, connected, visited);
        }
    }
}`,
    "35-insert-interval.md": `public int[][] insert(int[][] intervals, int[] newInterval) {
    List<int[]> allIntervals = new ArrayList<>();
    for (int[] interval : intervals) {
        allIntervals.add(new int[] {interval[0], interval[1]});
    }
    allIntervals.add(new int[] {newInterval[0], newInterval[1]});
    allIntervals.sort(Comparator.comparingInt(interval -> interval[0]));

    List<int[]> merged = new ArrayList<>();
    for (int[] interval : allIntervals) {
        if (merged.isEmpty()
                || merged.get(merged.size() - 1)[1] < interval[0]) {
            merged.add(interval);
        } else {
            int[] previous = merged.get(merged.size() - 1);
            previous[1] = Math.max(previous[1], interval[1]);
        }
    }
    return merged.toArray(new int[merged.size()][]);
}`,
    "37-non-overlapping-intervals.md": `public int eraseOverlapIntervals(int[][] intervals) {
    return intervals.length - largestCompatibleSubset(
            intervals,
            0,
            new ArrayList<>()
    );
}

private int largestCompatibleSubset(
        int[][] intervals,
        int index,
        List<int[]> selected
) {
    if (index == intervals.length) {
        return selected.size();
    }

    int withoutCurrent = largestCompatibleSubset(
            intervals,
            index + 1,
            selected
    );

    int withCurrent = 0;
    if (doesNotOverlap(intervals[index], selected)) {
        selected.add(intervals[index]);
        withCurrent = largestCompatibleSubset(
                intervals,
                index + 1,
                selected
        );
        selected.remove(selected.size() - 1);
    }
    return Math.max(withoutCurrent, withCurrent);
}

private boolean doesNotOverlap(int[] candidate, List<int[]> selected) {
    for (int[] interval : selected) {
        if (candidate[0] < interval[1] && interval[0] < candidate[1]) {
            return false;
        }
    }
    return true;
}`,
    "38-meeting-rooms.md": `public boolean canAttendMeetings(int[][] intervals) {
    for (int i = 0; i < intervals.length; i++) {
        for (int j = i + 1; j < intervals.length; j++) {
            boolean overlaps = intervals[i][0] < intervals[j][1]
                    && intervals[j][0] < intervals[i][1];
            if (overlaps) {
                return false;
            }
        }
    }
    return true;
}`,
    "47-spiral-matrix.md": `public List<Integer> spiralOrder(int[][] matrix) {
    int rows = matrix.length;
    int columns = matrix[0].length;
    boolean[][] visited = new boolean[rows][columns];
    int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    List<Integer> result = new ArrayList<>();

    int row = 0;
    int column = 0;
    int direction = 0;

    for (int count = 0; count < rows * columns; count++) {
        result.add(matrix[row][column]);
        visited[row][column] = true;

        int nextRow = row + directions[direction][0];
        int nextColumn = column + directions[direction][1];
        if (nextRow < 0
                || nextRow >= rows
                || nextColumn < 0
                || nextColumn >= columns
                || visited[nextRow][nextColumn]) {
            direction = (direction + 1) % directions.length;
            nextRow = row + directions[direction][0];
            nextColumn = column + directions[direction][1];
        }

        row = nextRow;
        column = nextColumn;
    }
    return result;
}`,
    "49-word-search.md": `public boolean exist(char[][] board, String word) {
    for (int row = 0; row < board.length; row++) {
        for (int column = 0; column < board[0].length; column++) {
            boolean[][] visited =
                    new boolean[board.length][board[0].length];
            if (search(board, word, row, column, 0, visited)) {
                return true;
            }
        }
    }
    return false;
}

private boolean search(
        char[][] board,
        String word,
        int row,
        int column,
        int index,
        boolean[][] visited
) {
    if (index == word.length()) {
        return true;
    }
    if (row < 0
            || row >= board.length
            || column < 0
            || column >= board[0].length
            || visited[row][column]
            || board[row][column] != word.charAt(index)) {
        return false;
    }

    visited[row][column] = true;
    boolean found = search(board, word, row + 1, column, index + 1, visited)
            || search(board, word, row - 1, column, index + 1, visited)
            || search(board, word, row, column + 1, index + 1, visited)
            || search(board, word, row, column - 1, index + 1, visited);
    visited[row][column] = false;
    return found;
}`,
    "55-valid-parentheses.md": `public boolean isValid(String text) {
    String previous;
    do {
        previous = text;
        text = text.replace("()", "")
                .replace("[]", "")
                .replace("{}", "");
    } while (!text.equals(previous));

    return text.isEmpty();
}`,
    "60-maximum-depth-of-binary-tree.md": `public int maxDepth(TreeNode root) {
    if (root == null) {
        return 0;
    }

    Queue<TreeNode> queue = new ArrayDeque<>();
    queue.offer(root);
    int depth = 0;

    while (!queue.isEmpty()) {
        int levelSize = queue.size();
        depth++;

        for (int i = 0; i < levelSize; i++) {
            TreeNode node = queue.poll();
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
        }
    }
    return depth;
}`,
    "61-same-tree.md": `public boolean isSameTree(TreeNode first, TreeNode second) {
    return serialize(first).equals(serialize(second));
}

private String serialize(TreeNode node) {
    if (node == null) {
        return "#";
    }
    return node.val
            + ","
            + serialize(node.left)
            + ","
            + serialize(node.right);
}`,
    "62-invert-binary-tree.md": `public TreeNode invertTree(TreeNode root) {
    return createInvertedCopy(root);
}

private TreeNode createInvertedCopy(TreeNode node) {
    if (node == null) {
        return null;
    }

    TreeNode copy = new TreeNode(node.val);
    copy.left = createInvertedCopy(node.right);
    copy.right = createInvertedCopy(node.left);
    return copy;
}`,
    "63-binary-tree-maximum-path-sum.md": `public int maxPathSum(TreeNode root) {
    if (root == null) {
        return Integer.MIN_VALUE;
    }

    int throughRoot = root.val
            + Math.max(0, bestDownwardPath(root.left))
            + Math.max(0, bestDownwardPath(root.right));

    return Math.max(
            throughRoot,
            Math.max(maxPathSum(root.left), maxPathSum(root.right))
    );
}

private int bestDownwardPath(TreeNode node) {
    if (node == null) {
        return 0;
    }
    return node.val + Math.max(
            0,
            Math.max(
                    bestDownwardPath(node.left),
                    bestDownwardPath(node.right)
            )
    );
}`,
    "64-binary-tree-level-order-traversal.md": `public List<List<Integer>> levelOrder(TreeNode root) {
    List<List<Integer>> levels = new ArrayList<>();
    int height = treeHeight(root);

    for (int level = 1; level <= height; level++) {
        List<Integer> values = new ArrayList<>();
        collectLevel(root, level, values);
        levels.add(values);
    }
    return levels;
}

private int treeHeight(TreeNode node) {
    if (node == null) {
        return 0;
    }
    return 1 + Math.max(treeHeight(node.left), treeHeight(node.right));
}

private void collectLevel(
        TreeNode node,
        int level,
        List<Integer> values
) {
    if (node == null) {
        return;
    }
    if (level == 1) {
        values.add(node.val);
        return;
    }
    collectLevel(node.left, level - 1, values);
    collectLevel(node.right, level - 1, values);
}`,
    "65-serialize-and-deserialize-binary-tree.md": `public String serialize(TreeNode root) {
    if (root == null) {
        return "#";
    }
    return root.val
            + ","
            + serialize(root.left)
            + ","
            + serialize(root.right);
}

public TreeNode deserialize(String data) {
    List<String> values = new ArrayList<>(
            Arrays.asList(data.split(","))
    );
    return buildTree(values);
}

private TreeNode buildTree(List<String> values) {
    String value = values.remove(0);
    if (value.equals("#")) {
        return null;
    }

    TreeNode node = new TreeNode(Integer.parseInt(value));
    node.left = buildTree(values);
    node.right = buildTree(values);
    return node;
}`,
    "66-subtree-of-another-tree.md": `public boolean isSubtree(TreeNode root, TreeNode subRoot) {
    String tree = serialize(root);
    String candidate = serialize(subRoot);
    return tree.contains(candidate);
}

private String serialize(TreeNode node) {
    if (node == null) {
        return ",#";
    }
    return ",N"
            + node.val
            + serialize(node.left)
            + serialize(node.right);
}`,
    "67-construct-binary-tree-from-preorder-and-inorder.md": `private int preorderIndex;

public TreeNode buildTree(int[] preorder, int[] inorder) {
    preorderIndex = 0;
    return build(preorder, inorder, 0, inorder.length - 1);
}

private TreeNode build(
        int[] preorder,
        int[] inorder,
        int inorderStart,
        int inorderEnd
) {
    if (inorderStart > inorderEnd) {
        return null;
    }

    int rootValue = preorder[preorderIndex++];
    int inorderRootIndex = inorderStart;
    while (inorder[inorderRootIndex] != rootValue) {
        inorderRootIndex++;
    }

    TreeNode root = new TreeNode(rootValue);
    root.left = build(
            preorder,
            inorder,
            inorderStart,
            inorderRootIndex - 1
    );
    root.right = build(
            preorder,
            inorder,
            inorderRootIndex + 1,
            inorderEnd
    );
    return root;
}`,
    "68-validate-binary-search-tree.md": `public boolean isValidBST(TreeNode root) {
    List<Integer> values = new ArrayList<>();
    inorder(root, values);

    for (int i = 1; i < values.size(); i++) {
        if (values.get(i) <= values.get(i - 1)) {
            return false;
        }
    }
    return true;
}

private void inorder(TreeNode node, List<Integer> values) {
    if (node == null) {
        return;
    }
    inorder(node.left, values);
    values.add(node.val);
    inorder(node.right, values);
}`,
    "70-lowest-common-ancestor-of-a-bst.md": `public TreeNode lowestCommonAncestor(
        TreeNode root,
        TreeNode first,
        TreeNode second
) {
    List<TreeNode> firstPath = pathTo(root, first.val);
    List<TreeNode> secondPath = pathTo(root, second.val);
    TreeNode answer = null;

    int commonLength = Math.min(firstPath.size(), secondPath.size());
    for (int i = 0; i < commonLength; i++) {
        if (firstPath.get(i) != secondPath.get(i)) {
            break;
        }
        answer = firstPath.get(i);
    }
    return answer;
}

private List<TreeNode> pathTo(TreeNode root, int target) {
    List<TreeNode> path = new ArrayList<>();
    TreeNode current = root;

    while (current != null) {
        path.add(current);
        if (target == current.val) {
            break;
        }
        current = target < current.val ? current.left : current.right;
    }
    return path;
}`,
    "71-implement-trie.md": `class Trie {
    private final List<String> words = new ArrayList<>();

    public void insert(String word) {
        if (!words.contains(word)) {
            words.add(word);
        }
    }

    public boolean search(String word) {
        return words.contains(word);
    }

    public boolean startsWith(String prefix) {
        for (String word : words) {
            if (word.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }
}`,
    "72-add-and-search-word.md": `class WordDictionary {
    private final List<String> words = new ArrayList<>();

    public void addWord(String word) {
        words.add(word);
    }

    public boolean search(String pattern) {
        for (String word : words) {
            if (matches(word, pattern)) {
                return true;
            }
        }
        return false;
    }

    private boolean matches(String word, String pattern) {
        if (word.length() != pattern.length()) {
            return false;
        }

        for (int i = 0; i < word.length(); i++) {
            char expected = pattern.charAt(i);
            if (expected != '.' && expected != word.charAt(i)) {
                return false;
            }
        }
        return true;
    }
}`,
    "73-word-search-ii.md": `public List<String> findWords(char[][] board, String[] words) {
    List<String> foundWords = new ArrayList<>();
    for (String word : words) {
        if (exists(board, word)) {
            foundWords.add(word);
        }
    }
    return foundWords;
}

private boolean exists(char[][] board, String word) {
    for (int row = 0; row < board.length; row++) {
        for (int column = 0; column < board[0].length; column++) {
            if (search(board, word, row, column, 0)) {
                return true;
            }
        }
    }
    return false;
}

private boolean search(
        char[][] board,
        String word,
        int row,
        int column,
        int index
) {
    if (index == word.length()) {
        return true;
    }
    if (row < 0
            || row >= board.length
            || column < 0
            || column >= board[0].length
            || board[row][column] != word.charAt(index)) {
        return false;
    }

    char saved = board[row][column];
    board[row][column] = '#';
    boolean found = search(board, word, row + 1, column, index + 1)
            || search(board, word, row - 1, column, index + 1)
            || search(board, word, row, column + 1, index + 1)
            || search(board, word, row, column - 1, index + 1);
    board[row][column] = saved;
    return found;
}`,
};

for (const [fileName, bruteForceCode] of Object.entries(bruteForceSolutions)) {
    const filePath = path.join(directory, fileName);
    let document = fs.readFileSync(filePath, "utf8");
    const firstCodeBlock = /```java\n[\s\S]*?```/;

    if (!firstCodeBlock.test(document)) {
        throw new Error(`No Java block found in ${fileName}`);
    }

    document = document.replace(
        firstCodeBlock,
        `\`\`\`java\n${bruteForceCode}\n\`\`\``,
    );
    document = document.replace(
        /### Brute-Force Java \/ Optimal[^\n]*/,
        "### Brute-Force Java",
    );
    document = document.replace(
        /The traversal shown above already has the best possible asymptotic bound[\s\S]*?duplicate work\./,
        "The optimized implementation removes unnecessary repeated searches or extra copies while preserving the same result.",
    );
    fs.writeFileSync(filePath, document);
}

const houseRobberFile = path.join(directory, "23-house-robber-ii.md");
let houseRobberDocument = fs.readFileSync(houseRobberFile, "utf8");
houseRobberDocument = houseRobberDocument.replace(
    /```java\n\/\/ Identical to brute force above[\s\S]*?```/,
    `\`\`\`java
public int rob(int[] houses) {
    if (houses.length == 1) {
        return houses[0];
    }

    return Math.max(
            robLinearRange(houses, 0, houses.length - 2),
            robLinearRange(houses, 1, houses.length - 1)
    );
}

private int robLinearRange(int[] houses, int start, int end) {
    int twoHousesBack = 0;
    int oneHouseBack = 0;

    for (int i = start; i <= end; i++) {
        int current = Math.max(
                oneHouseBack,
                twoHousesBack + houses[i]
        );
        twoHousesBack = oneHouseBack;
        oneHouseBack = current;
    }
    return oneHouseBack;
}
\`\`\``,
);
houseRobberDocument = houseRobberDocument.replace(
    /### Optimized Java[^\n]*/,
    "### Optimized Java — O(n) time, O(1) extra space",
);
fs.writeFileSync(houseRobberFile, houseRobberDocument);
