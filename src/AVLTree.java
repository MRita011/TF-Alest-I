import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class AVLTree {
    private Node root;

    public AVLTree() {
        root = null;
    }

    // Adicionar elementos na árvore
    public void add(int value) {
        Node node = new Node(value);
        root = addAVL(root, node);
    }

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
            return current; // se o valor já existir, ele retorna o atual

        // atualizando a altura do nó atual
        updateHeight(current);
        // balanceando a arvore após adicionar o nodo
        return balance(current);
    }

    // verifica se um determinado valor está contido na arvore
    public boolean contains(int value) {
        return containsNode(root, value);
    }

    // Método auxiliar
    private boolean containsNode(Node current, int value) {
        if (current == null)
            // nó nulo == valor não está na arvore
            return false;

        // retorna true se achar o valor
        if (value == current.element)
            return true;

        // se o valor é menor que o nó atual, vai para a esquerda
        if (value < current.element)
            return containsNode(current.left, value);

        else
            // se o valor é maior que o nó atual, vai para a direita
            return containsNode(current.right, value);
    }

    // Limpar o conteúdo da árvore
    public void clear() {
        root = null;
    }

    // Verificar se a árvore está vazia ou não
    public boolean isEmpty() {
        return root == null;
    }

    // Verificar qual é a altura da árvore
    public int height() {
        return height(root);
    }
    // Método auxiliar
    private int height(Node node) {
        if (node == null)
            // Retorna -1 se o nó for nulo
            return -1;

        else
            return node.height;
    }

    // Verificar quantos elementos tem na árvore
    public int size() {
        return countNodes(root);
    }
    private int countNodes(Node node) {
        if (node == null)
            return 0; // 0 se o nó for nulo

        // conta os nós da subarvore esquerda, direita e o próprio nó
        return 1 + countNodes(node.left) + countNodes(node.right);
    }

    // Retornar os elementos da árvore em uma lista usando caminhamento central
    public ArrayList<Integer> inOrder() {
        ArrayList<Integer> erd = new ArrayList<>();
        caminhamentoCentral(root, erd);
        return erd;
    }

    // Métodos auxiliares para ver se a arvore esta balanceada
    private boolean isBalanced() {
        return isBalanced(root);
    }

    private boolean isBalanced(Node node) {
        if (node == null)
            return true;

        int balanceFactor = getBalance(node);
        if (Math.abs(balanceFactor) > 1)
            return false;

        return isBalanced(node.left) && isBalanced(node.right);
    }

    public void treeInfo() {
        System.out.println("Informações da árvore:");
        System.out.println("Altura da árvore: " + height());
        System.out.println("Número de elementos: " + size());
        System.out.println("Balanceamento da árvore: " + (isBalanced() ? "Balanceada" : "Desbalanceada"));
    }

    // método para mostrar a arvpre
    public void printTree() {
        if (root == null) {
            System.out.println("Árvore vazia!");
            return;
        }

        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        System.out.println("Visualização da árvore AVL:");

        while (!queue.isEmpty()) {
            int levelSize = queue.size();

            for (int i = 0; i < levelSize; i++) {
                Node current = queue.poll();
                System.out.print(current.element + " ");

                if (current.left != null)
                    queue.add(current.left);

                if (current.right != null)
                    queue.add(current.right);
            }
            System.out.println(); // linha para o próximo nível
        }
    }

    private void caminhamentoCentral(Node node, ArrayList<Integer> erd) {
        if (node != null) {
            // percorre a subarvore esquerda
            caminhamentoCentral(node.left, erd);

            // adiciona o elemento atual na lista
            erd.add(node.element);

            // percorre a subarvore direita
            caminhamentoCentral(node.right, erd);
        }
    }

    // Retornar o pai de um elemento
    public Integer getParent(int value) {
        Node parent = findParent(root, value);
        // retorna o pai do elemento ou nulo (se não achar)
        return parent == null ? null : parent.element;
    }

    // Método auxiliar
    private Node findParent(Node current, int value) {
        if (current == null || current.element == value)
            return null;

        if (current.left != null && current.left.element == value || current.right != null && current.right.element == value) {
            // retorna o pai atual se o filho esquerdo/direito tiver o valor
            return current;
        }

        // percorre a subarevore esquerda se o valor for < que o do nó atual
        if (value < current.element)
            return findParent(current.left, value);

            // procura na subarvore dirieta se o valor é >= ao do nó atual
        else
            return findParent(current.right, value);
    }

    // Remover elementos da árvore
    public boolean remove(int value) {
        // remove o elemento
        Node[] nodes = removeAVL(root, value);

        // atualiza a raiz
        root = nodes[0];

        // retorna true se for removido com sucesso
        return nodes[1] != null;
    }

    // Método auxiliar
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
        // retorna o nó balanceado e o nó removido
        updateHeight(current);
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
        // calculando o fator de balanceamento do nó
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
            // rotaciona a esquerda
            if (getBalance(node.right) <= 0)
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

    // Atualiza a altura do nó
    private void updateHeight(Node node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    // Calcula o fator de balanceamento do nó
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

    public void printInOrder() {
        ArrayList<Integer> inOrderList = inOrder();
    }

    class Node {
        int element;
        Node left, right; // ref. dos filhos esquerdo e direito
        int height;

        Node(int element) {
            this.element = element;
            this.height = 0;
        }
    }

    // c) Testar a árvore de pesquisa implementada de acordo com as instruções fornecidas
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        // Instanciar a árvore implementada;
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
                    avlTree.printInOrder();
                }
                case 5 -> {
                    avlTree.treeInfo();
                }
                case 6 -> {
                    // Incluir os números 1, 2, 3, 4, 5, 6, 7, 8, 9, nesta ordem, na árvore;
                    System.out.println("Testando a árvore com inserção de 1 a 9:");
                    AVLTree testTree = new AVLTree();

                    int[] numerosCres = {1, 2, 3, 4, 5, 6, 7, 8, 9};
                    for (int num : numerosCres) {
                        testTree.add(num);
                    }
                    testTree.printTree();

                    // Apresentar a altura da árvore;
                    System.out.println("Altura da árvore: " + testTree.height());

                    // Limpar o conteúdo da árvore;
                    testTree.clear();
                    System.out.println("Árvore após limpar:");
                    testTree.printInOrder();

                    // Incluir os números 9, 8, 7, 6, 5, 4, 3, 2, 1, nesta ordem, na árvore;
                    System.out.println("Testando a árvore com inserção de 9 a 1:");
                    int[] numerosDec = {9, 8, 7, 6, 5, 4, 3, 2, 1};
                    for (int num : numerosDec) {
                        testTree.add(num);
                    }
                    testTree.printTree();
                }
                case 8 -> {
                    avlTree.clear();
                }
            }
        } while (op != 0);
        scan.close();
    }
}