import java.util.ArrayList;
import java.util.Scanner;

public class AVLTree {
    private Node root;
    private AVLFormatter formatter; // Adicionando o formatter

    public AVLTree() {
        root = null;
        formatter = new AVLFormatter(); // Inicializando o formatter
    }

    // Adicionar elementos na árvore AVL
    public void add(int value) {
        Node node = new Node(value);
        root = addAVL(root, node);
    }

    private Node addAVL(Node current, Node node) {
        if (current == null)
            return node;

        if (node.element < current.element)
            current.left = addAVL(current.left, node);

        else if (node.element > current.element)
            current.right = addAVL(current.right, node);

        else
            return current;

        updateHeight(current);
        return balance(current);
    }

    // Verificar se um determinado valor está na árvore AVL
    public boolean contains(int value) {
        return containsNode(root, value);
    }

    private boolean containsNode(Node current, int value) {
        if (current == null)
            return false;

        if (value == current.element)
            return true;

        if (value < current.element)
            return containsNode(current.left, value);

        else
            return containsNode(current.right, value);
    }

    // Limpar o conteúdo da árvore AVL
    public void clear() {
        root = null;
    }

    // Verificar se a árvore AVL está vazia
    public boolean isEmpty() {
        return root == null;
    }

    // Obter a altura da árvore AVL
    public int height() {
        return height(root);
    }

    private int height(Node node) {
        if (node == null)
            return -1;

        else
            return node.height;
    }

    // Obter o número de elementos na árvore AVL
    public int size() {
        return countNodes(root);
    }

    private int countNodes(Node node) {
        if (node == null)
            return 0;

        return 1 + countNodes(node.left) + countNodes(node.right);
    }

    // Retornar os elementos da árvore AVL em ordem usando o caminhamento central
    public ArrayList<Integer> inOrder() {
        ArrayList<Integer> erd = new ArrayList<>();
        inOrderTraversal(root, erd);
        return erd;
    }

    private void inOrderTraversal(Node node, ArrayList<Integer> erd) {
        if (node != null) {
            inOrderTraversal(node.left, erd);
            erd.add(node.element);
            inOrderTraversal(node.right, erd);
        }
    }

    // Obter o pai de um elemento na árvore AVL
    public Integer getParent(int value) {
        Node parent = findParent(root, value);
        return parent == null ? null : parent.element;
    }

    private Node findParent(Node current, int value) {
        if (current == null || current.element == value)
            return null;

        if ((current.left != null && current.left.element == value) || (current.right != null && current.right.element == value)) {
            return current;
        }

        if (value < current.element)
            return findParent(current.left, value);

        else
            return findParent(current.right, value);
    }

    // Remover um elemento da árvore AVL
    public boolean remove(int value) {
        Node[] nodes = removeAVL(root, value);
        root = nodes[0];
        return nodes[1] != null;
    }

    private Node[] removeAVL(Node current, int value) {
        if (current == null)
            return new Node[]{null, null};

        Node removed;

        if (value < current.element) {
            Node[] nodes = removeAVL(current.left, value);
            current.left = nodes[0];
            removed = nodes[1];
        } else if (value > current.element) {
            Node[] nodes = removeAVL(current.right, value);
            current.right = nodes[0];
            removed = nodes[1];
        } else {
            removed = current;
            if (current.left == null)
                return new Node[]{current.right, current};

            else if (current.right == null)
                return new Node[]{current.left, current};

            else {
                Node successor = findMin(current.right);
                current.element = successor.element;
                Node[] nodes = removeAVL(current.right, successor.element);
                current.right = nodes[0];
            }
        }

        updateHeight(current);
        return new Node[]{balance(current), removed};
    }

    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private Node balance(Node node) {
        int balanceFactor = getBalance(node);

        if (balanceFactor > 1) {
            if (getBalance(node.left) >= 0)
                node = rotateRight(node);
            else {
                node.left = rotateLeft(node.left);
                node = rotateRight(node);
            }
        } else if (balanceFactor < -1) {
            if (getBalance(node.right) <= 0)
                node = rotateLeft(node);
            else {
                node.right = rotateRight(node.right);
                node = rotateLeft(node);
            }
        }
        return node;
    }

