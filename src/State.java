import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

public class State {
    private static char blackCircle = '\u26AB';
    private static char whiteCircle = '\u26AA';
    private static char blackSquare = '\u2B1B';
    private static char whiteSquare = '\u2B1C';
    private static char line = '\u23E4';


    private ArrayList<ArrayList<String>> board;
    private ArrayList<ArrayList<ArrayList<String>>> domain;
    private int n;

    int is, js;
    String val;

    public State(ArrayList<ArrayList<String>> board,
                 ArrayList<ArrayList<ArrayList<String>>> domain) {

        this.board = board;
        this.domain = domain;
        this.n = board.size();
    }

    public State(ArrayList<ArrayList<String>> board,
                 ArrayList<ArrayList<ArrayList<String>>> domain, int i, int j , String val) {

        this.board = board;
        this.domain = domain;
        this.n = board.size();
        this.is = i;
        this.js = j;
        this. val = val;
    }

    public ArrayList<ArrayList<String>> getBoard() {
        return board;
    }

    public void setIndexBoard(int x, int y, String value) {
        board.get(x).set(y, value);
    }

    public void removeIndexDomain(int x, int y, String value) {
        domain.get(x).get(y).remove(value);
    }

    public ArrayList<ArrayList<ArrayList<String>>> getDomain() {
        return domain;
    }

    public State copy() {
        ArrayList<ArrayList<String>> cb = copyBoard(board);
        ArrayList<ArrayList<ArrayList<String>>> cd = copyDomain(domain);
        return (new State(cb, cd, is, js, val));
    }

