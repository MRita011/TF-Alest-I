import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

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
        while (!Objects.equals(current.element, v)) {
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
        while (!Objects.equals(current.element, v)) {
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
        System.out.println("Nível do menor nodo: " + minNodeLevel(root));
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

    /**
     * Método minNodeLeve(...)
     * método retorna o nível do nodo com menor valor existente na árvore
     * @param current
     * @return valor do menor nodo da árvore
     */
    public int minNodeLevel(Node current) {
        if (current == null)
            return -1;

        int level = 0;
        while (current.left != null) {
            current = current.left;
            level++;
        }
        return level;
    }

    /**
     * Método diffMaxRoot(...)
     * método retorna a diferença entre o valor do nodo máximo da árvore e a raiz
     * @return valor da subtração do valor do nodo de maior valor com o valor do nodo raiz
     */
    public int diffMaxRoot() {
        // arvore vazia
        if (root == null)
            return 0;

        // nodo maximo do lado direito
        Node maxNode = findMaxNode(root);

        // diferença entre o valor maximo e a raiz
        return maxNode.element - root.element;
    }

    private Node findMaxNode(Node current) {
        if (current.right == null)
            return current;
        return findMaxNode(current.right);
    }

    /**
     * Método countInternalNodes(...)
     * método retorna a quantidade de nodos internos (galhos) de uma árvore
     * @param current
     * @return valor inteiro correspondente a quantidade de nodos folha
     */
    public int countInternalNodes(Node current) {
        if (current == null || (current.left == null && current.right == null))
            return 0;
        return 1 + countInternalNodes(current.left) + countInternalNodes(current.right);
    }

    /**
     * Método sumExternalNodes(...)
     * método retorna a soma dos valores de todos os nodos externos (folhas) de uma árvore
     * @param current
     * @return valor inteiro correspondente a soma dos valores de todos os nodos folha
     */
    public int sumExternalNodes(Node current) {
        if (current == null)
            return 0;

        if (current.left == null && current.right == null)
            return current.element;

        return sumExternalNodes(current.left) + sumExternalNodes(current.right);
    }

    /**
     * Método breadthFirstOrder(...)
     * método que imprime o caminhamento em largura
     */
    public void breadthFirstOrder() {
        if (root == null)
            return;

        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            System.out.print(current.element + " ");

            if (current.left != null)
                queue.add(current.left);

            if (current.right != null)
                queue.add(current.right);
        }
        System.out.println();
    }

    /**
     * Método sumBetween(...)
     * método método que retorna a soma de valores de uma sequência de nodos (deve incluir o valor do nodo inicial, mas não do nodo final)
     * @param start valor que corresponde ao nodo de início
     * @param end valor que corresponde ao nodo de fim
     * @return valor inteiro correspondente a soma dos nodos do caminho determinado
     * */

    public int sumBetween(int start, int end) {
        if (root == null || start >= end)
            return 0;

        Node startNode = findNode(start);
        Node endNode = findNode(end);

        if (startNode == null || endNode == null)
            return 0;

        Node ac = findAC(root, start, end);
        return sumPath(ac, startNode, endNode.element);
    }

    private Node findNode(int value) {
        Node current = root;

        while (current != null) {
            if (current.element == value)
                return current;

            else if (value < current.element)
                current = current.left;

            else
                current = current.right;
        }
        return null;
    }

    private Node findAC(Node current, int start, int end) {
        if (current == null)
            return null;

        if (current.element > start && current.element > end)
            return findAC(current.left, start, end);

        if (current.element < start && current.element < end)
            return findAC(current.right, start, end);

        return current;
    }

    private int sumPath(Node current, Node startNode, int exclude) {
        if (current == null)
            return 0;

        if (current.element == startNode.element)
            return startNode.element;

        int sum = 0;
        if (current.element != exclude)
            sum += current.element;

        if (current.element > startNode.element)
            return sum + sumPath(current.left, startNode, exclude);

        else
            return sum + sumPath(current.right, startNode, exclude);
    }
}