    private void updateHeight(Node node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    private int getBalance(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        y.left = x.right;
        x.right = y;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private Node rotateLeft(Node y) {
        Node x = y.right;
        y.right = x.left;
        x.left = y;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    // Imprimir os elementos da árvore AVL em ordem
    public void printInOrder() {
        ArrayList<Integer> inOrderList = inOrder();
        System.out.println("Elementos da árvore AVL em ordem:");
        for (Integer element : inOrderList) {
            System.out.print(element + " ");
        }
        System.out.println();
    }

    // Imprimir a árvore AVL formatada
    public void printFormattedTree() {
        if (root == null) {
            System.out.println("Árvore vazia!");
            return;
        }

        String formattedTree = formatter.topDown(root);
        System.out.println("Árvore AVL formatada:");
        System.out.println(formattedTree);
    }

    // Mostrar informações sobre a árvore AVL
    public void treeInfo() {
        System.out.println("Informações da Árvore AVL:");
        System.out.println("Altura da árvore: " + height());
        System.out.println("Número de elementos na árvore: " + size());
        System.out.println("Elementos em ordem:");
        printInOrder();
    }

    // Testar a árvore AVL com casos específicos
    public void testAVL() {
        AVLTree testTree = new AVLTree();

        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        for (int num : numbers) {
            testTree.add(num);
        }

        testTree.printFormattedTree();

        testTree.clear();
        System.out.println("Árvore após limpar:");
        testTree.printFormattedTree();

        int[] numbersReverse = {9, 8, 7, 6, 5, 4, 3, 2, 1};
        for (int num : numbersReverse) {
            testTree.add(num);
        }

        testTree.printFormattedTree();
    }

    // Classe para representar um nó na árvore AVL
    static class Node {
        int element, height;
        Node left, right;

        Node(int element) {
            this(element, null, null);
        }

        Node(int element, Node left, Node right) {
            this.element = element;
            this.left = left;
            this.right = right;
            height = 0;
        }
    }

    // Método main para testar a árvore AVL
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        AVLTree avlTree = new AVLTree();
        int op, value;

        do {
            System.out.println("\nÁRVORE AVL");
            System.out.println("---------------------------------------");
            System.out.println("1 | Adicionar nodo");
            System.out.println("2 | Remover nodo");
            System.out.println("3 | Pesquisar nodo");
            System.out.println("4 | Exibir a árvore");
            System.out.println("5 | Mostrar informações");
            System.out.println("6 | Testar árvore");
            System.out.println("8 | Esvaziar árvore");
            System.out.println("0 | Sair do programa");
            System.out.println("---------------------------------------");
            System.out.print("Digite a opção desejada: ");

            op = scan.nextInt();
            switch (op) {
                case 1 -> {
                    System.out.print("Informe um valor inteiro: ");
                    value = scan.nextInt();
                    avlTree.add(value);
                }
                case 2 -> {
                    System.out.print("Informe um valor inteiro: ");
                    value = scan.nextInt();

                    if (!avlTree.remove(value))
                        System.out.println("Valor não encontrado!");
                }
                case 3 -> {
                    System.out.print("Informe um valor inteiro: ");
                    value = scan.nextInt();

                    if (avlTree.contains(value))
                        System.out.println("Valor encontrado!");
                    else
                        System.out.println("Valor não encontrado!");
                }
                case 4 -> {
                    avlTree.printFormattedTree();
                }
                case 5 -> {
                    avlTree.treeInfo();
                }
                case 6 -> {
                    avlTree.testAVL();
                }
                case 8 -> {
                    avlTree.clear();
                }
            }
        } while (op != 0);

        scan.close();
    }
}