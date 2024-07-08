// Imports
import java.util.ArrayList;
import java.util.Scanner;

public class AVLTree {
    private Node root;
    private AVLFormatter formatter;

    public AVLTree() {
        root = null;
        formatter = new AVLFormatter();
    }

    /** Adicionar elementos na árvore */
    public void add(int value) {
        Node node = new Node(value);
        root = addAVL(root, node);
    }

    // Método auxiliar pro add
    private Node addAVL(Node current, Node node) {
        if (current == null)
            // Se encontrar um nó nulo, adiciona o novo nó nele
            return node;

        if (node.element < current.element)
            // se o valor for menor, adiciona a esquerda
            current.left = addAVL(current.left, node);

        else if (node.element > current.element)
            // se o valor é maior, adiciona a direita
            current.right = addAVL(current.right, node);

        else
            // se o valor já existir, ele retorna o atual
            return current;

        // atualizando a altura do nó atual
        updateHeight(current);
        // balanceando a arvore após adicionar o nodo
        return balance(current);
    }

    /** Retornar o pai de um elemento */
    public Integer getParent(int value) {
        Node parent = findParent(root, value);
        // retorna o pai do elemento ou nulo (se não achar)
        return parent == null ? null : parent.element;
    }

    // Método auxiliar do getParent
    private Node findParent(Node current, int value) {
        if (current == null || current.element == value)
            return null;

        if ((current.left != null && current.left.element == value) || (current.right != null && current.right.element == value)) {
            // retorna o pai atual se o filho esquerdo/direito tiver o valor
            return current;
        }

        if (value < current.element)
            // percorre a subarevore esquerda se o valor for < que o do nó atual
            return findParent(current.left, value);

        else
            // procura na subarvore dirieta se o valor é >= ao do nó atual
            return findParent(current.right, value);
    }

    /** Limpar o conteúdo da árvore */
    public void clear() {
        root = null;
    }

    /** Verificar se um elemento está armazenado na árvore ou não */
    public boolean contains(int value) {
        return containsNode(root, value);
    }

    // Método auxiliar do contains
    private boolean containsNode(Node current, int value) {
        if (current == null)
            // nó nulo == valor não está na arvore
            return false;

        if (value == current.element)
            // retorna true se achar o valor
            return true;

        if (value < current.element)
            // se o valor é menor que o nó atual, vai para a esquerda
            return containsNode(current.left, value);

        else
            // se o valor é maior que o nó atual, vai para a direita
            return containsNode(current.right, value);
    }

    /** Verificar qual é a altura da árvore */
    public int height() {
        return height(root);
    }

    // Método auxiliar do height
    private int height(Node node) {
        if (node == null)
            // Retorna -1 se o nó for nulo
            return -1;
        else
            return node.height;
    }

    /** Verificar quantos elementos tem na árvore */
    public int size() {
        return countNodes(root);
    }

    private int countNodes(Node node) {
        if (node == null)
            return 0;

        // conta os nós da subarvore esquerda, direita e o próprio nó
        return 1 + countNodes(node.left) + countNodes(node.right);
    }

    /** Verificar se a árvore está vazia ou não */
    public boolean isEmpty() {
        return root == null;
    }

    /** Retornar os elementos da árvore em uma lista usando caminhamento central */
    public ArrayList<Integer> inOrder() {
        ArrayList<Integer> erd = new ArrayList<>();
        inOrderTraversal(root, erd);
        return erd;
    }

    private void inOrderTraversal(Node node, ArrayList<Integer> erd) {
        if (node != null) {
            // percorre a subarvore esquerda
            inOrderTraversal(node.left, erd);

            // adiciona o elemento atual na lista
            erd.add(node.element);

            // percorre a subarvore direita
            inOrderTraversal(node.right, erd);
        }
    }

    // ------------- Métodos Adicionados ------------- //

    public boolean remove(int value) {
        // remove o elemento
        Node[] nodes = removeAVL(root, value);

        // atualiza a raiz
        root = nodes[0];

        // retorna true se for removido com sucesso
        return nodes[1] != null;
    }

    // Método auxiliar do remove
    private Node[] removeAVL(Node current, int value) {
        // retorna o array nulo (não encotrou o valor na arvore)
        if (current == null)
            return new Node[]{null, null};

        Node removed;

        // remove da subarvore esquerda se o valor é < que o nó atual
        if (value < current.element) {
            Node[] nodes = removeAVL(current.left, value);

            // atualiza a subarvore esquerda
            current.left = nodes[0];

            // guarda o nó removido
            removed = nodes[1];
        }

        // remove da subarvore direita se o valor é > que o nó atual
        else if (value > current.element) {
            Node[] nodes = removeAVL(current.right, value);
            current.right = nodes[0];
            removed = nodes[1];
        }

        // se achar o valor a ser removido
        else {
            // guarda o nó que vai ser removido
            removed = current;

            // se a subarvore esquerda for nula, retorna a subarvore direita
            if (current.left == null)
                return new Node[]{current.right, current};

            // se a subarvore direita for nula, retorna a subarvore esquerda
            else if (current.right == null)
                return new Node[]{current.left, current};

            // se nada der certo, encontra o sucessor na subarvore direita para substituir o nó removido
            else {
                Node successor = findMin(current.right);
                // substitui o elemento atual pelo do sucessor
                current.element = successor.element;

                // atualiza a subarvore direita
                Node[] nodes = removeAVL(current.right, successor.element);
                current.right = nodes[0];
            }
        }
        // atualiza a altura do nó atual
        updateHeight(current);
        // retorna o nó balanceado e o nó removido
        return new Node[]{balance(current), removed};
    }

