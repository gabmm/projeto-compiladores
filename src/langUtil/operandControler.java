package langUtil;

import java.util.Stack;

public class operandControler<A> {
    private final Stack<A> stk;
    private boolean debugger;

    public operandControler(boolean debugger){
        this.stk = new Stack<A>();
        this.debugger = debugger;
    }

    public A push(A item) {
        if (debugger) System.out.println("Push: " + item.toString());
        A top = this.stk.push(item);
        if (debugger) this.printStack();
        return top;
    }

    public A pop() {
        A top = this.stk.pop();
        if (debugger) System.out.println("Pop: " + top.toString());
        if (debugger) this.printStack();
        return top;
    }

    public void printStack() {
        System.out.println("---- Pilha ----");
        for (int i = this.stk.size() - 1; i >= 0; i--) {
            if (i == this.stk.size() - 1) {
                System.out.println("[Top] " + this.stk.get(i));
            } else {
                System.out.println("       " + this.stk.get(i));
            }
        }
        System.out.println("---------------");
    }

    public boolean isEmpty() { 
        return this.stk.isEmpty(); 
    }

    public A peek() { 
        return this.stk.peek();
    }
}
