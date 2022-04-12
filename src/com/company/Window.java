package com.company;
import javax.swing.*;


public class Window {

    private JFrame frame;

    private String title;
    private int width,height;

    Window(String title, int width, int height){
        this.title = title;
        this.width = width;
        this.height = height;

        createWindow();
    }

    private void createWindow(){
        frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public JFrame getFrame() {
        return frame;
    }
}
