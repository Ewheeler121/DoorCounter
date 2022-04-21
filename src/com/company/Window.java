package com.company;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;


public class Window {

    private JFrame frame;

    private String title;
    private int width,height;

    private BufferedImage image = null;

    JTextArea text;

    JLabel label;

    private JPanel panel;
    private JButton button1;
    private JButton button2;
    private JButton button3;

    private CardWriter writer = new CardWriter();
    private Decryptor decryptor = new Decryptor("password123");

    private File f;

    private JFileChooser chooser;

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

        panel = new JPanel();
        text = new JTextArea("load a image",1,20);
        label = new JLabel();

        button1 = new JButton("Choose File");
        button2 = new JButton("Output text");
        button3 = new JButton("our Evil option >:)");

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooser = new JFileChooser();
                chooser.showDialog(frame, "open Picture");
                File f = chooser.getSelectedFile();

                try{
                    image = ImageIO.read(f);

                    String buff = "";
                    char[] buffer = writer.getMessageFromImage(image);
                    char[] buffer2 = decryptor.decrypt(buffer);
                    for(int i = 0; i < buffer.length; i++){
                        buff += buffer2[i];
                    }

                    text.setText(buff);

                }catch(Exception exception){
                    image = null;
                    JOptionPane.showMessageDialog(null,
                            "File Chosen is not a PNG",
                            "Error",
                            JOptionPane.INFORMATION_MESSAGE);
                }

            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(image != null){
                    try{
                        writer.changeText(decryptor.encrypt(text.getText()),new File(f.getAbsoluteFile() + "troll.png"));
                    }catch (Exception exception){
                        JOptionPane.showMessageDialog(null,
                                "Error could not output: " + exception.getMessage(),
                                "Error",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                }else{
                    JOptionPane.showMessageDialog(null,
                            "Chose a File",
                            "Error",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        panel.add(button1);
        panel.add(text);
        panel.add(button2);
        panel.add(button3);
        frame.add(panel);
        frame.pack();
    }

    public JFrame getFrame() {
        return frame;
    }
}
