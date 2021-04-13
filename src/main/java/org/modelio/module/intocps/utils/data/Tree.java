package org.modelio.module.intocps.utils.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;


public class Tree<T> {

  private T head;

  private ArrayList<Tree<T>> leafs = new ArrayList<>();

  private Tree<T> parent = null;

  private HashMap<T, Tree<T>> locate = new HashMap<>();

  public Tree(T head) {
    this.head = head;
    this.locate.put(head, this);
  }

  public void addLeaf(T root, T leaf) {
    if (this.locate.containsKey(root)) {
        this.locate.get(root).addLeaf(leaf);
    } else {
      addLeaf(root).addLeaf(leaf);
    }
  }

  public Tree<T> addLeaf(T leaf) {
    Tree<T> t = new Tree<>(leaf);
    this.leafs.add(t);
    t.parent = this;
    t.locate = this.locate;
    this.locate.put(leaf, t);
    return t;
  }

  public Tree<T> setAsParent(T parentRoot) {
    Tree<T> t = new Tree<>(parentRoot);
    t.leafs.add(this);
    this.parent = t;
    t.locate = this.locate;
    t.locate.put(this.head, this);
    t.locate.put(parentRoot, t);
    return t;
  }

  public T getHead() {
    return this.head;
  }

  public Tree<T> getTree(T element) {
    return this.locate.get(element);
  }

  public Tree<T> getParent() {
    return this.parent;
  }

  public Collection<T> getSuccessors(T root) {
    Collection<T> successors = new ArrayList<>();
    Tree<T> tree = getTree(root);
    if (null != tree) {
      for (Tree<T> leaf : tree.leafs) {
        successors.add(leaf.head);
      }
    }
    return successors;
  }

  public Collection<Tree<T>> getSubTrees() {
    return this.leafs;
  }

  public static <T> Collection<T> getSuccessors(T of, Collection<Tree<T>> in) {
    for (Tree<T> tree : in) {
      if (tree.locate.containsKey(of)) {
        return tree.getSuccessors(of);
      }
    }
    return new ArrayList<>();
  }

  @Override
  public String toString() {
    return printTree(0);
  }

  private static final int indent = 2;

  private String printTree(int increment) {
    String s = "";
    String inc = "";
    for (int i = 0; i < increment; ++i) {
      inc = inc + " ";
    }
    s = inc + this.head;
    for (Tree<T> child : this.leafs) {
      s += "\n" + child.printTree(increment + indent);
    }
    return s;
  }
}