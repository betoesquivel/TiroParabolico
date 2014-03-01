package tiroparabolico;

/**
 * Clase Base
 *
 * source: Antonio Mejorado
 *
 * @author José Alberto Esquivel Patiño
 * @version 1.00 2008/6/13
 */
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Rectangle;

/**
 * Esta clase es una base para personajes dentro de un applet de java. La clase
 * debe de ser inicializada con al menos una animación no nula.
 *
 * @author ppesq
 */
public class Base {

    //control de movimiento
    private int posX;    //posicion en x.       
    private int posY;	//posicion en y.

    //control de animaciones
    private Animacion animacionBasica; //animacion principal del objeto
    private Animacion animacionCaminarIzquierda; //para caminar hacia la izquierda
    private Animacion animacionCaminarDerecha;  //para caminar hacia la derecha
    private Animacion animacionColision; //para colisionar
    private boolean corriendoAnimacionBasica; //true cuando está corriendo la animacion basica
    private boolean haciaLaDerecha; //true cuando se está moviendo hacia la derecha
    private boolean haciaLaIzquierda; //true cuando se está moviendo hacia la izquierda
    private boolean colisionando; //true cuando se está reproduciendo animacion de colision

    /* CONSTRUCTORES */
    /**
     * Metodo constructor usado para crear el objeto con valores default
     *
     */
    public Base() {
        this.posX = 0;
        this.posY = 0;
        this.animacionBasica = animacionCaminarDerecha = animacionCaminarIzquierda = animacionColision = null;

    }

    /**
     * Metodo constructor usado para crear el objeto
     *
     * @param posX es la <code>posicion en x</code> del objeto.
     * @param posY es la <code>posicion en y</code> del objeto.
     * @param image es la <code>imagen</code> del objeto.
     */
    public Base(int posX, int posY, Animacion animacionBasica) {
        this.posX = posX;
        this.posY = posY;
        this.animacionBasica = animacionBasica;
    }

    /**
     * Metodo constructor usado para crear el objeto
     *
     * @param posX es la <code>posicion en x</code> del objeto.
     * @param posY es la <code>posicion en y</code> del objeto.
     * @param image es la <code>imagen</code> del objeto.
     */
    public Base(int posX, int posY, Animacion animacionCaminarIzquierda, Animacion animacionCaminarDerecha) {
        this.posX = posX;
        this.posY = posY;
        this.animacionCaminarIzquierda = animacionCaminarIzquierda;
        this.animacionCaminarDerecha = animacionCaminarDerecha;
    }
    /* CONSTRUCTORES */

    /**
     * Checa si el objeto <code>Base</code> intersecta a otro
     * <code>Base</code>
     *
     * @return un valor boleano <code>true</code> si lo intersecta
     * <code>false</code> en caso contrario
     */
    public boolean intersecta(Base obj) {
        return getPerimetro().intersects(obj.getPerimetro());
    }
    /* COMPORTAMIENTOS */

    /**
     * Metodo de acceso que regresa un nuevo rectangulo
     *
     * @return un objeto de la clase <code>Rectangle</code> que es el perimetro
     * del rectangulo
     */
    public Rectangle getPerimetro() {
        return new Rectangle(getPosX(), getPosY(), getAncho(), getAlto());
    }
    
    public boolean isClicked(int x, int y) {
        return this.getPerimetro().contains(x, y);
    }

    public void updateAnimation(long tiempoTranscurrido) {
        Animacion anim = new Animacion();
        if (isCorriendoAnimacionBasica()) {
            anim = animacionBasica;
        } else if (isHaciaLaDerecha()) {
            anim = animacionCaminarDerecha;
        } else if (isHaciaLaIzquierda()) {
            anim = animacionCaminarIzquierda;
        } else if (isColisionando()) {
            anim = animacionColision;
        }
        //Actualiza la animación en base al tiempo transcurrido
        anim.actualiza(tiempoTranscurrido);
    }
    /* COMPORTAMIENTOS */

    /* GETTERS Y SETTERS */
    /**
     * Metodo getPosX Regresa la posición en x del objeto
     *
     * @return posX que es la posición X de tipo <code>int</code>
     */
    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    /**
     * Metodo getPosY Regresa la posición en y del objeto
     *
     * @return posY que es la posición Y de tipo <code>int</code>
     */
    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    /**
     * Regresa la imagen del cuadro actual animándose.
     *
     * @return Imagen de tipo <code>Image</code>
     */
    public Image getImagen() {
        if (corriendoAnimacionBasica) {
            return animacionBasica.getImagen();
        } else if (haciaLaDerecha) {
            return animacionCaminarDerecha.getImagen();
        } else if (haciaLaIzquierda) {
            return animacionCaminarIzquierda.getImagen();
        } else {
            return null;
        }
    }

