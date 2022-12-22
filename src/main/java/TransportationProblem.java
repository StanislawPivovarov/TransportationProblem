import java.io.File;
import java.util.*;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toCollection;

public class TransportationProblem {

    private static int[] potrebnost = new int[]{1000, 2000, 700};
    private static int[] objem = new int[]{1700, 900, 1100};
    private static double[][] cost = new double[][]{{3637, 3043, 4386},
                                                                            {3793, 3165, 4711},
                                                                            {4509, 3714, 5607}};;
    private static dostavka[][] referencePlan; // хранит стоимость перевозки, координаты в таблице
    private static dostavka trial;

    private static class dostavka {
        final double costOfTransportation;
        final int row,colum;
        double count;

        public dostavka(double count, double costOfTransportation, int row, int colum) {
            this.count = count;
            this.costOfTransportation = costOfTransportation;
            this.row = row;
            this.colum = colum;
        }

//        @Override
//        public String toString() {
//            return String.valueOf(count);
//        }
        @Override
        public String toString() {
            return "dostavka{" +
                    "costOfTransportation=" + costOfTransportation +
                    ", row=" + row +
                    ", colum=" + colum +
                    ", count=" + count +
                    '}';
        }
    }

//  МЕТОД СЕВЕРО-ЗАПАДНОГО УГЛА
    static void northWestCornerRule() {
        for (int i = 0, northwest = 0; i < objem.length; i++) {
            for (int j = northwest; j < potrebnost.length; j++) {

                int quantity = Math.min(objem[i], potrebnost[j]);
                if (quantity > 0) {
                    referencePlan[i][j] = new dostavka(quantity, cost[i][j], i, j);

                    objem[i] = objem[i] - quantity;
                    potrebnost[j] = potrebnost[j] - quantity;

                    if (objem[i] == 0) {
                        northwest = j;
                        break;
                    }
                }
            }
        }
//      Опорный план
        //
        System.out.println();
        System.out.println("Опорный план:");
        for (int i = 0; i < referencePlan.length; i++) {  //идём по строкам
            System.out.print("|");
            for (int j = 0; j < 3; j++) {//идём по столбцам
                if (referencePlan[i][j] != null)
                    System.out.print(" " + referencePlan[i][j].count + " "); //вывод элемента
                else
                    System.out.print(" null   ");
            }
            System.out.print("|");
            System.out.println();
        }
    }

    static void steppingStone() {
        double maxReduction = 0;
        dostavka[] move = null;
        dostavka leaving = null;

//      проверка на вырожденность -- опорный план, что количество базсиных ячеек должны быть равны количества спроса и количества поставок +1
        fixDegenerateCase();

        for (int row = 0; row < objem.length; row++) {
            for (int colum = 0; colum < potrebnost.length; colum++) {

                if (referencePlan[row][colum] != null)
                    continue;
//              берем элемент из матрицы
                dostavka trial = new dostavka(0, cost[row][colum], row, colum);
                dostavka[] path = getClosedPath(trial);

                double reduction = 0;
                double lowestQuantity = Integer.MAX_VALUE;
                dostavka leavingCandidate = null;

                boolean plus = true;
                for (dostavka dostavka : path) {
                    if (plus) {
                        reduction += dostavka.costOfTransportation;
                    } else {
                        reduction -= dostavka.costOfTransportation;
                        if (dostavka.count < lowestQuantity) {
                            leavingCandidate = dostavka;
                            lowestQuantity = dostavka.count;
                        }
                    }
                    plus = !plus;
                }
                if (reduction < maxReduction) {
                    move = path;
                    leaving = leavingCandidate;
                    maxReduction = reduction;
                }
            }
        }

        if (move != null) {
            double q = leaving.count;
            boolean plus = true;
            for (dostavka dostavka : move) {
                dostavka.count += plus ? q : -q;
                referencePlan[dostavka.row][dostavka.colum] = dostavka.count == 0 ? null : dostavka;
                plus = !plus;
            }
            steppingStone();
        }
    }

    static LinkedList<dostavka> matrixToList() {
        return stream(referencePlan)
                .flatMap(row -> stream(row))
                .filter(s -> s != null)
                .collect(toCollection(LinkedList::new));
    }

