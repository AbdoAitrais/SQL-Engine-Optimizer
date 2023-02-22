package BusinessObject;

import java.util.Stack;

public class Test {
    public static void main(String[] args) {
        Stack<Integer> st = new Stack<>();
        st.push(5);
        st.push(2);
        st.push(6);

        System.out.println(st.peek());
        System.out.println(st.pop());
    }
}
