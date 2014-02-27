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
public class Malo extends Base {

    //Variable de clase contador
    private static int cont = -1;

    //Variable con el lado por el que va a entrar (izquierda 1 o derecha 2)
    private int lado;
    private final int DEFAULT_LADO = 1;
    private final int DEFAULT_X = 55; //posicionado encima de la plataforma
    private final int DEFAULT_Y = 185;
    private final double G = 10.0; //representa la constante de la gravedad. 
    private int despVerticalMaximo; //para que no tope con la parte superior del jframe
    private int despHorizontalMaximo; //para que no tope con la parte lateral del Jframe
    private boolean hurled;

    //Control de movimiento
    private int speed;
    private int initialSpeed; //mandatory
    private int verticalSpeed; //optional
    private int horizontalSpeed; //optional
    private double angulo; //mandatory
    private final int DEFAULT_SPEED = 10;
    private int direccion;

    //Control de colisiones
    private boolean inCollision;
    private int collisionCycles;
    private final int defaultCollisionCycles = 10;

    //URLs de cuadros de las animaciones
    private URL[] burgerURLs = {
        this.getClass().getResource("images/frame_000.gif"),
        this.getClass().getResource("images/frame_001.gif"),
        this.getClass().getResource("images/frame_002.gif"),
        this.getClass().getResource("images/frame_003.gif"),
        this.getClass().getResource("images/frame_004.gif"),
        this.getClass().getResource("images/frame_005.gif")
    };
    private URL[] umbrellaCollisionURLs = {
        this.getClass().getResource("images/umbrellaCollision01_01.png"),
        this.getClass().getResource("images/umbrellaCollision02_02.png")
    };

    /**
     * Default constructor that loads images from the arrays mentioned above.
     */
    public Malo() {
        if (cont < 0) {
            cont = 0;
        } else {
            //modifico el contador? 
        }
        Animacion main = new Animacion();
        Animacion collision = new Animacion();
        lado = DEFAULT_LADO;
        inCollision = false;
        collisionCycles = -1;
        /*
         Agrega todos los cuadros a la animacion main con 100ms de duracion
         */
        for (URL burger : burgerURLs) {
            main.sumaCuadro(Toolkit.getDefaultToolkit().getImage(burger), 100);
        }
        /*
         Agrega todos los cuadros a la animacion collision con 100ms de duracion
         */
        for (URL coll : umbrellaCollisionURLs) {
            collision.sumaCuadro(Toolkit.getDefaultToolkit().getImage(coll), 100);
        }

        //Agrega las animaciones creadas al personaje en su clase Base. 
        setAnimacionBasica(main);
        setAnimacionColision(collision);

        //direccion inicial del malo
        setCorriendoAnimacionBasica(true);
        hurled = false;
    }

    public Malo(int posX, int posY, Animacion animacionBasica) {
        super(posX, posY, animacionBasica);
    }

    public Malo(int posX, int posY, Animacion animacionCaminarIzquierda, Animacion animacionCaminarDerecha) {
        super(posX, posY, animacionCaminarIzquierda, animacionCaminarDerecha);
    }

    /* COMPORTAMIENTOS */
    /**
     * Metodo collide que actualiza la posicion del paraguas y
     *
     */
    public void collide(int appletWidth) {
        randomReset(appletWidth);
    }

    /**
     * Metodo collide que pone al objeto en estado de colision.
     *
     */
    public void collideSides() {
//        setColisionando(true);
//        setCorriendoAnimacionBasica(false);
        setCollisionCycles(defaultCollisionCycles);
        inCollision = true;
    }

    public void decreaseCollisionCounter() {
        setCollisionCycles(getCollisionCycles() - 1);
    }

    /**
     * Método fall
     *
     * Modifica la posición del objeto malo, aumentando su posición en Y para
     * que caiga.
     */
    public void fall() {
        setPosY(getPosY() + speed);
    }

    /**
     * Metodo hurl que lanza al objeto malo en un tiro parabólico.
     *
     */
    public void randomHurl() {
        //calcular limite minimo y maximo que la velocidad random puede tomar
        //para que no se pase en desplazamiento en Y o en X.
        //Con el limite menor y 0 calcular un valor random.
        //calcular los limites aceptados de ángulos que puedo tener
        //con esos limites obtener un ángulo random
        angulo = 45.0;
        initialSpeed = setRandomSpeed(2, 10);
        setHurled(true);
    }

    /**
     * Método move
     *
     * Modifica la posición del objeto malo, aumentando su posición en X para
     * que caiga.
     */
    public void move(int t) {
        //aqui se actualiza la posicion en x y y de la hamburguesa. 
        //        x = vx0t = v0 (cos q0 )t
//        y =  vy0t - ½gt2 = v0 (sen q0)t - ½ gt2
        if (isHurled()) {
            double newX = initialSpeed * Math.cos(angulo) * t * 1.0;
            double newY = (initialSpeed * Math.sin(angulo) * t * 1.0 - ((1 / 2) * G * t * t * 1.0));

            setPosX(DEFAULT_X + (int) Math.round(newX));
            setPosY(DEFAULT_Y - (int) Math.round(newY));
        }

    }

    /**
     * Metodo setRandomSpeed
     *
     * Cambia la velocidad del objeto a un numero aleatorio entre los parametros
     * enviados.
     *
     * @param lower velocidad minima de tipo <code>int</code>
     * @param upper velocidad maxima de tipo <code>int</code>
     */
    public int setRandomSpeed(int lower, int upper) {
        int R = (int) (Math.random() * (upper - lower)) + lower;
        return R;
    }