    private ArrayList<ArrayList<String>> copyBoard(ArrayList<ArrayList<String>> cBoard) {
        ArrayList<ArrayList<String>> res = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            res.add(new ArrayList<>(Arrays.asList(new String[n])));
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                res.get(i).set(j, cBoard.get(i).get(j));
            }
        }

        return res;
    }

    private ArrayList<ArrayList<ArrayList<String>>> copyDomain(ArrayList<ArrayList<ArrayList<String>>> cDomain) {
        ArrayList<ArrayList<ArrayList<String>>> res = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            ArrayList<ArrayList<String>> row = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                row.add(new ArrayList<>(Arrays.asList("", "")));
            }
            res.add(row);
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < cDomain.get(i).get(j).size(); k++) {
                    res.get(i).get(j).set(k, cDomain.get(i).get(j).get(k));
                }
            }
        }

        return res;
    }

    public void printBoard() {
        for (ArrayList<String> strings : this.getBoard()) {
            for (String s : strings) {
                switch (s) {
                    case "w":
                        System.out.print(whiteCircle + "  ");
                        break;
                    case "W":
                        System.out.print(whiteSquare + "  ");
                        break;
                    case "b":
                        System.out.print(blackCircle + "  ");
                        break;
                    case "B":
                        System.out.print(blackSquare + "  ");
                        break;
                    default:
                        System.out.print(line + "  ");
                        break;
                }
            }
            System.out.println("\n");
        }
    }

    public void printDomain() {
        for (ArrayList<ArrayList<String>> strings : this.getDomain()) {
            for (ArrayList<String> s : strings) {
                System.out.print(s + " ");
            }
            System.out.println();
        }
    }

    private int lenDom(int i, int j) {
        ArrayList<String> x = domain.get(i).get(j);
        int s = x.size();
        for (int k = 0; k < x.size(); k++) {
            if (x.get(k).equals("")) {
                s--;
            }
        }
        return s;
    }

    public ArrayList<State> MRVChildren(int s, Hashtable<String, Boolean> visited) {
        State child = this.copy();
        int m = 3;
        int mini = 0, minj = 0;
        ArrayList<State> children = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (lenDom(i, j) < m && !this.getDomain().get(i).get(j).contains("n")) {
                    mini = i;
                    minj = j;
                    m = lenDom(i, j);
                }
            }
        }
        if (m == 1) {
            if (this.getDomain().get(mini).get(minj).contains("w")) {
                child.setIndexBoard(mini, minj, "w");
                child.val = "w";
                child.is = mini;
                child.js = minj;
            } else if (this.getDomain().get(mini).get(minj).contains("b")) {
                child.setIndexBoard(mini, minj, "b");
                child.val = "b";
                child.is = mini;
                child.js = minj;
            }
            if (!visited.containsKey(child.hash())) {
                if (forwardChecking(mini, minj, child)) {
                    child.removeIndexDomain(mini, minj, "w");
                    child.removeIndexDomain(mini, minj, "b");
                    child.getDomain().get(mini).get(minj).add("n");

                    children.add(child);
                    visited.put(child.hash(), true);
                }
            }
        } else if (m == 2) {
            loop:
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (this.getDomain().get(i).get(j).size() == 2 && !this.getDomain().get(i).get(j).contains("n")) {
                        mini = i;
                        minj = j;
                        child = this.copy();

                        child.setIndexBoard(mini, minj, "b");
                        child.val = "b";
                        child.is = mini;
                        child.js = minj;
                        if (!visited.containsKey(child.hash())) {
                            if (forwardChecking(mini, minj, child)) {
                                child.removeIndexDomain(mini, minj, "w");
                                child.removeIndexDomain(mini, minj, "b");
                                child.getDomain().get(mini).get(minj).add("n");

                                Binairo.backFrom = s;

                                children.add(child);
                                visited.put(child.hash(), true);
                                System.out.println("*");

                                break loop;
                            }
                        }
                        child = this.copy();
                        child.setIndexBoard(mini, minj, "w");
                        child.val = "w";
                        child.is = mini;
                        child.js = minj;
                        if (!visited.containsKey(child.hash())) {
                            if (forwardChecking(mini, minj, child)) {
                                child.removeIndexDomain(mini, minj, "w");
                                child.removeIndexDomain(mini, minj, "b");
                                child.getDomain().get(mini).get(minj).add("n");

                                children.add(child);
                                visited.put(child.hash(), true);
                                System.out.println("**");
                                break loop;
                            }
                        }
                    }
                }
            }
        }
        System.out.println(mini + " " + minj);
        return children;
    }

    public boolean forwardChecking(int x, int y, State child) {
        //check number of circles
        //row
        int numberOfWhites = 0;
        int numberOfBlacks = 0;
        for (int j = 0; j < n; j++) {
            String a = child.getBoard().get(x).get(j);
            switch (a) {
                case "w", "W" -> numberOfWhites++;
                case "b", "B" -> numberOfBlacks++;
            }
        }
        if (numberOfBlacks == n / 2) {
            for (int j = 0; j < n; j++) {
                child.removeIndexDomain(x, j, "b");
            }
        } else if (numberOfBlacks > n / 2) return false;
        if (numberOfWhites == n / 2) {
            for (int j = 0; j < n; j++) {
                child.removeIndexDomain(x, j, "w");
            }
        } else if (numberOfWhites > n / 2) return false;
        for (int j = 0; j < n; j++) {
            if (child.getDomain().get(x).get(j).size() == 0)
                return false;
            //backtrack
        }

        //column
        numberOfWhites = 0;
        numberOfBlacks = 0;
        for (int j = 0; j < n; j++) {
            String a = child.getBoard().get(j).get(y);
            switch (a) {
                case "w", "W" -> numberOfWhites++;
                case "b", "B" -> numberOfBlacks++;
            }
        }
        if (numberOfBlacks == n / 2) {
            for (int j = 0; j < n; j++) {
                child.removeIndexDomain(j, y, "b");
            }
        } else if (numberOfBlacks > n / 2) return false;
        if (numberOfWhites == n / 2) {
            for (int j = 0; j < n; j++) {
                child.removeIndexDomain(j, y, "w");
            }
        } else if (numberOfWhites > n / 2) return false;
        for (int j = 0; j < n; j++) {
            if (child.getDomain().get(j).get(y).size() == 0)
                return false;
            //backtrack
        }

        //checkAdjacency
        //Horizontal
        for (int j = 0; j < n - 2; j++) {
            ArrayList<String> row = child.getBoard().get(x);
            String c1 = row.get(j).toUpperCase();
            String c2 = row.get(j + 1).toUpperCase();
            String c3 = row.get(j + 2).toUpperCase();
            if (c1.equals(c2) && c2.equals(c3) && !c1.equals("E")) {
                return false;
            } else if (c1.equals(c2) && c1.equals("W")) {
                child.removeIndexDomain(x, j + 2, "w");
            } else if (c1.equals(c3) && c1.equals("W")) {
                child.removeIndexDomain(x, j + 1, "w");
            } else if (c2.equals(c3) && c2.equals("W")) {
                child.removeIndexDomain(x, j, "w");
            } else if (c1.equals(c2) && c1.equals("B")) {
                child.removeIndexDomain(x, j + 2, "b");
            } else if (c1.equals(c3) && c1.equals("B")) {
                child.removeIndexDomain(x, j + 1, "b");
            } else if (c2.equals(c3) && c2.equals("B")) {
                child.removeIndexDomain(x, j, "b");
            }
        }

        //column
        for (int i = 0; i < n - 2; i++) {
            String c1 = child.getBoard().get(i).get(y).toUpperCase();
            String c2 = child.getBoard().get(i + 1).get(y).toUpperCase();
            String c3 = child.getBoard().get(i + 2).get(y).toUpperCase();
            if (c1.equals(c2) && c2.equals(c3) && !c1.equals("E")) {
                return false;
            } else if (c1.equals(c2) && c1.equals("W")) {
                child.removeIndexDomain(i + 2, y, "w");
            } else if (c1.equals(c3) && c1.equals("W")) {
                child.removeIndexDomain(i + 1, y, "w");
            } else if (c2.equals(c3) && c2.equals("W")) {
                child.removeIndexDomain(i, y, "w");
            } else if (c1.equals(c2) && c1.equals("B")) {
                child.removeIndexDomain(i + 2, y, "b");
            } else if (c1.equals(c3) && c1.equals("B")) {
                child.removeIndexDomain(i + 1, y, "b");
            } else if (c2.equals(c3) && c2.equals("B")) {
                child.removeIndexDomain(i, y, "b");
            }
        }

        //checkIfUnique
        //check if two rows are duplicated
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                int count = 0;
                for (int k = 0; k < n; k++) {
                    String a = child.getBoard().get(i).get(k);
                    if (a.equals(child.getBoard().get(j).get(k)) && !a.equals("E")) {
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
                    if (child.getBoard().get(i).get(j).equals(child.getBoard().get(i).get(k))) {
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

    public ArrayList<State> LCVChildren(int s, Hashtable<String, Boolean> visited) {
        State child, ch = this.copy();
        int m = n * n, x;
        int mini = 0, minj = 0;
        ArrayList<State> children = new ArrayList<>();
        boolean sw = false;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (!this.getDomain().get(i).get(j).contains("n")) {
                    child = this.copy();
                    if (this.getDomain().get(i).get(j).contains("w")) {
                        child.setIndexBoard(i, j, "w");
                        child.is = i;
                        child.js = j;
                        child.val = "w";
                        x = forwardCheckingLCV(i, j, child);
                        if (x != -1 && x < m && !visited.containsKey(child.hash())) {
                            m = x;
                            mini = i;
                            minj = j;
                            ch = child.copy();
                            sw = true;
                        }
                    }
                    if (this.getDomain().get(i).get(j).contains("b")) {
                        child = this.copy();
                        child.setIndexBoard(i, j, "b");
                        child.is = i;
                        child.js = j;
                        child.val = "b";
                        x = forwardCheckingLCV(i, j, child);
                        if (x != -1 && x < m && !visited.containsKey(child.hash())) {
                            m = x;
                            mini = i;
                            minj = j;
                            ch = child.copy();
                            sw = true;
                        } else if (x == m) {
                            Binairo.backFrom = s;
                        }
                    }
                }
            }
        }
        if(sw) {
            visited.put(ch.hash(), true);
            ch.removeIndexDomain(mini, minj, "w");
            ch.removeIndexDomain(mini, minj, "b");
            if (ch.getDomain().get(mini).get(minj).contains("")) {
                ch.getDomain().get(mini).get(minj).set(0, "n");
            } else ch.getDomain().get(mini).get(minj).add("n");
            children.add(ch);
            System.out.println(mini + " " + minj);
        }
        return children;
    }

    public int forwardCheckingLCV(int x, int y, State child) {
        int numberOfChanged = 0;
        //check number of circles
        //row
        int numberOfWhites = 0;
        int numberOfBlacks = 0;
        for (int j = 0; j < n; j++) {
            String a = child.getBoard().get(x).get(j);
            switch (a) {
                case "w", "W" -> numberOfWhites++;
                case "b", "B" -> numberOfBlacks++;
            }
        }
        if (numberOfBlacks == n / 2) {
            for (int j = 0; j < n; j++) {
                if (child.getDomain().get(x).get(j).contains("b")) {
                    child.removeIndexDomain(x, j, "b");
                    numberOfChanged++;
                }
            }
        } else if (numberOfBlacks > n / 2) return -1;
        if (numberOfWhites == n / 2) {
            for (int j = 0; j < n; j++) {
                if (child.getDomain().get(x).get(j).contains("w")) {
                    child.removeIndexDomain(x, j, "w");
                    numberOfChanged++;
                }
            }
        } else if (numberOfWhites > n / 2) return -1;
        for (int j = 0; j < n; j++) {
            if (child.getDomain().get(x).get(j).size() == 0)
                return -1;
            //backtrack
        }

        //column
        numberOfWhites = 0;
        numberOfBlacks = 0;
        for (int j = 0; j < n; j++) {
            String a = child.getBoard().get(j).get(y);
            switch (a) {
                case "w", "W" -> numberOfWhites++;
                case "b", "B" -> numberOfBlacks++;
            }
        }
        if (numberOfBlacks == n / 2) {
            for (int j = 0; j < n; j++) {
                if (child.getDomain().get(j).get(y).contains("b")) {
                    child.removeIndexDomain(j, y, "b");
                    numberOfChanged++;
                }
            }
        } else if (numberOfBlacks > n / 2) return -1;
        if (numberOfWhites == n / 2) {
            for (int j = 0; j < n; j++) {
                if (child.getDomain().get(j).get(y).contains("w")) {
                    child.removeIndexDomain(j, y, "w");
                    numberOfChanged++;
                }
            }
        } else if (numberOfWhites > n / 2) return -1;
        for (int j = 0; j < n; j++) {
            if (child.getDomain().get(j).get(y).size() == 0)
                return -1;
            //backtrack
        }

        //checkAdjacency
        //Horizontal
        for (int j = 0; j < n - 2; j++) {
            ArrayList<String> row = child.getBoard().get(x);
            String c1 = row.get(j).toUpperCase();
            String c2 = row.get(j + 1).toUpperCase();
            String c3 = row.get(j + 2).toUpperCase();
            if (c1.equals(c2) && c2.equals(c3) && !c1.equals("E")) {
                return -1;
            } else if (c1.equals(c2) && c1.equals("W")) {
                if (child.getDomain().get(x).get(j + 2).contains("w")) {
                    child.removeIndexDomain(x, j + 2, "w");
                    numberOfChanged++;
                }
            } else if (c1.equals(c3) && c1.equals("W")) {
                if (child.getDomain().get(x).get(j + 1).contains("w")) {
                    child.removeIndexDomain(x, j + 1, "w");
                    numberOfChanged++;
                }
            } else if (c2.equals(c3) && c2.equals("W")) {
                if (child.getDomain().get(x).get(j).contains("w")) {
                    child.removeIndexDomain(x, j, "w");
                    numberOfChanged++;
                }
            } else if (c1.equals(c2) && c1.equals("B")) {
                if (child.getDomain().get(x).get(j + 2).contains("b")) {
                    child.removeIndexDomain(x, j + 2, "b");
                    numberOfChanged++;
                }
            } else if (c1.equals(c3) && c1.equals("B")) {
                if (child.getDomain().get(x).get(j + 1).contains("b")) {
                    child.removeIndexDomain(x, j + 1, "b");
                    numberOfChanged++;
                }
            } else if (c2.equals(c3) && c2.equals("B")) {
                if (child.getDomain().get(x).get(j).contains("b")) {
                    child.removeIndexDomain(x, j, "b");
                    numberOfChanged++;
                }
            }
        }

        //column
        for (int i = 0; i < n - 2; i++) {
            String c1 = child.getBoard().get(i).get(y).toUpperCase();
            String c2 = child.getBoard().get(i + 1).get(y).toUpperCase();
            String c3 = child.getBoard().get(i + 2).get(y).toUpperCase();
            if (c1.equals(c2) && c2.equals(c3) && !c1.equals("E")) {
                return -1;
            } else if (c1.equals(c2) && c1.equals("W")) {
                if (child.getDomain().get(i + 2).get(y).contains("w")) {
                    child.removeIndexDomain(i + 2, y, "w");
                    numberOfChanged++;
                }
            } else if (c1.equals(c3) && c1.equals("W")) {
                if (child.getDomain().get(i + 1).get(y).contains("w")) {
                    child.removeIndexDomain(i + 1, y, "w");
                    numberOfChanged++;
                }
            } else if (c2.equals(c3) && c2.equals("W")) {
                if (child.getDomain().get(i).get(y).contains("w")) {
                    child.removeIndexDomain(i, y, "w");
                    numberOfChanged++;
                }
            } else if (c1.equals(c2) && c1.equals("B")) {
                if (child.getDomain().get(i + 2).get(y).contains("b")) {
                    child.removeIndexDomain(i + 2, y, "b");
                    numberOfChanged++;
                }
            } else if (c1.equals(c3) && c1.equals("B")) {
                if (child.getDomain().get(i + 1).get(y).contains("b")) {
                    child.removeIndexDomain(i + 1, y, "b");
                    numberOfChanged++;
                }
            } else if (c2.equals(c3) && c2.equals("B")) {
                if (child.getDomain().get(i).get(y).contains("b")) {
                    child.removeIndexDomain(i, y, "b");
                    numberOfChanged++;
                }
            }
        }

        //checkIfUnique
        //check if two rows are duplicated
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                int count = 0;
                for (int k = 0; k < n; k++) {
                    String a = child.getBoard().get(i).get(k);
                    if (a.equals(child.getBoard().get(j).get(k)) && !a.equals("E")) {
                        count++;
                    }
                }
                if (count == n) {
                    return -1;
                }
            }
        }

        //check if two columns are duplicated
        for (int j = 0; j < n - 1; j++) {
            for (int k = j + 1; k < n; k++) {
                int count = 0;
                for (int i = 0; i < n; i++) {
                    if (child.getBoard().get(i).get(j).equals(child.getBoard().get(i).get(k))) {
                        count++;
                    }
                }
                if (count == n) {
                    return -1;
                }
            }
        }

        return numberOfChanged;
    }

    public boolean firstForwardChecking() {
        //check number of circles
        //row
        for (int i = 0; i < n; i++) {
            int numberOfWhites = 0;
            int numberOfBlacks = 0;
            for (int j = 0; j < n; j++) {
                String a = this.getBoard().get(i).get(j);
                switch (a) {
                    case "w", "W" -> numberOfWhites++;
                    case "b", "B" -> numberOfBlacks++;
                }
            }
            if (numberOfBlacks == n / 2) {
                for (int j = 0; j < n; j++)
                    this.removeIndexDomain(i, j, "b");
            } else if (numberOfBlacks > n / 2) return false;
            if (numberOfWhites == n / 2) {
                for (int j = 0; j < n; j++)
                    this.removeIndexDomain(i, j, "w");
            } else if (numberOfWhites > n / 2) return false;

            for (int j = 0; j < n; j++) {
                if (this.getDomain().get(i).get(j).size() == 0)
                    return false;
            }
        }

        //column
        for (int i = 0; i < n; i++) {
            int numberOfWhites = 0;
            int numberOfBlacks = 0;
            for (int j = 0; j < n; j++) {
                String a = this.getBoard().get(j).get(i);
                switch (a) {
                    case "w", "W" -> numberOfWhites++;
                    case "b", "B" -> numberOfBlacks++;
                }
            }
            if (numberOfBlacks == n / 2) {
                for (int j = 0; j < n; j++) {
                    this.removeIndexDomain(j, i, "b");
                }
            } else if (numberOfBlacks > n / 2) return false;
            if (numberOfWhites == n / 2) {
                for (int j = 0; j < n; j++) {
                    this.removeIndexDomain(j, i, "w");
                }
            } else if (numberOfWhites > n / 2) return false;
            for (int j = 0; j < n; j++) {
                if (this.getDomain().get(j).get(i).size() == 0)
                    return false;
            }
        }

        //checkAdjacency
        //Horizontal
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n - 2; j++) {
                ArrayList<String> row = this.getBoard().get(i);
                String c1 = row.get(j).toUpperCase();
                String c2 = row.get(j + 1).toUpperCase();
                String c3 = row.get(j + 2).toUpperCase();
                if (c1.equals(c2) && c2.equals(c3) && !c1.equals("E")) {
                    return false;
                } else if (c1.equals(c2) && c1.equals("W")) {
                    this.removeIndexDomain(i, j + 2, "w");
                } else if (c1.equals(c3) && c1.equals("W")) {
                    this.removeIndexDomain(i, j + 1, "w");
                } else if (c1.equals(c2) && c1.equals("B")) {
                    this.removeIndexDomain(i, j + 2, "b");
                } else if (c1.equals(c3) && c1.equals("B")) {
                    this.removeIndexDomain(i, j + 1, "b");
                }
            }
        }

        //column
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < n - 2; i++) {
                String c1 = this.getBoard().get(i).get(j).toUpperCase();
                String c2 = this.getBoard().get(i + 1).get(j).toUpperCase();
                String c3 = this.getBoard().get(i + 2).get(j).toUpperCase();
                if (c1.equals(c2) && c2.equals(c3) && !c1.equals("E")) {
                    return false;
                } else if (c1.equals(c2) && c1.equals("W")) {
                    this.removeIndexDomain(i + 2, j, "w");
                } else if (c1.equals(c3) && c1.equals("W")) {
                    this.removeIndexDomain(i + 1, j, "w");
                } else if (c1.equals(c2) && c1.equals("B")) {
                    this.removeIndexDomain(i + 2, j, "b");
                } else if (c1.equals(c3) && c1.equals("B")) {
                    this.removeIndexDomain(i + 1, j, "b");
                }
            }
        }

        //checkIfUnique
        //check if two rows are duplicated
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                int count = 0;
                for (int k = 0; k < n; k++) {
                    String a = this.getBoard().get(i).get(k);
                    if (a.equals(this.getBoard().get(j).get(k)) && !a.equals("E")) {
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
                    if (this.getBoard().get(i).get(j).equals(this.getBoard().get(i).get(k))) {
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

    public String hash() {
        StringBuilder hash = new StringBuilder();
        hash.append("i:")
                .append(is)
                .append("j:")
                .append(js)
                .append("val:")
                .append(val);

        return hash.toString();
    }

}