    static dostavka[] getClosedPath(dostavka s) {
        LinkedList<dostavka> path = matrixToList();
        path.addFirst(s);

//      удалить (и продолжать удалять) элементы, у которых нет
//      вертикальных И горизонтальных соседей
        while (path.removeIf(e -> {
            dostavka[] nbrs = getNeighbors(e, path);
            return nbrs[0] == null || nbrs[1] == null;
        })) ;

        // расположите остальные элементы в правильном порядке
        dostavka[] stones = path.toArray(new dostavka[path.size()]);
        dostavka prev = s;
        for (int i = 0; i < stones.length; i++) {
            stones[i] = prev;
            prev = getNeighbors(prev, path)[i % 2];
        }
        return stones;
    }

    static dostavka[] getNeighbors(dostavka s, LinkedList<dostavka> lst) {
        dostavka[] nbrs = new dostavka[2];
        for (dostavka o : lst) {
            if (o != s) {
                if (o.row == s.row && nbrs[0] == null)
                    nbrs[0] = o;
                else if (o.colum == s.colum && nbrs[1] == null)
                    nbrs[1] = o;
                if (nbrs[0] != null && nbrs[1] != null)
                    break;
            }
        }
        return nbrs;
    }

//    Проверка на вырожденность
    static void fixDegenerateCase() {
        final double eps = Double.MIN_VALUE;

        if (objem.length + potrebnost.length - 1 != matrixToList().size()) {
            for (int r = 0; r < objem.length; r++)
                for (int c = 0; c < potrebnost.length; c++) {
                    if (referencePlan[r][c] == null) {
                        dostavka dummy = new dostavka(eps, cost[r][c], r, c);
                        if (getClosedPath(dummy).length == 0) {
                            referencePlan[r][c] = dummy;
                            return;
                        }
                    }
                }
        }
    }

//    Вывод результата
    static void printResult(String filename) {
        double totalCosts = 0;
        System.out.println();
        System.out.println("Итоговая таблица: ");
        for (int row = 0; row < objem.length; row++) {
            for (int colum = 0; colum < potrebnost.length; colum++) {

                dostavka s = referencePlan[row][colum];
                if (s != null && s.row == row && s.colum == colum) {
                    System.out.printf(" %6s ", (int) s.count);
                    totalCosts += (s.count * s.costOfTransportation);
                } else
                    System.out.printf(" %6s ","-");
            }
            System.out.println();
        }
        System.out.println();
        System.out.printf("Стоимость транспортировки: %s%n%n", totalCosts*10000000);
    }

    public static void main(String[] args) throws Exception {

        for (String filename : new String[]{"input1.txt"}) {
            init();
            northWestCornerRule();
            steppingStone();
            printResult(filename);
        }
    }

    //Инициализация массива стоимости перевозки
    static void init() throws Exception {

        try (Scanner sc = new Scanner(new File("src/main/java/input1.txt"))) {
            int numSources = sc.nextInt();
            int numDestinations = sc.nextInt();

            List<Integer> src = new ArrayList<>();
            List<Integer> dst = new ArrayList<>();

            for (int i = 0; i < numSources; i++)
                src.add(sc.nextInt());

            for (int i = 0; i < numDestinations; i++)
                dst.add(sc.nextInt());

            int totalSrc = src.stream().mapToInt(i -> i).sum();
            int totalDst = dst.stream().mapToInt(i -> i).sum();
            if (totalSrc > totalDst)
                dst.add(totalSrc - totalDst);
            else if (totalDst > totalSrc)
                src.add(totalDst - totalSrc);
            objem = src.stream().mapToInt(i -> i).toArray();
            potrebnost = dst.stream().mapToInt(i -> i).toArray();

            cost = new double[objem.length][potrebnost.length];
            referencePlan = new dostavka[objem.length][potrebnost.length];
            for (int i = 0; i < numSources; i++)
                for (int j = 0; j < numDestinations; j++)
                    cost[i][j] = sc.nextDouble();

            for (int i = 0; i < cost.length; i++) {  //идём по строкам
                System.out.print("");
                for (int j = 0; j < 3; j++) {//идём по столбцам
                    System.out.print(" " + cost[i][j] + " "); //вывод элемента
                }
                System.out.println();
            }
        }
    }
}