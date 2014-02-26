/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiroparabolico;

/**
 *
 * @author ppesq
 */
import java.applet.AudioClip;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.LinkedList;
import javax.swing.JFrame;

/**
 * El applet AppletAnimacion muestra una animación en pantalla.
 */
public class JFrameJuego1 extends JFrame implements Runnable, KeyListener, MouseListener {

    private Image dbImage;    // Imagen a proyectar
    private Graphics dbg;	// Objeto grafico
    private Image background;
    private URL backgroundURL = this.getClass().getResource("images/PlataformaYPaisaje.png");
    

    //Personajes en el juego
    private Bueno ninja;    //objeto bueno, controlable con el teclado
    private LinkedList<Malo> malos; //lista de malos

    //marcador y pausa
    private int score;
    private boolean pausado;
    private int cuadranteOprimido;
    //Variables de control de tiempo de la animación
    private long tiempoActual;
    private long tiempoInicial;
    int posX, posY;

    //sonidos
    private SoundClip sonido;    // Objeto AudioClip
    private SoundClip bomb;    //Objeto AudioClip 
    //Se cargan los sonidos.
    private String saURL = "sound/8-bit-explosion.wav";
    private String baURL = "sound/Explosion.wav";

    public JFrameJuego1() {
        init();
        start();
    }

    
    
    
    /**
     * El método init() crea la animación que se mostrará en pantalla.
     */
    public void init() {
        ninja = new Bueno();
        
        this.setSize(500,350);
        //genera lista de malos de tamanio aleatorio
        //puede ser 6, 10 o 12
        malos = generateRandomMaloList(6, 10, 12);
        ninja.setPosX(getWidth() / 2 - ninja.getAncho() / 2);
        ninja.setPosY(getHeight() / 2 - ninja.getAlto() / 2);

        //inicializo el marcador en 0
        score = 0;

        //el juego no esta pausado
        pausado = false;

        //se cargan los sonidos
        sonido = new SoundClip(saURL);
        bomb = new SoundClip(baURL);

        background = Toolkit.getDefaultToolkit().getImage(backgroundURL);
        //Pinta el fondo del Applet con una imagen		
        setBackground(Color.white);
        addKeyListener(this);
        addMouseListener(this);
    }

    //El método start() inicializa el thread que utiliza el Applet
    public void start() {

        //Crea el thread
        Thread hilo = new Thread(this);
        //Inicializa el thread
        hilo.start();
    }

    /**
     * Metodo stop sobrescrito de la clase Applet. En este metodo se pueden
     * tomar acciones para cuando se termina de usar el Applet. Usualmente
     * cuando el usuario sale de la pagina en donde esta este Applet.
     */
    public void stop() {

    }

    /**
     * Metodo destroy sobrescrito de la clase Applet. En este metodo se toman
     * las acciones necesarias para cuando el Applet ya no va a ser usado.
     * Usualmente cuando el usuario cierra el navegador.
     */
    public void destroy() {

    }

    /**
     * El método run() manda a llamar los métodos atualiza() y repaint(),
     * nesecarios para actualizar y mostrar la animación en pantalla.
     */
    public void run() {

        //Guarda el tiempo actual del sistema
        tiempoActual = System.currentTimeMillis();

        //Ciclo principal del Applet. Actualiza y despliega en pantalla la animación hasta que el Applet sea cerrado
        while (true) {
            if (!pausado) {
                //Actualiza la animación
                actualiza();

                //Manda a llamar checa colision
                checaColision();
            }
            //Manda a llamar al método paint() para mostrar en pantalla la animación
            repaint();

            //Hace una pausa de 100 milisegundos
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
        }

    }

    /**
     * El método actualiza() actualiza la animación
     */
    public void actualiza() {

        //Determina el tiempo que ha transcurrido desde que el Applet inicio su ejecución
        long tiempoTranscurrido
                = System.currentTimeMillis() - tiempoActual;

        //Guarda el tiempo actual
        tiempoActual += tiempoTranscurrido;
        if (cuadranteOprimido != -1) {
            ninja.setDirection(cuadranteOprimido);
            cuadranteOprimido = -1;
        }
        ninja.move();
        ninja.updateAnimation(tiempoTranscurrido);

        for (Malo paraguas : malos) {
            if (!paraguas.isInCollision()) {
                paraguas.move();
            }
            paraguas.updateAnimation(tiempoTranscurrido);

        }
        try {
            Thread.sleep(20);
        } catch (InterruptedException ex) {
        }

    }

    /**
     * Metodo usado para checar las colisiones del objeto elefante y asteroid
     * con las orillas del <code>Applet</code>.
     */
    public void checaColision() {
        for (Malo paraguas : malos) {
            if (paraguas.isInCollision()) {
                paraguas.decreaseCollisionCounter();
                if (paraguas.getCollisionCycles() < 0) {
                    paraguas.randomResetSide(getHeight(), getWidth());
                }
            } else {
                if (paraguas.intersecta(ninja)) {
                    paraguas.collideSides();
                    paraguas.setCont(paraguas.getCont() + 1);
                    sonido.play();
                }
            }

            //Checa colision con el applet
            if (paraguas.getLado() == 1 && paraguas.getPosX() > (getWidth() - paraguas.getAncho())) {
                paraguas.randomResetSide(getHeight(), getWidth());

            } else if (paraguas.getLado() == 2 && paraguas.getPosX() < 0) {
                paraguas.randomResetSide(getHeight(), getWidth());
            }

        }

        //checks ninja collision with applet X
        if (ninja.getPosX() <= 0) {
            ninja.moveRight();
            ninja.setDirection(ninja.getRIGHT());
        } else if (ninja.getPosX() >= getWidth() - ninja.getAncho()) {
            ninja.moveLeft();
            ninja.setDirection(ninja.getLEFT());
        }
        //checks ninja collision with applet Y
        if (ninja.getPosY() <= 0) {
            ninja.setDirection(ninja.getDOWN());

        } else if (ninja.getPosY() >= getHeight() - ninja.getAlto()) {
            ninja.setDirection(ninja.getUP());
        }

    }

