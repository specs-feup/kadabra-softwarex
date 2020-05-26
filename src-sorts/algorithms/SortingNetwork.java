package algorithms;

public class SortingNetwork {

    public static void sort(int[] valuesArray) {

        int i;
        int length = valuesArray.length;
        int halfLength = length / 2;
        for (i = 0; i < halfLength; i++) {

            /* odd */
            compareLevel(valuesArray, 0);

            /* even */
            compareLevel(valuesArray, 1);
        }

        if (length % 2 != 0) {

            compareLevel(valuesArray, 0);
        }
    }

    public static void sortSameFunc(int[] valuesArray) {

        int length = valuesArray.length;
        for (int i = 0; i < length; i++) {
            System.out.println(i);
            /* odd */
            for (int j = 0; j + 1 < length; j += 2) {

                if (valuesArray[j] > valuesArray[j + 1]) {

                    int temp = valuesArray[j];
                    valuesArray[j] = valuesArray[j + 1];
                    valuesArray[j + 1] = temp;
                }
            }

            /* even */
            for (int j = 1; j + 1 < length; j += 2) {

                if (valuesArray[j] > valuesArray[j + 1]) {

                    int temp = valuesArray[j];
                    valuesArray[j] = valuesArray[j + 1];
                    valuesArray[j + 1] = temp;
                }
            }

        }

        if (length % 2 != 0) {

            for (int i = 0; i + 1 < length; i += 2) {

                if (valuesArray[i] > valuesArray[i + 1]) {

                    int temp = valuesArray[i];
                    valuesArray[i] = valuesArray[i + 1];
                    valuesArray[i + 1] = temp;
                }
            }
        }
    }

    static void swapMax(int[] valuesArray, int index) {

        if (valuesArray[index] > valuesArray[index + 1]) {

            int temp = valuesArray[index];
            valuesArray[index] = valuesArray[index + 1];
            valuesArray[index + 1] = temp;
        }
    }

    static void compareLevel(int[] values, int start) {

        int i;
        int length = values.length;
        for (i = start; i + 1 < length; i += 2) {

            swapMax(values, i);
        }
    }

    static void compareAndSwap(int[] valuesArray, boolean[] mask) {

        int i;
        int maskLength = mask.length;
        for (i = 0; i < maskLength; i++) {

            if (mask[i]) {
                swapMax(valuesArray, i);
            }
        }
    }

    public static void sort6(int[] valuesArray) {

        int t_0 = valuesArray[0];
        int t_1 = valuesArray[1];
        int t_2 = valuesArray[2];
        int t_3 = valuesArray[3];
        int t_4 = valuesArray[4];
        int t_5 = valuesArray[5];

        if (t_0 > t_1) {

            int temp = t_0;
            t_0 = t_1;
            t_1 = temp;
        }

        if (t_2 > t_3) {

            int temp = t_2;
            t_2 = t_3;
            t_3 = temp;
        }

        if (t_4 > t_5) {

            int temp = t_3;
            t_3 = t_4;
            t_4 = temp;
        }

        if (t_1 > t_2) {

            int temp = t_1;
            t_1 = t_2;
            t_2 = temp;
        }

        if (t_3 > t_4) {

            int temp = t_3;
            t_3 = t_4;
            t_4 = temp;
        }

        if (t_0 > t_1) {

            int temp = t_0;
            t_0 = t_1;
            t_1 = temp;
        }

        if (t_2 > t_3) {

            int temp = t_2;
            t_2 = t_3;
            t_3 = temp;
        }

        if (t_4 > t_5) {

            int temp = t_3;
            t_3 = t_4;
            t_4 = temp;
        }

        if (t_1 > t_2) {

            int temp = t_1;
            t_1 = t_2;
            t_2 = temp;
        }

        if (t_3 > t_4) {

            int temp = t_3;
            t_3 = t_4;
            t_4 = temp;
        }

        if (t_0 > t_1) {

            int temp = t_0;
            t_0 = t_1;
            t_1 = temp;
        }

        if (t_2 > t_3) {

            int temp = t_2;
            t_2 = t_3;
            t_3 = temp;
        }

        if (t_4 > t_5) {

            int temp = t_3;
            t_3 = t_4;
            t_4 = temp;
        }

        if (t_1 > t_2) {

            int temp = t_1;
            t_1 = t_2;
            t_2 = temp;
        }

        if (t_3 > t_4) {

            int temp = t_3;
            t_3 = t_4;
            t_4 = temp;
        }

        valuesArray[0] = t_0;
        valuesArray[1] = t_1;
        valuesArray[2] = t_2;
        valuesArray[3] = t_3;
        valuesArray[4] = t_4;
        valuesArray[5] = t_5;
    }

}