    /**
     * Metodo randomReset Resetea la posicion del objeto afuera del applet en
     * todo X
     *
     * @param appletWidth
     */
    public void randomReset(int appletWidth) {
        //formula random
        //Math.random() * (upper - lower)) + lower

        //posiciona al objeto en su mitad
        if (getLado() == 1) {
            setPosX((int) (Math.random() * (appletWidth / 2 - 1)));
        } else {
            setPosX((int) (Math.random() * (appletWidth - appletWidth / 2) + appletWidth / 2));
        }
        //corrige la posicion si se paso
        if (getPosX() > appletWidth - getAncho()) {
            //correct displacement out of screen
            setPosX(appletWidth - getAncho());
        }
        setPosY((int) (Math.random() * -200));
    }

    public void resetPosition() {
        setPosX(DEFAULT_X);
        setPosY(DEFAULT_Y);
    }

    /**
     * Metodo randomReset Resetea la posicion del objeto afuera del applet en
     * todo Y
     *
     * @param appletWidth
     */
    public void randomResetSide(int appletHeight, int appletWidth) {
        //formula random
        //Math.random() * (upper - lower)) + lower

        //posiciona al objeto en su mitad
        if (getLado() == 1) {
//            setPosY((int) (Math.random() * (appletHeight - 15)) + 15);
            setPosX((int) (Math.random() * (0 - appletWidth / 4 - appletWidth / 4)));
        } else {
//            setPosY((int) (Math.random() * (appletHeight - appletHeight / 2) + appletHeight / 2));
            setPosX((int) (Math.random() * (appletWidth + appletWidth / 4 - appletWidth) + appletWidth));
        }

        //el +15 es porque ahora el limite inferior en y no es 0 por la barra que agrega el JFrame a la aplicación.
        setPosY((int) (Math.random() * (appletHeight - 15)) + 15);

        //corrige la posicion si se paso
        if (getPosY() > appletHeight - getAlto()) {
            //correct displacement out of screen
            setPosY(appletHeight - getAlto());
        }

//        setColisionando(false);
//        setCorriendoAnimacionBasica(true);
        setCollisionCycles(-1);
        setInCollision(false);
    }

    /* COMPORTAMIENTOS */
    /* SETTERS Y GETTERS */
    public void setDespVerticalMaximo(int n) {
        this.despVerticalMaximo = n;
    }

    public void setDespHorizontalMaximo(int n) {
        this.despHorizontalMaximo = n;
    }

    public void setInCollision(boolean b) {
        this.inCollision = b;
    }

    public boolean isInCollision() {
        return this.inCollision;
    }

    public boolean isHurled() {
        return hurled;
    }

    public void setHurled(boolean hurled) {
        this.hurled = hurled;
    }

    public int getInitialSpeed() {
        return initialSpeed;
    }

    public void setInitialSpeed(int initialSpeed) {
        this.initialSpeed = initialSpeed;
    }

    public double getAngulo() {
        return angulo;
    }

    public void setAngulo(double angulo) {
        this.angulo = angulo;
    }

    /**
     * Metodo de modificacion setCont
     *
     * que modifica el valor de la variable lado
     *
     * @param c variable de tipo <code>int</code> con nuevo score
     */
    public void setCont(int c) {
        this.cont = c;
    }

    /**
     * Metodo de acceso getCont
     *
     * que regresa
     *
     * @return variable de tipo <code>int</code> llamada cont con score
     */
    public int getCont() {
        return cont;
    }

    public int getCollisionCycles() {
        return collisionCycles;
    }

    public void setCollisionCycles(int collisionCycles) {
        this.collisionCycles = collisionCycles;
    }

    /**
     * Metodo de modificacion setLado
     *
     * que modifica el valor de la variable lado
     *
     * @param lado variable de tipo <code>int</code> que puede ser o por la
     * mitad izquierda (1) o por la mitad derecha (2)
     */
    public void setLado(int lado) {
        this.lado = lado;
    }

    /**
     * Metodo de acceso getLado
     *
     * que regresa
     *
     * @return variable de tipo <code>int</code> llamada lado con lugar por
     * donde sale
     */
    public int getLado() {
        return lado;
    }

    /**
     * Metodo getSpeed regresa la velocidad del objeto
     *
     * @return speed de tipo <code>int</code>
     */
    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * Metodo getUmbrellaURLS regresa URLs de imágenes animación básica de
     * umbrella.
     *
     * @return burgerURLs <code>URL[]</code>
     */
    public URL[] getUmbrellaURLs() {
        return burgerURLs;
    }

    public void setUmbrellaURLs(URL[] umbrellaURLs) {
        this.burgerURLs = umbrellaURLs;
    }

    /**
     * Metodo getUmbrellaCOllisionURLs Regresa el arrgeglo con URLs de imagenes
     * de colisión
     *
     * @return umbrellaCollisionURLs tipo <code>URL[]</code>
     */
    public URL[] getUmbrellaCollisionURLs() {
        return umbrellaCollisionURLs;
    }

    public void setUmbrellaCollisionURLs(URL[] umbrellaCollisionURLs) {
        this.umbrellaCollisionURLs = umbrellaCollisionURLs;
    }
    /* SETTERS Y GETTERS */

}
