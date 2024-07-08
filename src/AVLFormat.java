import java.util.ArrayList;

class AVLFormatter {

    int padding = 2; // mínimo de espaços horizontais entre dois dados de nó

    private int indent(ArrayList<String> lines, int margin) {
        // Se for negativo, prefixa todas as linhas com espaços e retorna 0
        if (margin >= 0) return margin;
        String spaces = " ".repeat(-margin);
        int i = 0;
        for (String line : lines) {
            lines.set(i++, spaces + line);
        }
        return 0;
    }

    private ArrayList<String> merge(ArrayList<String> left, ArrayList<String> right) {
        // Mescla dois arrays, onde as strings da direita são recuadas para que não haja sobreposição
        int minSize = Math.min(left.size(), right.size());
        int offset = 0;
        for (int i = 0; i < minSize; i++) {
            offset = Math.max(offset, left.get(i).length() + padding - right.get(i).replaceAll("\\S.*", "").length());
        }
        indent(right, -indent(left, offset));
        for (int i = 0; i < minSize; i++) {
            left.set(i, left.get(i) + right.get(i).substring(left.get(i).length()));
        }
        if (right.size() > minSize) {
            left.addAll(right.subList(minSize, right.size()));
        }
        return left;
    }

    private ArrayList<String> buildLines(AVLTree.Node node) {
        if (node == null) return new ArrayList<>();
        ArrayList<String> lines = merge(buildLines(node.left), buildLines(node.right));
        int half = String.valueOf(node.element).length() / 2;
        int i = half;
        if (!lines.isEmpty()) {
            String line;
            i = lines.get(0).indexOf("*"); // Encontra o índice do primeiro subárvore
            if (node.right == null) {
                line = " ".repeat(i) + "┌─┘";
                i += 2;
            } else if (node.left == null) {
                line = " ".repeat(i = indent(lines, i - 2)) + "└─┐";
            } else {
                int dist = lines.get(0).length() - 1 - i; // Encontra a distância entre as raízes das subárvores
                line = String.format("%s┌%s┴%s┐", " ".repeat(i), "─".repeat(dist / 2 - 1), "─".repeat((dist - 1) / 2));
                i += dist / 2;
            }
            lines.set(0, line);
        }
        lines.add(0, " ".repeat(indent(lines, i - half)) + node.element);
        lines.add(0, " ".repeat(i + Math.max(0, half - i)) + "*"); // Adiciona um marcador para o chamador
        return lines;
    }

    public String topDown(AVLTree.Node root) {
        ArrayList<String> lines = buildLines(root);
        return String.join("\n", lines.subList(1, lines.size()));
    }
}
