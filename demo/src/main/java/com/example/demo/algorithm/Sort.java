package com.example.demo.algorithm;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class Sort {

    @Test
    public void quickSortTest(){
        int[] intArrays = new int[]{3,5,1,9,45,14,36,25,20,33,2,24,18,8};
//        int[] intArrays = new int[]{10,5,2,3};
        System.out.println(Arrays.toString(quickSort(intArrays)));

    }

    /**
     * 快速排序
     * 基线条件：数组为空或数组只有一个元素的数组时“有序”的
     * 递归条件：
     */
    public int[] quickSort(int[] intArrays){
        if (intArrays == null || intArrays.length < 2) {
            return intArrays;
        }
//        if (intArrays.length == 2) {
//            if (intArrays[0] > intArrays[1]) {
//                int temp = intArrays[0];
//                intArrays[0] = intArrays[1];
//                intArrays[1] = temp;
//            }
//            return intArrays;
//        }
        int pivot = intArrays[intArrays.length/2];
        int[] lessArr = new int[intArrays.length];
        int[] greaterArr = new int[intArrays.length];
        int lessIndex = 0,greaterIndex = 0;
        for (int intArray : intArrays) {
            if (intArray > pivot) {
                greaterArr[greaterIndex++] = intArray;
            } else {
                lessArr[lessIndex++] = intArray;
            }
        }
        int[] lessArrays = new int[lessIndex];
        System.arraycopy(lessArr,0,lessArrays,0,lessIndex);
        System.arraycopy(quickSort(lessArrays), 0, intArrays, 0, lessIndex);

        int[] greaterArrays = new int[greaterIndex];
        System.arraycopy(greaterArr, 0, greaterArrays, 0, greaterIndex);
        System.arraycopy(quickSort(greaterArrays), 0, intArrays, lessIndex, greaterIndex);
        return intArrays;
    }
}
