package com.example.demo.algorithm;

import org.junit.jupiter.api.Test;


public class DiGui {
    /**
     * 给定一个数字数组
     * 使用递归实现：将这些数字想加并返回结果
     * 1、基准条件：数组为空或者只有一个元素
     * 2、递归条件：数组元素大于一个
     */
    public int sumArray(int[] intArrays) {
        if (intArrays == null || intArrays.length == 0) {
            intArrays = new int[]{0};
        }
        //基准条件
        if (intArrays.length == 1) {
            return intArrays[0];
        }

        return intArrays[0] + sumArray(getNewArray(intArrays));
    }

    public int[] getNewArray(int[] intArrays){
        if (intArrays == null) {
            return new int[1];
        }
        if (intArrays.length == 1) {
            return intArrays;
        }
        int[] result = new int[intArrays.length-1];
        System.arraycopy(intArrays, 1, result, 0, result.length);
        return result;
    }

    @Test
    public void sunArray(){
        int[] intArry = new int[]{2,4,6,8,10,12,14,16,18,20};
        System.out.println(sumArray(intArry));

    }

    /**
     * 给定一个数组列表
     * 使用递归实现：计算列表中包含的元素数
     * 基准条件：
     * 递归条件：
     * @param intArrays
     * @return
     */
    public int numArray(int[] intArrays){
        //基准条件

        return numArray(intArrays);
    }

}
