/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiroparabolico;

import javax.swing.JFrame;

/**
 *
 * @author ppesq
 */
public class Juego {

    public static void main(String[] args) {
        JFrameJuego1 miJuego = new JFrameJuego1();
        miJuego.setVisible(true);
        miJuego.setSize(500, 350);
        miJuego.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