    private Node findMin(Node node) {
        // percorre até encontrar o menor valor da subarvore esquerda
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private Node balance(Node node) {
        // calculando o FB do nó (FB = altura(esquerda) - altura(direita))
        int balanceFactor = getBalance(node);

        // verifica se desbalanceou a esquerda
        if (balanceFactor > 1) {
            if (getBalance(node.left) >= 0)
                // rotaciona pra direita
                node = rotateRight(node);

            else {
                // rotaciona pra esquerda na subarvore esquerda
                node.left = rotateLeft(node.left);
                // rotaciona a direita o nó atual
                node = rotateRight(node);
            }
        }

        // verifica se desbalanceou a direita
        else if (balanceFactor < -1) {
            if (getBalance(node.right) <= 0)
                // rotaciona a esquerda
                node = rotateLeft(node);

            else {
                // rotaciona a direita na subarvore direita
                node.right = rotateRight(node.right);
                // rotaciona a esquerda no nó atual
                node = rotateLeft(node);
            }
        }
        return node;
    }

    // Método auxilair pra att. a altura do nó
    private void updateHeight(Node node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    private int getBalance(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private Node rotateRight(Node y) {
        // x = filho esquerdo de y
        Node x = y.left;

        // filho direito de X = filho esquerdo de Y
        y.left = x.right;

        // y = filho direito de X
        x.right = y;

        // att. altura do y e x
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private Node rotateLeft(Node y) {
        // x = filho direito de y
        Node x = y.right;

        // filho esquerdo de x = filho dirieto de y
        y.right = x.left;

        // y = filho esquerdo de x
        x.left = y;

        // att. altura de y e x
        updateHeight(y);
        updateHeight(x);
        return x;
    }


    // Imprimir a arvore ERD
    public void printInOrder() {
        ArrayList<Integer> inOrderList = inOrder();
        System.out.println("Elementos da árvore AVL em ordem:");
        for (Integer element : inOrderList) {
            System.out.print(element + " , ");
        }
        System.out.println();
    }

    // Formatando a arvore
    public void printFormattedTree() {
        if (root == null) {
            System.out.println("Árvore vazia!");
            return;
        }
        String formattedTree = formatter.topDown(root);
        System.out.println(formattedTree);
    }

    // Mostrar informações
    public void treeInfo() {
        System.out.println("Informações da Árvore AVL:");
        System.out.println("Altura da árvore: " + height());
        System.out.println("Número de elementos na árvore: " + size());
        System.out.println("Elementos em ordem:");
        printInOrder();
    }

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

    // Implemente um método main que instancie a árvore implementada e chame os seus métodos para verificar o correto funcionamento;
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // Instanciar a árvore implementada;
        AVLTree avlTree = new AVLTree();
        int op, value;

        do {
            System.out.println("\nÁRVORE AVL\n" +
                    "---------------------------------------\n" +
                    "01 | Adicionar nodo\n" +
                    "02 | Remover nodo\n" +
                    "03 | Pesquisar nodo\n" +
                    "04 | Exibir árvore\n" +
                    "05 | Mostrar informações\n" +
                    "06 | Testar árvore\n" +
                    "07 | Imprimir elementos em ordem (ERD)\n" +
                    "08 | Limpar árvore\n" +
                    "09 | Mostrar o pai de um elemento\n" +
                    "10 | Imprimir árvore\n" +
                    "0 | Sair\n" +
                    "---------------------------------------\n" +
                    "Escolha uma opção: ");
            op = in.nextInt();

            switch (op) {
                case 1 -> {
                    System.out.println("Digite um valor (inteiro) a ser adicionado: ");
                    value = in.nextInt();

                    avlTree.add(value);
                    System.out.println("O valor " + value + " foi adicionado com sucesso!");
                }

                case 2 -> {
                    System.out.print("Digite o valor a ser removido: ");
                    value = in.nextInt();

                    if (avlTree.remove(value))
                        System.out.println("Valor removido!");
                    else
                        System.out.println("Valor não encontrado na árvore.");
                }

                case 3 -> {
                    System.out.println("Digite o valor (inteiro) que deseja pesquisar: ");
                    value = in.nextInt();

                    if (avlTree.contains(value))
                        System.out.println("O valor " + value + " foi encontrado na árvore!!");
                    else
                        System.out.println("O valor " + value + " não foi encontrado :c.");
                }

                case 4 -> {
                    avlTree.printFormattedTree();
                }

                case 5 -> {
                    avlTree.treeInfo();
                }

                case 6 -> {
                    // Adicionar números em ordem crescente
                    int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9};
                    for (int num : numbers)
                        avlTree.add(num);

                    System.out.println("Árvore após adicionar números 1-9:");
                    avlTree.printFormattedTree();

                    avlTree.clear();
                    System.out.println("Árvore após limpar:");
                    avlTree.printFormattedTree();

                    int[] numbersReverse = {9, 8, 7, 6, 5, 4, 3, 2, 1};
                    for (int num : numbersReverse)
                        avlTree.add(num);

                    System.out.println("Árvore após adicionar números 9-1:");
                    avlTree.printFormattedTree();
                }
                case 7 -> {
                    avlTree.printInOrder();
                }
                case 8 -> {
                    avlTree.clear();
                    System.out.println("A árvore foi limpa!");
                }
                case 0 -> {
                    System.out.println("Saindo... :(");
                }
            }
        } while (op != 0);
        in.close();
    }
}