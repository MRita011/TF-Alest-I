// Imports
import java.util.LinkedList;
import java.util.Queue;

/**
 * CLASSE BinarySearchTree
 * Trabalhando com árvore binária de pesquisa
 */

class BinarySearchTree {
    private Node root;

    public BinarySearchTree() {
        root = null;
    }

    public void add(Integer v) {
        Node prev, current;

        // cria um novo nodo
        Node node = new Node();

        // atribui o valor recebido ao item de dados do nodo
        node.element = v;
        node.right = null;
        node.left = null;

        // se a raiz está nula, a árvore está vazia
        if (root == null)
            root = node;

        else {
            current = root;
            // percorre a árvore
            while (true) {
                prev = current;
                // ir para esquerda
                if (v <= current.element) {
                    current = current.left;
                    if (current == null) {
                        // insere na subárvore da esquerda
                        prev.left = node;
                        return;
                    }
                }
                // ir para direita
                else {
                    current = current.right;
                    if (current == null) {
                        // insere na subárvore da direita
                        prev.right = node;
                        return;
                    }
                }
            }
        }
    }

    public Node contains(Integer v) {
        // se arvore vazia
        if (root == null)
            return null;

        // começa a procurar desde raiz
        Node current = root;
        // enquanto nao encontrou
        while (current.element != v) {
            if (v < current.element)
                current = current.left; // caminha para esquerda
            else
                current = current.right; // caminha para direita

            // encontrou uma folha -> sai
            if (current == null)
                return null;
        }

        // terminou o laço while e chegou aqui é pq encontrou item
        return current;
    }

    public boolean remove(Integer v) {
        // se arvore vazia
        if (root == null)
            return false;

        Node current = root;
        Node father = root;
        boolean child_left = true;

        // buscando o valor
        while (current.element != v) {
            // enquanto nao encontrou
            father = current;

            // caminha para esquerda
            if (v < current.element) {
                current = current.left;
                // é filho a esquerda? sim
                child_left = true;
            }
            // caminha para direita
            else {
                current = current.right;
                // é filho a esquerda? NAO
                child_left = false;
            }
            // encontrou uma folha -> sai
            if (current == null)
                return false;
        }
        // Se nao possui nenhum filho (é uma folha), elimine-o
        if (current.left == null && current.right == null) {
            // se raiz
            if (current == root)
                root = null;
                // se for filho a esquerda do pai
            else if (child_left)
                father.left = null;
                // se for filho a direita do pai
            else
                father.right = null;
        }
        // Se é pai e nao possui um filho a direita, substitui pela subarvore a direita
        else if (current.right == null) {
            // se raiz
            if (current == root)
                root = current.left;
                // se for filho a esquerda do pai
            else if (child_left)
                father.left = current.left;
                // se for filho a direita do pai
            else
                father.right = current.left;
        }
        // Se é pai e nao possui um filho a esquerda, substitui pela subarvore a esquerda
        else if (current.left == null) {
            // se raiz
            if (current == root)
                root = current.right;
                // se for filho a esquerda do pai
            else if (child_left)
                father.left = current.right;
                // se for  filho a direita do pai
            else
                father.right = current.right;
        }
        // Se possui mais de um filho, se for um avô ou outro grau maior de parentesco
        else {
            Node successor = node_successor(current);
            // Usando sucessor que seria o Nó mais a esquerda da subarvore a direita do No que deseja-se remover
            // se raiz
            if (current == root)
                root = successor;
                // se for filho a esquerda do pai
            else if (child_left)
                father.left = successor;
                // se for filho a direita do pai
            else
                father.right = successor;
            // acertando o ponteiro a esquerda do sucessor agora que ele assumiu
            successor.left = current.left;
            // a posição correta na arvore
        }
        return true;
    }

    // O sucessor é o nodo mais a esquerda da subarvore a direita do nodo que foi passado como parâmetro do método
    public Node node_successor(Node node) {
        Node father_successor = node;
        Node successor = node;
        Node current = node.left;

        // enquanto nao chegar no nodo mais a esquerda
        while (current != null) {
            father_successor = successor;
            successor = current;
            // caminha para a esquerda
            current = current.left;
        }
        // se sucessor nao é o filho a direita do Nó que deverá ser eliminado
        if (successor != node.right) {
            // pai herda os filhos do sucessor que sempre serão a direita
            father_successor.left = successor.right;
            successor.right = node.right;
        }
        return successor;
    }

    void clearTree() {
        root = null;
    }

    public void inOrder(Node current) {
        if (current != null) {
            inOrder(current.left);
            System.out.print(current.element + " ");
            inOrder(current.right);
        }
    }

