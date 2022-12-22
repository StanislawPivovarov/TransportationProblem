import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.Objects;

// ����� ������������
public abstract class Main {
    public static Integer[][] cost = new Integer[][]{{3637, 3043, 4386},
                                                                            {3793, 3783, 4711},
                                                                            {4509, 3714, 5607}}; //  �����������
    public static Integer[][] referencePlanMinEl = new Integer[][]{{0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}}; // ������� ���� ��� ������ � �����������

    public static Integer[][] countMinInArray = new Integer[][]{{0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}}; //���������� ����������� ���������
//    public static Integer[][] referencePlanDoublePredpoch = new Integer[][]{{0, 0, 0},
//            {0, 0, 0},
//            {0, 0, 0}}; //�����
//    public static Integer[][] newReferencePlan = new Integer[][]{{0, 0, 0},
//            {0, 0, 0},
//            {0, 0, 0}};
    public static Integer[] objem = new Integer[]{1800, 1700, 400}; //������������ � �����
    public static Integer[] potrebnost = new Integer[]{1300, 900, 1700};


    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {  //��� �� ������� ������� ���������
            for (int j = 0; j < 3; j++) {//��� �� �������� ������� ���������
                System.out.print(" " + cost[i][j] + " "); //����� ����� �������
            }
            System.out.println();//������� ������ ���� ����������� ���������� ��������� �����
        }
        minEl();
    }
//
//    private static void minOne() {
//        countMinInArray[0][0] = -1;
//        countMinInArray[0][2] = -1;
//        countMinInArray[1][1] = -1;
//    }