    /**
     * Metodo de acceso que regresa el ancho del icono
     *
     * @return un objeto de la clase <code>ImageIcon</code> que es el ancho del
     * icono.
     */
    public int getAncho() {
        return (new ImageIcon(getImagen())).getIconWidth();
    }

    /**
     * Metodo de acceso que regresa el alto del icono
     *
     * @return un objeto de la clase <code>ImageIcon</code> que es el alto del
     * icono.
     */
    public int getAlto() {
        return (new ImageIcon(getImagen())).getIconHeight();
    }

    /**
     * Metodo de acceso que regresa la animacionBasica
     *
     * @return un objeto de la clase <code>Animacion</code> que tiene la
     * animación básica.
     */
    public Animacion getAnimacionBasica() {
        return animacionBasica;
    }

    public void setAnimacionBasica(Animacion animacionBasica) {
        this.animacionBasica = animacionBasica;
    }

    /**
     * Metodo de acceso que regresa la animacion de caminata hacia la izquierda
     *
     * @return objeto de la clase <code>Animacion</code> que tiene la animación
     * para caminar hacia la izquierda
     */
    public Animacion getAnimacionCaminarIzquierda() {
        return animacionCaminarIzquierda;
    }

    public void setAnimacionCaminarIzquierda(Animacion animacionCaminarIzquierda) {
        this.animacionCaminarIzquierda = animacionCaminarIzquierda;
    }

    /**
     * Metodo de acceso que regresa la animacion de caminata hacia la derecha
     *
     * @return objeto de la clase <code>Animacion</code> que tiene la animación
     * para caminar hacia la derecha
     */
    public Animacion getAnimacionCaminarDerecha() {
        return animacionCaminarDerecha;
    }

    public void setAnimacionCaminarDerecha(Animacion animacionCaminarDerecha) {
        this.animacionCaminarDerecha = animacionCaminarDerecha;
    }

    /**
     * Metodo de acceso que regresa la animacion de colisión
     *
     * @return objeto de la clase <code>Animacion</code> que tiene la animación
     * para colisionar.
     */
    public Animacion getAnimacionColision() {
        return animacionColision;
    }

    /**
     * Metodo setAnimacionColision
     *
     * @param animacionColision
     */
    public void setAnimacionColision(Animacion animacionColision) {
        this.animacionColision = animacionColision;
    }

    public boolean isCorriendoAnimacionBasica() {
        return corriendoAnimacionBasica;
    }

    /**
     * Metodo de modificación para el boolean que dice si se está corriendo una
     * animación básica
     *
     * @param corriendoAnimacionBasica es una variable de tipo
     * <code>boolean</code> que indica el valor que la booleana de clase debe
     * tomar.
     */
    public void setCorriendoAnimacionBasica(boolean corriendoAnimacionBasica) {
        this.corriendoAnimacionBasica = corriendoAnimacionBasica;
    }

    public boolean isHaciaLaDerecha() {
        return haciaLaDerecha;
    }

    /**
     * Metodo de modificación para el boolean que dice si se está corriendo una
     * animación de caminar hacia la derecha
     *
     * @param haciaLaDerecha es una variable de tipo <code>boolean</code> que
     * indica el valor que la booleana de clase debe tomar.
     */
    public void setHaciaLaDerecha(boolean haciaLaDerecha) {
        this.haciaLaDerecha = haciaLaDerecha;
    }

    public boolean isHaciaLaIzquierda() {
        return haciaLaIzquierda;
    }

    /**
     * Metodo de modificación para el boolean que dice si se está corriendo una
     * animación de caminar hacia la izquierda
     *
     * @param haciaLaIzquierda es una variable de tipo <code>boolean</code> que
     * indica el valor que la booleana de clase debe tomar.
     */
    public void setHaciaLaIzquierda(boolean haciaLaIzquierda) {
        this.haciaLaIzquierda = haciaLaIzquierda;
    }

    public boolean isColisionando() {
        return colisionando;
    }

    /**
     * Metodo de modificación para el boolean que dice si se está corriendo una
     * animación de colisión
     *
     * @param colisionando es una variable de tipo <code>boolean</code> que
     * indica el valor que la booleana de clase debe tomar.
     */
    public void setColisionando(boolean colisionando) {
        this.colisionando = colisionando;
    }
    /* FIN DE GETTERS Y SETTERS */

}// Fin de la clase Base