    public void preOrder(Node current) {
        if (current != null) {
            System.out.print(current.element + " ");
            preOrder(current.left);
            preOrder(current.right);
        }
    }

    public void postOrder(Node current) {
        if (current != null) {
            postOrder(current.left);
            postOrder(current.right);
            System.out.print(current.element + " ");
        }
    }

    public int height(Node current) {
        if (current == null || (current.left == null && current.right == null)) {
            return 0;
        } else {
            if (height(current.left) > height(current.right))
                return (1 + height(current.left));
            else
                return (1 + height(current.right));
        }
    }

    public int countNodes(Node current) {
        if (current == null)
            return 0;
        else
            return (1 + countNodes(current.left) + countNodes(current.right));
    }

    public Node getRoot() {
        return root;
    }

    public void orders() {
        System.out.print("\n Caminhamento Central (in-order): ");
        inOrder(root);
        System.out.print("\n Exibindo em Pós-ordem (post-order): ");
        postOrder(root);
        System.out.print("\n Exibindo em Pré-ordem (pre-order): ");
        preOrder(root);
        System.out.print("\n Exibindo em Largura (breadth-first): ");
        breadthFirstOrder();

    }

    public void treeInfo() {
        System.out.println("Altura da arvore: " + height(root));
        System.out.println("Quantidade de Nós: " + countNodes(root));
        System.out.println("Nível do menor nodo: " + minNodeLevel());
        System.out.println("Diferença entre o valor máximo e a raiz: " + diffMaxRoot());
        System.out.println("Contagem dos nodos internos (galhos): " + countInternalNodes(root));
        System.out.println("Soma dos valores de nodos externos (folhas): " + sumExternalNodes(root));

    }

    public void printTree() {
        if (root != null) {
            TreeFormatter formatter = new TreeFormatter();
            System.out.println(formatter.topDown(root));
        } else {
            System.out.println("Árvore vazia!");
        }
    }

    // ------------- Métodos Adicionados ------------- //

    public int minNodeLevel() {
        //se árvore vazia, retorna 0
        if (root == null)
            return 0;

        //inicia pelo root e atribui o nível 0
        Node current = root;
        int level = 0;

        //enquanto left diferente de null, corre para esquerda e incrementa level
        while (current.left != null) {
            current = current.left;
            level++;
        }

        //quando encontra o menor, retorna o nível
        return level;
    }

    public int diffMaxRoot() {
        if (root == null)
            return 0;

        //inicia a procura pela raiz
        Node current = root;

        //enquanto houver números à direita, ou seja, maiores que a raiz e seus subsequentes, continua percorrendo a árvore
        while (current.right != null) {
            current = current.right;
        }

        //subtrai o número mais à direita (maior) da raiz
        return current.element - root.element;
    }

    public int countInternalNodes(Node node) {
        if (node == null)
            return 0;

        //verifica se é folha
        if (node.left == null && node.right == null)
            return 0;

        //conta o nodo atual e os anteriores
        return 1 + countInternalNodes(node.left) + countInternalNodes(node.right);
    }

    public int sumExternalNodes(Node node) {
        if (node == null)
            return 0;

        //verifica se é folha
        if (node.left == null && node.right == null)
            return node.element;

        //retorna retorna a soma dos nodos folha
        return sumExternalNodes(node.left) + sumExternalNodes(node.right);
    }

    public void breadthFirstOrder() {
        if (root == null) {
            System.out.println("Árvore vazia!");
            return;
        }

        //cria uma lista encadeada com o nodo raiz
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        //enquanto a fila não estiver vazia, o current recebe o primeiro elemento da fila
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            //printa o elemento atual
            System.out.print(current.element + " ");

            //se left diferente de null, adiciona na fila
            if (current.left != null)
                queue.add(current.left);

            //se right diferente de null, adiciona na fila
            if (current.right != null)
                queue.add(current.right);
        }
    }

    public int sumBetween(int start, int end) {
        return sumBetween(root, start, end);
    }

    private int sumBetween(Node node, int start, int end) {
        if (node == null)
            return 0;

        //inicializa a variável sum
        int sum = 0;

        //se nodo maior ou igual ao elemento inicial inserido no parâmetro e menor que o final,
        //sum recebe o valor de sum + valor do nodo atual
        if (node.element >= start && node.element < end)
            sum += node.element;

        //se o nodo atual for maior que o inicial, continua percorrendo o lado esquerdo
        if (node.element > start)
            sum += sumBetween(node.left, start, end);

        //se o nodo atual for menor que o final, continua percorrendo o lado direito
        if (node.element < end)
            sum += sumBetween(node.right, start, end);

        //retorna o valor que está em sum neste momento
        return sum;
    }
}