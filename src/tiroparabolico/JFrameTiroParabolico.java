/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tiroparabolico;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.HeadlessException;
import javax.swing.JFrame;

/**
 *
 * @author ppesq
 */
public class JFrameTiroParabolico extends JFrame {
     public JFrameTiroParabolico() throws HeadlessException {
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Hello World and Git");
    }

    public void paint(Graphics g) {

        //Color blanco
        g.setColor(Color.WHITE);
        //Dibuja un rectángulo blanco para el fondo
        g.fillRect(0, 0, getWidth(), getHeight());
        //Color negro
        g.setColor(Color.BLACK);
        //Define la fuente con la cual se desplegará el mensaje
        g.setFont(new Font("Serif", Font.BOLD, 18));
        //Dibuja el mensaje en la ventana
        g.drawString("HOLA MUNDO", this.getSize().width / 2 - 60,
                this.getSize().height / 2 + 9);
    }
}
