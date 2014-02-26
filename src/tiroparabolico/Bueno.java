/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiroparabolico;

import java.awt.Toolkit;
import java.net.URL;

/**
 *
 * @author ppesq
 */
public class Bueno extends Base {

    private final String DESAPARECE = "DESAPARECE";
    private final String PAUSADO = "PAUSADO";
    private final int DEFAULT_SPEED = 10;

    private final int UP = 1;
    private final int RIGHT = 2;
    private final int DOWN = 3;
    private final int LEFT = 4;
    private final int STOPPED = 0;

    private int speed;
    private int direction;

    private boolean colision; //true si esta en colision
    
    //Cuadros
    private URL[] stoppedURLs = {
        this.getClass().getResource("images/ninjaStopped01_01.png"),
        this.getClass().getResource("images/ninjaStopped02_02.png")
    };
    private URL[] derechaURLs = {
        this.getClass().getResource("images/ninjaDerecha01_01.png"),
        this.getClass().getResource("images/ninjaDerecha02_02.png"),
        this.getClass().getResource("images/ninjaDerecha03_03.png"),
        this.getClass().getResource("images/ninjaDerecha04_04.png"),
        this.getClass().getResource("images/ninjaDerecha05_05.png"),
        this.getClass().getResource("images/ninjaDerecha06_06.png")
    };
    private URL[] izquierdaURLs = {
        this.getClass().getResource("images/ninjaIzquierda01_01.png"),
        this.getClass().getResource("images/ninjaIzquierda02_02.png"),
        this.getClass().getResource("images/ninjaIzquierda03_03.png"),
        this.getClass().getResource("images/ninjaIzquierda04_04.png"),
        this.getClass().getResource("images/ninjaIzquierda05_05.png"),
        this.getClass().getResource("images/ninjaIzquierda06_06.png")
    };

    /**
     * Default Constructor that loads images in the arrays mentioned above...
     */
    public Bueno() {
        speed = DEFAULT_SPEED;
        Animacion parado = new Animacion();
        Animacion derecha = new Animacion();
        Animacion izquierda = new Animacion();

        /*
         Agrega todos los cuadros a la animacion main con 100ms de duracion
         */
        for (URL url : stoppedURLs) {
            parado.sumaCuadro(Toolkit.getDefaultToolkit().getImage(url), 100);
        }
        /*
         Agrega todos los cuadros a la animacion main con 100ms de duracion
         */
        for (URL url : derechaURLs) {
            derecha.sumaCuadro(Toolkit.getDefaultToolkit().getImage(url), 100);
        }
        /*
         Agrega todos los cuadros a la animacion collision con 100ms de duracion
         */
        for (URL url : izquierdaURLs) {
            izquierda.sumaCuadro(Toolkit.getDefaultToolkit().getImage(url), 100);
        }

        //Agrega las animaciones creadas al personaje en su clase Base. 
        setAnimacionBasica(parado);
        setAnimacionCaminarDerecha(derecha);
        setAnimacionCaminarIzquierda(izquierda);

        //direccion inicial del ninja
        setCorriendoAnimacionBasica(true);
    }

    public Bueno(int posX, int posY, Animacion animacionBasica) {
        super(posX, posY, animacionBasica);
    }

    public Bueno(int posX, int posY, Animacion animacionCaminarIzquierda, Animacion animacionCaminarDerecha) {
        super(posX, posY, animacionCaminarIzquierda, animacionCaminarDerecha);
    }

    /* GETTERS y SETTERS */
    public String getPAUSADO() {
        return PAUSADO;
    }
    
    public String getDESAPARECE() {
        return DESAPARECE;
    }

    /**
     * Getters que regresan la direccion que significa cada cuadrante.
     *
     * @return
     */
    public int getUP() {
        return UP;
    }

    public int getRIGHT() {
        return RIGHT;
    }

    public int getDOWN() {
        return DOWN;
    }

    public int getLEFT() {
        return LEFT;
    }

    public int getSTOPPED() {
        return STOPPED;
    }

    /**
     * Metodo de acceso getSpeed
     *
     * regresa la velocidad del objeto
     *
     * @return variable speed de tipo <code>int</code> que contiene la velocidad
     * del objeto.
     */
    public int getSpeed() {
        return speed;

    }

    /**
     * Metodo de modificacion setSpeed
     *
     * que cambia la velocidad del objeto
     *
     * @param speed de tipo <code>int</code> que contiene la velocidad del
     * objeto.
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * Metodo de modificacion setDirection
     *
     * modifica la direccion del objeto
     *
     * @param direction variable de tipo <code>int</code> que contiene la
     * direccion en la que se mueve el objeto
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    /**
     * Metodo de acceso getDirection
     *
     * regresa la direccion del objeto
     *
     * @return variable de tipo <code>int</code> con la direccion
     */
    public int getDirection() {
        return direction;
    }

    /* GETTERS y SETTERS */

    /* COMPORTAMIENTOS */
    public void moveRight() {
        setHaciaLaIzquierda(false);
        setHaciaLaDerecha(true);
        setCorriendoAnimacionBasica(false);
    }

    public void moveLeft() {
        setHaciaLaIzquierda(true);
        setHaciaLaDerecha(false);
        setCorriendoAnimacionBasica(false);
    }

    public void stop() {
        setHaciaLaIzquierda(false);
        setHaciaLaDerecha(false);
        setCorriendoAnimacionBasica(true);
    }

    public void collide(){
        colision = true; 
    }
    public void move() {
        switch (direction) {
            case UP:
                setPosY(getPosY() - speed);
                break;
            case DOWN:
                setPosY(getPosY() + speed);
                break;
            case RIGHT:
                setPosX(getPosX() + speed);
                moveRight();
                break;
            case LEFT:
                setPosX(getPosX() - speed);
                moveLeft();
                break;
            default:
                stop();
                break;
        }

    }

}//Fin de la clase Bueno