    /**
     * Metodo <I>update</I> sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo lo que hace es actualizar el contenedor
     *
     * @param g es el <code>objeto grafico</code> usado para dibujar.
     */
    public void paint(Graphics g) {
        // Inicializan el DoubleBuffer
        if (dbImage == null) {
            dbImage = createImage(this.getSize().width, this.getSize().height);
            dbg = dbImage.getGraphics();
        }

        // Actualiza la imagen de fondo.
        dbg.setColor(getBackground());
        dbg.fillRect(0, 0, this.getSize().width, this.getSize().height);
        dbg.drawImage(background, 0, 0, rootPane);
        // Actualiza el Foreground.
        dbg.setColor(getForeground());
        paint1(dbg);

        // Dibuja la imagen actualizada
        g.drawImage(dbImage, 0, 0, this);
        
        paint1(g);
    }

    /**
     * El método paint() muestra en pantalla la animación
     */
    public void paint1(Graphics g) {
        // Muestra en pantalla el cuadro actual de la animación
        if (ninja != null && malos != null) {
            
            if (pausado) {
                g.drawString(ninja.getPAUSADO(), ninja.getPosX() - ninja.getAncho() / 2, ninja.getPosY() + ninja.getAlto() / 2);
            }
            g.drawImage(ninja.getImagen(), ninja.getPosX(), ninja.getPosY(), this);
            for (Malo paraguas : malos) {
                if(paraguas.isInCollision()){
                    g.drawString(ninja.getDESAPARECE(), paraguas.getPosX() - paraguas.getAncho() / 2, paraguas.getPosY() + paraguas.getAlto() / 2);
                }
                g.drawImage(paraguas.getImagen(), paraguas.getPosX(), paraguas.getPosY(), this);
            }
            g.drawString("Score: " + malos.get(0).getCont(), 25, 40);
        } else {
            g.drawString("Cargando...", getWidth() / 2, getHeight() / 2);
        }

    }

    /**
     * Metodo crearMalo que crea un malo en una posicion aleatoria fuera del
     * applet en la parte de arriba.
     *
     * @return objeto de clase <code>Malo</code>
     */
    public Malo crearMalo(int lado) {
        Malo nuevoParaguas = new Malo();

        nuevoParaguas.setLado(lado);

        //Posiciona al nuevo paraguas aleatoriamente en la parte superior del applet
        nuevoParaguas.randomResetSide(getHeight(), getWidth());

        //establece la velocidad del objeto de manera aleatoria entre 3 y 6 px
        nuevoParaguas.setRandomSpeed(3, 6);

        return nuevoParaguas;
    }

    /**
     * Metodo generateRandomMaloList que genera una lista de malos, escogiendo
     * entre 3 tamanios enviados como parametro.
     *
     * @param easy minima cantidad de malos a crear tipo <code>int</code>
     * @param medium mediana cantidad de malos a crear tipo <code>int</code>
     * @param hard maxima cantidad de malos a crear tipo <code>int</code>
     * @return objeto de tipo <code>LinkedList</code> de la clase Malo con los
     * malos del juego ya con su speed y posiciones aleatorias.
     */
    public LinkedList<Malo> generateRandomMaloList(int easy, int medium, int hard) {
        int R = (int) (Math.random() * (3 - 1)) + 1;
        switch (R) {
            case 1:
                R = easy;
                break;
            case 2:
                R = medium;
                break;
            case 3:
                R = hard;
                break;
        }

        int lado = 1;
        LinkedList<Malo> malos = new LinkedList<Malo>();
        for (int i = 0; i < R; i++) {
            if (i >= R / 2) {
                lado = 2;
            }
            malos.add(crearMalo(lado));
        }
        return malos;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        //presiono flecha izquierda
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            ninja.moveLeft();
            //Presiono flecha derecha
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            ninja.moveRight();
            //presiono p
        } else if (e.getKeyCode() == KeyEvent.VK_P) {
            pausado = !pausado;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        ninja.stop();
    }

    @Override
    public void mouseClicked(MouseEvent me) {

    }

    @Override
    public void mousePressed(MouseEvent me) {
        //Mouse coordinates
        int mx = me.getX();
        int my = me.getY();

        //Applet dimensions
        int w = getWidth();
        int h = getHeight();
        if (mx >= w / 2) {
            //cuadrante 1 o 2
            if (my >= h / 2) {
                //cuadrante 2
                cuadranteOprimido = 2;
            } else {
                //cuadrante 1
                cuadranteOprimido = 1;
            }
        } else {
            //cuadrante 3 o 4
            if (my >= h / 2) {
                //cuadrante 3
                cuadranteOprimido = 3;
            } else {
                //cuadrante 4
                cuadranteOprimido = 4;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent me) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