// ������ 5 ���, ���� ��������, ��� ��� �� 6 ��� ������ �� �����
    private static void minEl() {
        int min = minElementInArray(cost); // ����������� ������� �� ��������� ������� ����������
        int max = maxElementInArray(cost); // ������������ ������� �� ��������� ������� ����������
        System.out.println("1");

        for (int i = 0; i < cost.length; i++) {  //��� �� �������
            for (int j = 0; j < cost.length; j++) {//��� �� ��������
                if (cost[i][j] == min) {
                    if (!potrebnost[j].equals(objem[i])) { // ������� ���� �� ������ ����� � �����
                        if (potrebnost[j] > objem[i]) { // �������, ��� �� ��� ������ �������� ��� ������
                            potrebnost[j] = potrebnost[j] - objem[i]; // ���, ��� ������ -- ��������
                            for (int k = 0; k < referencePlanMinEl.length; k++) {
                                if (referencePlanMinEl[i][k] <= 0) { //��������� ������� ����. ���� 0, �� -1, ������ ���������
                                    referencePlanMinEl[i][k] = -1;
                                    cost[i][k] = max; // ��� ��� ����� �����������, ������ ���, ����� �� ��� ������� ��� ���� ��������
                                }
                            }
                            referencePlanMinEl[i][j] = objem[i]; // objem ���������� ������, ����������� ���������
                            objem[i] = 0; // ������� ����� ����������
                        } else {
                            // ���� �������� ������ ���� � ��������� ������
                            objem[i] = objem[i] - potrebnost[j];
                            referencePlanMinEl[i][j] = potrebnost[j];
                            for (int k = 0; k < referencePlanMinEl.length; k++) {
                                if (referencePlanMinEl[k][j] <= 0) {
                                    referencePlanMinEl[k][j] = -1;
                                    cost[k][j] = max;
                                }
                            }
                            referencePlanMinEl[i][j] = potrebnost[j];
                            potrebnost[j] = 0;
                        }
                    } else {
                        // ���� �����
                        referencePlanMinEl[i][j] = potrebnost[j];
                        potrebnost[j] = 0;
                        objem[i] = 0;
                        for (int k = 0; k < referencePlanMinEl.length; k++) {
                            if (referencePlanMinEl[k][j] <= 0) {
                                referencePlanMinEl[k][j] = -1;
                                cost[k][j] = max;
                            }
                        }
                    }
//                ����� ��� ������� ��� ��������� ������� �� ����������
                    cost[i][j] = max;
                }
            }
        }
        System.out.println(Arrays.toString(potrebnost));
        System.out.println(Arrays.toString(objem));
        System.out.println();

        soutKeng();
//        2
        min = minElementInArray(cost);
        System.out.println("2");
        for (int i = 0; i < cost.length; i++) {  //��� �� �������
            for (int j = 0; j < cost.length; j++) {//��� �� ��������
                if (cost[i][j] == min) {
                    if (!potrebnost[j].equals(objem[i])) {
                        if (potrebnost[j] > objem[i]) {
                            potrebnost[j] = potrebnost[j] - objem[i];
                            for (int k = 0; k < referencePlanMinEl.length; k++) {
                                if (referencePlanMinEl[i][k] <= 0) {
                                    referencePlanMinEl[i][k] = -1;
                                    cost[i][k] = max;
                                }
                            }
                            referencePlanMinEl[i][j] = objem[i];
                            objem[i] = 0;
                        } else {
                            objem[i] = objem[i] - potrebnost[j];
                            referencePlanMinEl[i][j] = potrebnost[j];
                            for (int k = 0; k < referencePlanMinEl.length; k++) {
                                if (referencePlanMinEl[k][j] <= 0) {
                                    referencePlanMinEl[k][j] = -1;
                                    cost[k][j] = max;
                                }
                            }
                            referencePlanMinEl[i][j] = potrebnost[j];
                            potrebnost[j] = 0;
                        }
                    } else {
                        referencePlanMinEl[i][j] = potrebnost[j];
                        potrebnost[j] = 0;
                        objem[i] = 0;
                        for (int k = 0; k < referencePlanMinEl.length; k++) {
                            if (referencePlanMinEl[k][j] <= 0) {
                                referencePlanMinEl[k][j] = -1;
                                cost[k][j] = max;
                            }
                        }
                    }
//                ����� ��� ������� ��� ��������� ������� �� ����������
                    cost[i][j] = max;
                }
            }
        }

        System.out.println(Arrays.toString(potrebnost));
        System.out.println(Arrays.toString(objem));
        System.out.println();

        soutKeng();

//      3
        min = minElementInArray(cost);
        System.out.println("3");
        for (int i = 0; i < cost.length; i++) {  //��� �� �������
            for (int j = 0; j < cost.length; j++) {//��� �� ��������
                if (cost[i][j] == min) {
                    if (!potrebnost[j].equals(objem[i])) {
                        if (potrebnost[j] > objem[i]) {
                            potrebnost[j] = potrebnost[j] - objem[i];
                            for (int k = 0; k < referencePlanMinEl.length; k++) {
                                if (referencePlanMinEl[i][k] <= 0) {
                                    referencePlanMinEl[i][k] = -1;
                                    cost[i][k] = max;
                                }
                            }
                            referencePlanMinEl[i][j] = objem[i];
                            objem[i] = 0;
                        } else {
                            objem[i] = objem[i] - potrebnost[j];
                            referencePlanMinEl[i][j] = potrebnost[j];
                            for (int k = 0; k < referencePlanMinEl.length; k++) {
                                if (referencePlanMinEl[k][j] <= 0) {
                                    referencePlanMinEl[k][j] = -1;
                                    cost[k][j] = max;
                                }
                            }
                            referencePlanMinEl[i][j] = potrebnost[j];
                            potrebnost[j] = 0;
                        }
                    } else {
                        referencePlanMinEl[i][j] = potrebnost[j];
                        potrebnost[j] = 0;
                        objem[i] = 0;
                        for (int k = 0; k < referencePlanMinEl.length; k++) {
                            if (referencePlanMinEl[k][j] <= 0) {
                                referencePlanMinEl[k][j] = -1;
                                cost[k][j] = max;
                            }
                        }
                    }
//                ����� ��� ������� ��� ��������� ������� �� ����������
                    cost[i][j] = max;
                }
            }
        }

        System.out.println(Arrays.toString(potrebnost));
        System.out.println(Arrays.toString(objem));
        System.out.println();

        soutKeng();

//      4
        min = minElementInArray(cost);
        System.out.println("4");
        for (int i = 0; i < cost.length; i++) {  //��� �� �������
            for (int j = 0; j < cost.length; j++) {//��� �� ��������
                if (cost[i][j] == min) {
                    if (!potrebnost[j].equals(objem[i])) {
                        if (potrebnost[j] > objem[i]) {
                            potrebnost[j] = potrebnost[j] - objem[i];
                            for (int k = 0; k < referencePlanMinEl.length; k++) {
                                if (referencePlanMinEl[i][k] <= 0) {
                                    referencePlanMinEl[i][k] = -1;
                                    cost[i][k] = max;
                                }
                            }
                            referencePlanMinEl[i][j] = objem[i];
                            objem[i] = 0;
                        } else {
                            objem[i] = objem[i] - potrebnost[j];
                            referencePlanMinEl[i][j] = potrebnost[j];
                            for (int k = 0; k < referencePlanMinEl.length; k++) {
                                if (referencePlanMinEl[k][j] <= 0) {
                                    referencePlanMinEl[k][j] = -1;
                                    cost[k][j] = max;
                                }
                            }
                            referencePlanMinEl[i][j] = potrebnost[j];
                            potrebnost[j] = 0;
                        }
                    } else {
                        referencePlanMinEl[i][j] = potrebnost[j];
                        potrebnost[j] = 0;
                        objem[i] = 0;
                        for (int k = 0; k < referencePlanMinEl.length; k++) {
                            if (referencePlanMinEl[k][j] <= 0) {
                                referencePlanMinEl[k][j] = -1;
                                cost[k][j] = max;
                            }
                        }
                    }
//                ����� ��� ������� ��� ��������� ������� �� ����������
                    cost[i][j] = max;
                }
            }
        }

        System.out.println(Arrays.toString(potrebnost));
        System.out.println(Arrays.toString(objem));
        System.out.println();


//      5
        min = minElementInArray(cost);

        for (int i = 2; i < cost.length; i++) {  //��� �� �������
            for (int j = 2; j < cost.length; j++) {//��� �� ��������
                if (cost[i][j] == min) {
                    if (potrebnost[j] > objem[i]) {
                        potrebnost[j] = potrebnost[j] - objem[i];
                        for (int k = 0; k < referencePlanMinEl.length; k++) {
                            if (referencePlanMinEl[i][k] <= 0) {
                                referencePlanMinEl[i][k] = -1;
                                cost[i][k] = max;
                            }
                        }
                        referencePlanMinEl[i][j] = objem[i];
                    } else {
                        objem[i] = objem[i] - potrebnost[j];
                        referencePlanMinEl[i][j] = potrebnost[i];
                        for (int k = 0; k < referencePlanMinEl.length; k++) {
                            if (referencePlanMinEl[k][j] <= 0) {
                                referencePlanMinEl[k][j] = -1;
                                cost[k][j] = max;
                            }
                        }
                        referencePlanMinEl[i][j] = potrebnost[i];
                    }
//                ����� ��� ������� ��� ��������� ������� �� ����������
                    cost[i][j] = max;
                }
            }
        }

        soutKeng();
    }

    private static void soutKeng() {
        //����� �������� �����
        System.out.println("===================================");
        for (int i = 0; i < referencePlanMinEl.length; i++) {  //��� �� �������
            System.out.print("|");
            for (int j = 0; j < 3; j++) {//��� �� ��������
                System.out.print(" " + referencePlanMinEl[i][j] + " "); //����� �������
            }
            System.out.print("|");
            System.out.println();//������� ������ ���� ����������� ���������� ��������� �����
        }
        System.out.println(
                referencePlanMinEl[0][0] * cost[0][0] +
                        referencePlanMinEl[1][0] * cost[1][0] +
                        referencePlanMinEl[0][1] * cost[0][1] +
                        referencePlanMinEl[1][2] * cost[1][2] +
                        referencePlanMinEl[2][2] * cost[2][2]
        );
        System.out.println("===================================");
    }

    private static int maxElementInArray(Integer[][] b) {
        IntSummaryStatistics stats = Arrays.stream(b)
                .flatMap(Arrays::stream)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .summaryStatistics();

        return stats.getMax();
    }

    private static int minElementInArray(Integer[][] b) {
        IntSummaryStatistics stats = Arrays.stream(b)
                .flatMap(Arrays::stream)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .summaryStatistics();

        return stats.getMin();
    }
}
