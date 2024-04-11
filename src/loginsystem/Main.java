/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package loginsystem;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JFrame;

/**
 *
 * @author jonat
 */
public class Main {

    public static void main(String args[]) {
        LoginFrame d = new LoginFrame();
        JFrame frame = new JFrame();
        frame.setSize(800, 800);
        frame.add(d);
        frame.setVisible(true);
    }
}
