/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tiroparabolico;

/**
 * Indicaciones: 
 * -> lo de guardar lo hace Beto.
 * En jugando con java se ve en Score como guardar el juego en disco. Algo que va a
 * pasar cuando se presione g. Solamente se puede guardar un juego. El juego se va
 * a cargar cuando se presione la letra c. 
 * 
 * Tambien tenemos que agregar lo de pausa. 
 * 
 * Agregar fisica... -> Lo de física lo hace Hugo.
 * 
 * Recursos: Hay que conseguir una pelotita de sprite. Y también las plataformas.
 * o podemos pintar rectangulos... 
 * |-> podemos hacerlo con un jugador de basquetbol, un balon de basquetbol y una canasta
 *      le haces click al jugador y sale el balon en tiro parabolico y debe atraparlo la canasta
 * @author ppesq
 */
public class TiroParabolico {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //José Alberto Esquivel A01139626   
        //Hugo León Garza A01139720
        JFrameTiroParabolico juegoTiro = new JFrameTiroParabolico();
        juegoTiro.setVisible(true);
    }
    
}
