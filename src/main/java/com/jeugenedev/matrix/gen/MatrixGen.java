package com.jeugenedev.matrix.gen;

import com.jeugenedev.matrix.entity.Matrix;

import java.util.Random;

public class MatrixGen extends Matrix {
    public MatrixGen(int rows, int columns, int maxInt) {
        super(gen(rows, columns, maxInt));
    }

    public MatrixGen(int rows, int columns) {
        this(rows, columns, 256);
    }

    private static int[][] gen(int rows, int columns, int maxInt) {
        Random random = new Random();
        int[][] matrix = new int[rows][columns];
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                matrix[i][j] = random.nextInt(maxInt + 1);
            }
        }
        return matrix;
    }
}
