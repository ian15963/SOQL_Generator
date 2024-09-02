package org.example.tree;

public class BTree<T> {

    private final Node<T> root;

    public BTree(T value) {
        this.root = new Node<>(value);
    }

    public void addRight(T value){
        if (root.right == null){
            root.setRight(new Node[5]);
            root.right[0] = new Node<>(value);
            return;
        }

        Node<T>[] current = root.getRight();
        int totalNull = 0;
        for (Node<T> node: current){
            if(node == null){
                node = new Node<>(value);
                totalNull++;
                break;
            }
        }

        if (totalNull == 0){
            Node<T>[] newNodes = new Node[current.length * 2];
            int previousCurrentLength = current.length;
            if (current.length >= 0){
                System.arraycopy(current, 0, newNodes, 0, previousCurrentLength);
            }
            current = newNodes;
            current[previousCurrentLength] = new Node<>(value);
        }

    }

    public void addLeft(T value){
        if(root.left == null){
            root.setLeft(new Node[5]);
            root.left[0] = new Node<>(value);
            return;
        }

        Node<T>[] current = root.left;
        int totalNull = 0;
        for (int i = 0; i < current.length; i++){
            if(current[i] == null){
                current[i] = new Node<>(value);
                totalNull++;
                break;
            }
        }

        if(totalNull == 0){
            Node<T>[] newNodes = new Node[current.length * 2];
            int previousCurrentLength = current.length;
            if (current.length >= 0){
                System.arraycopy(current, 0, newNodes, 0, current.length);
            }
            current = newNodes;
            current[previousCurrentLength] = new Node<>(value);
        }
    }

    public Node<T> searchNode(T value){
        if (root == null || (root.left == null && root.right == null)){
            throw new RuntimeException();
        }

        if(root.left != null){
            for (int i = 0; i < root.left.length; i++){
                if (root.left[i] != null){
                    if(root.left[i].getValue() == value){
                        return root.left[i];
                    }
                }
            }
        }

        if(root.right != null){
            for (int i = 0; i < root.right.length; i++){
                if(root.right[i] != null){

                }
            }
        }
        return null;
    }

    public String buildQuery(){
        StringBuilder stringBuilder = new StringBuilder();
        if (root == null || (root.left == null && root.right == null)){
            throw new RuntimeException();
        }

        if(root.left != null){
            for (int i = 0; i < root.left.length; i++) {
                if(root.left[i] != null){
                    if (i == root.left.length - 1){
                        stringBuilder.append("%s".formatted(root.left[i].getValue()));
                    }else{
                        stringBuilder.append("%s,".formatted(root.left[i].getValue()));
                    }
                }
            }
        }

        Node<T>[] parentRightNodes = root.right;
        for (int i = 0; i < parentRightNodes.length; i++){
            if(parentRightNodes[i] != null){
                if (parentRightNodes[i].left != null){
                    for (int j = 0; j < parentRightNodes[i].left.length; j++){
                        if (parentRightNodes[i].left[i] != null){
                            Node<T> tNode = parentRightNodes[i].left[j];
                            stringBuilder.append("%s".formatted(tNode.value));
                        }
                    }
                }
            }
        }

        return stringBuilder.toString();
    }

    public static class Node<T> {

        private T value;
        private Node<T>[] left;
        private Node<T>[] right;

        public Node() {
        }

        public Node(T value) {
            this.value = value;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public Node<T>[] getLeft() {
            return left;
        }

        public void setLeft(Node<T>[] left) {
            this.left = left;
        }

        public Node<T>[] getRight() {
            return right;
        }

        public void setRight(Node<T>[] right) {
            this.right = right;
        }
    }


    public Node<T> getRoot() {
        return root;
    }
}
