import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;

public class Binairo {
    private final ArrayList<ArrayList<String>> board;
    private final ArrayList<ArrayList<ArrayList<String>>> domain;
    private final int n;

    public static int backFrom = 0;
 //   public ArrayList<State> visited = new ArrayList<>();
    Hashtable<String, Boolean> visited = new Hashtable<>();


    public Binairo(ArrayList<ArrayList<String>> board,
                   ArrayList<ArrayList<ArrayList<String>>> domain,
                   int n) {
        this.board = board;
        this.domain = domain;
        this.n = n;
    }

    public void MRV() {
        long tStart = System.nanoTime();
        State state = new State(board, domain);

        drawLine();
        System.out.println("MRV\nInitial Board: \n");
        state.printBoard();
        drawLine();

        backtrackMRV(state);
        drawLine();
        long tEnd = System.nanoTime();
        System.out.println("Total time: " + (tEnd - tStart) / 1000000000.000000000);
    }

    public void LCV() {
        long tStart = System.nanoTime();
        State state = new State(board, domain);

        drawLine();
        System.out.println("LCV\nInitial Board: \n");
        state.printBoard();
        drawLine();

        backtrackLCV(state);
        drawLine();
        long tEnd = System.nanoTime();
        System.out.println("Total time: " + (tEnd - tStart) / 1000000000.000000000);
    }

    //FC + MRV
    private boolean backtrackMRV(State initState) {
        State state;
        ArrayList<State> nextStates;
        Stack<State> stack = new Stack<>();
        initState.firstForwardChecking();
        stack.push(initState);
        while (!stack.empty()) {
            state = stack.peek();
            state.printBoard();
            drawLine();
            if (isFinished(state)) {
                state.printBoard();
                return true;
            }
            nextStates = state.MRVChildren(stack.size(), visited);
            if (nextStates.size() == 0) {
                int x = stack.size();
                for (int i = 0; i < x - backFrom; i++) {
                    State s = stack.pop();
                    if(i != x - backFrom - 1)
                        visited.remove(s.hash());
                }
            }
            for (State s : nextStates) {
                stack.push(s);
                if (s.is==4 && s.js == 6){
                    System.out.println();
                }
            }
        }

        return false;
    }

    //FC + LCV
    private boolean backtrackLCV(State initState) {
        State state;
        ArrayList<State> nextStates;
        Stack<State> stack = new Stack<>();
        initState.firstForwardChecking();
        stack.push(initState);
        while (!stack.empty()) {
            state = stack.peek();
            state.printBoard();
            drawLine();
            if (isFinished(state)) {
                state.printBoard();
                return true;
            }
            nextStates = state.LCVChildren(stack.size(), visited);
            if (nextStates.size() == 0) {
                int x = stack.size();
                for (int i = 0; i < x - backFrom; i++) {
                    State s = stack.pop();
                    if(i != x - backFrom - 1)
                        visited.remove(s.hash());
                }
            }
            for (State s : nextStates) {
                stack.push(s);
                if (s.is==3 && s.js == 1){
                    System.out.println();
                }
            }
        }

        return false;
    }

    private boolean checkNumberOfCircles(State state) {
        ArrayList<ArrayList<String>> cBoard = state.getBoard();
        //row
        for (int i = 0; i < n; i++) {
            int numberOfWhites = 0;
            int numberOfBlacks = 0;
            for (int j = 0; j < n; j++) {
                String a = cBoard.get(i).get(j);
                switch (a) {
                    case "w", "W" -> numberOfWhites++;
                    case "b", "B" -> numberOfBlacks++;
                }
            }
            if (numberOfBlacks > n / 2 || numberOfWhites > n / 2) {
                return false;
            }
        }
        //column
        for (int i = 0; i < n; i++) {
            int numberOfWhites = 0;
            int numberOfBlacks = 0;
            for (int j = 0; j < n; j++) {
                String a = cBoard.get(j).get(i);
                switch (a) {
                    case "w", "W" -> numberOfWhites++;
                    case "b", "B" -> numberOfBlacks++;
                }
            }
            if (numberOfBlacks > n / 2 || numberOfWhites > n / 2) {
                return false;
            }
        }
        return true;
    }

    private boolean checkAdjacency(State state) {
        ArrayList<ArrayList<String>> cBoard = state.getBoard();

        //Horizontal
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n - 2; j++) {
                ArrayList<String> row = cBoard.get(i);
                String c1 = row.get(j).toUpperCase();
                String c2 = row.get(j + 1).toUpperCase();
                String c3 = row.get(j + 2).toUpperCase();
                if (c1.equals(c2) && c2.equals(c3) && !c1.equals("E")) {
                    return false;
                }
            }
        }
        //column
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < n - 2; i++) {
                String c1 = cBoard.get(i).get(j).toUpperCase();
                String c2 = cBoard.get(i + 1).get(j).toUpperCase();
                String c3 = cBoard.get(i + 2).get(j).toUpperCase();
                if (c1.equals(c2) && c2.equals(c3) && !c1.equals("E")) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean checkIfUnique(State state) {
        ArrayList<ArrayList<String>> cBoard = state.getBoard();

        //check if two rows are duplicated
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                int count = 0;
                for (int k = 0; k < n; k++) {
                    String a = cBoard.get(i).get(k);
                    if (a.equals(cBoard.get(j).get(k)) && !a.equals("E")) {
                        count++;
                    }
                }
                if (count == n) {
                    return false;
                }
            }
        }

        //check if two columns are duplicated

        for (int j = 0; j < n - 1; j++) {
            for (int k = j + 1; k < n; k++) {
                int count = 0;
                for (int i = 0; i < n; i++) {
                    if (cBoard.get(i).get(j).equals(cBoard.get(i).get(k))) {
                        count++;
                    }
                }
                if (count == n) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean allAssigned(State state) {
        ArrayList<ArrayList<String>> cBoard = state.getBoard();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                String s = cBoard.get(i).get(j);
                if (s.equals("E"))
                    return false;
            }
        }
        return true;
    }


    private boolean isFinished(State state) {
        return allAssigned(state) && checkAdjacency(state) && checkNumberOfCircles(state) && checkIfUnique(state);
    }

    public boolean isConsistent(State state) {
        return checkNumberOfCircles(state) && checkAdjacency(state) && checkIfUnique(state);
    }

    private void drawLine() {
        for (int i = 0; i < n * 2; i++) {
            System.out.print("\u23E4\u23E4");
        }
        System.out.println();
    }
}
