package com.jeugenedev.matrix.gui.render;

import com.jeugenedev.matrix.entity.Matrix;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class HtmlRender implements GuiRender {
    private final Matrix matrix;

    public HtmlRender(Matrix matrix) {
        this.matrix = matrix;
    }

    @Override
    public RenderedGui render() {
        StringBuilder table = new StringBuilder("<tr>");
        for(int i = 0; i < matrix.getMatrix().length; i++) {
            table.append("<tr>");
            for(int j = 0; j < matrix.getMatrix()[i].length; j++) {
                table.append("<td>").append(matrix.getMatrix()[i][j]).append("</td>");
            }
            table.append("</tr>");
        }
        RenderedGui renderedGui;
        try(InputStream baseHtml = getClass().getClassLoader().getResourceAsStream("matrix.html")) {
            String data = new String(baseHtml.readAllBytes(), StandardCharsets.UTF_8).replace("<place_past/>", table.toString());
            renderedGui = new RenderedHtml(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return renderedGui;
    }

    private static class RenderedHtml implements RenderedGui {
        private final String data;
        private ScheduledFuture<?> scheduledFuture;

        public RenderedHtml(String data) {
            this.data = data;
        }

        private void showBrowser(File html) throws IOException, ExecutionException, InterruptedException {
            File process = new File("./.process");
            File bat = new File("./.process/%s.bat".formatted(UUID.randomUUID().toString()));
            if((process.exists() && process.isDirectory() || process.mkdir()) && bat.createNewFile()) {
                try(FileOutputStream fos = new FileOutputStream(bat)) {
                    fos.write("start chrome %s".formatted(html.getCanonicalPath()).getBytes(StandardCharsets.UTF_8));
                }
                Runtime.getRuntime().exec(bat.getCanonicalPath());
                AtomicInteger count = new AtomicInteger(3);
                scheduledFuture = Executors.newScheduledThreadPool(1).scheduleWithFixedDelay(() -> {
                    System.out.println(count.getAndDecrement());
                    if(count.get() < 1) {
                        bat.delete();
                        html.delete();
                        scheduledFuture.cancel(true);
                    }
                }, 0, 1, TimeUnit.SECONDS);
            }
            else {
                throw new IOException();
            }
        }

        @Override
        public void show() {
            File html = new File("./rendered-%s.html".formatted(UUID.randomUUID().toString()));
            try {
                if(html.createNewFile()) {
                    try(FileOutputStream fos = new FileOutputStream(html)) {
                        fos.write(data.getBytes(StandardCharsets.UTF_8));
                    }
                    showBrowser(html);
                }
                else {
                    System.out.println("Rendering failed!");
                }
            } catch (IOException | ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
