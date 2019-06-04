package com.ssaw.commons.code;

import java.util.Arrays;

/**
 * @author HuSen
 * @date 2019/6/3 11:02
 */
public class Demo {

    /**
     * 冒泡
     *
     * @param array 排序的数组
     */
    private static void bubbleSort(int[] array) {
        if (array.length <= 1) {
            return;
        }
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
    }

    /**
     * 选择
     *
     * @param array 排序的数组
     */
    private static void selectSort(int[] array) {
        if (array.length <= 1) {
            return;
        }
        for (int i = 0; i < array.length; i++) {
            int minIndex = i;
            for (int j = i; j < array.length; j++) {
                if (array[j] < array[minIndex]) {
                    minIndex = j;
                }
            }
            int temp = array[i];
            array[i] = array[minIndex];
            array[minIndex] = temp;
        }
    }

    /**
     * 插入
     *
     * @param array 排序的数组
     */
    private static void insertSort(int[] array) {
        if (array.length <= 1) {
            return;
        }
        int current;
        for (int i = 0; i < array.length - 1; i++) {
            current = array[i + 1];
            int preIndex = i;
            while (preIndex >= 0 && current < array[preIndex]) {
                array[preIndex + 1] = array[preIndex];
                preIndex--;
            }
            array[preIndex + 1] = current;
        }
    }

    /**
     * 希尔
     *
     * @param array 排序的数组
     */
    private static void shellSort(int[] array) {
        int length = array.length;
        if (length <= 1) {
            return;
        }
        int temp, gap = length / 2;
        while (gap > 0) {
            for (int i = gap; i < length; i++) {
                temp = array[i];
                int preIndex = i - gap;
                while (preIndex >= 0 && temp < array[preIndex]) {
                    array[preIndex + gap] = array[preIndex];
                    preIndex -= gap;
                }
                array[preIndex + gap] = temp;
            }
            gap /= 2;
        }
    }

    /**
     * 归并
     *
     * @param array 排序的数组
     * @return 排序后的数组
     */
    private static int[] mergeSort(int[] array) {
        if (array.length <= 1) {
            return array;
        }
        int mid = array.length / 2;
        int[] left = Arrays.copyOfRange(array, 0, mid);
        int[] right = Arrays.copyOfRange(array, mid, array.length);
        return merge(mergeSort(left), mergeSort(right));
    }

    private static int[] merge(int[] left, int[] right) {
        int[] result = new int[left.length + right.length];
        for (int index = 0, i = 0, j = 0; index < result.length; index++) {
            if (i >= left.length) {
                result[index] = right[j++];
            } else if (j >= right.length) {
                result[index] = left[i++];
            } else if (left[i] > right[j]) {
                result[index] = right[j++];
            } else {
                result[index] = left[i++];
            }
        }
        return result;
    }

    /**
     * 快排
     *
     * @param array 排序的数组
     * @param start 开始
     * @param end   结束
     */
    private static void quickSort(int[] array, int start, int end) {
        if (array.length < 1 || start < 0 || end >= array.length) {
            return;
        }
        int smallIndex = partition(array, start, end);
        if (smallIndex > start) {
            quickSort(array, start, smallIndex - 1);
        }
        if (smallIndex < end) {
            quickSort(array, smallIndex + 1, end);
        }
    }

    private static int partition(int[] array, int start, int end) {
        int pivot = start + (end - start + 1) / 2;
        int smallIndex = start - 1;
        swap(array, pivot, end);
        for (int i = start; i <= end; i++) {
            if (array[i] <= array[end]) {
                smallIndex++;
                if (i > smallIndex) {
                    swap(array, i, smallIndex);
                }
            }
        }
        return smallIndex;
    }

    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public static void main(String[] args) {
        int[] array = {1, 29, 12, 212, 122, 11, 4, 6, 9, 10};
        quickSort(array, 0, array.length - 1);
        System.out.println(Arrays.toString(array));
    }
}