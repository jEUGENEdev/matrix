package com.jeugenedev.matrix;

import com.jeugenedev.matrix.entity.Matrix;
import com.jeugenedev.matrix.gen.MatrixGen;
import com.jeugenedev.matrix.gui.render.HtmlRender;

public class App {
    public static void main(String[] args) {
        Matrix matrix = new MatrixGen(3, 3);
        HtmlRender htmlRender = new HtmlRender(matrix);
        htmlRender.render().show();
    }
}
