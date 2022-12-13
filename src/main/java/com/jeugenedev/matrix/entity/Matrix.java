package com.jeugenedev.matrix.entity;

import java.util.Arrays;

abstract public class Matrix {
    private final int[][] matrix;

    public Matrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    @Override
    public String toString() {
        StringBuilder matrixString = new StringBuilder();
        Arrays.stream(matrix).forEach(row -> matrixString.append(String.join(" ", Arrays.stream(row).boxed().map("%-8s"::formatted).toList())).append("\n"));
        return matrixString.toString();
    }
}
