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
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
    private Bueno gordo;    //objeto bueno, controlable con el teclado
    private Malo burger;

    //marcador y pausa
    private int score;
    private boolean pausado;
    private int cuadranteOprimido;
    //Variables de control de tiempo de la animación
    private long tiempoActual;
    private long tiempoInicial;
    int posX, posY;
    double timer;

    //Almacenamiento
    private final String nombreArchivo = "datos.txt";
    private final String divisor = ",";
    private boolean guardar;
    private boolean cargar;

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
        gordo = new Bueno();
        this.setSize(800, 400);

        //Posiciona al gordo en la mitad derecha del applet en la parte de hasta abajo.
        gordo.setPosX(3 * getWidth() / 4 - gordo.getAncho() / 2);
        gordo.setPosY(getHeight() - gordo.getAlto() - gordo.getAlto() / 2 + 4);

        burger = crearMalo(1);

        //inicializo el marcador en 0
        score = 0;

        //el juego no esta pausado
        pausado = false;
        guardar = cargar = false;

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

        //Como el gordo no tiene animacion basica, si no se está moviendo, no hago nada.
        if (gordo.getDirection() != gordo.getSTOPPED()) {
            gordo.move();
            gordo.updateAnimation(tiempoTranscurrido);
        }

        timer += 0.2;
        if (!burger.isInCollision()) {
            burger.move(timer);
        }
        burger.updateAnimation(tiempoTranscurrido);

        try {
            Thread.sleep(20);
        } catch (InterruptedException ex) {
        }

        if (cargar) {
            try {
                cargarJuego();    //lee el contenido del archivo
            } catch (IOException e) {
                System.out.println("Error en " + e.toString());
            }
            cargar = false;
        }

        if (guardar) {
            try {
                grabarJuego();    //Graba el vector en el archivo.
            } catch (IOException e) {
                System.out.println("Error en " + e.toString());
            }
            guardar = false;
        }

    }

    /**
     * Metodo usado para checar las colisiones del objeto elefante y asteroid
     * con las orillas del <code>Applet</code>.
     */
    public void checaColision() {
        if (burger.isInCollision()) {
            burger.decreaseCollisionCounter();
            if (burger.getCollisionCycles() < 0) {
                burger.setHurled(false);
                burger.resetPosition();
                timer = 0;
            }
        } else {
            if (burger.intersecta(gordo)) {
                burger.collideSides();
                burger.setCont(burger.getCont() + 1);
                sonido.play();
            }
        }

        //Checa colision con el applet
        if (burger.getLado() == 1 && burger.getPosX() > (getWidth() - burger.getAncho())) {
            burger.resetPosition();

        } else if (burger.getLado() == 2 && burger.getPosX() < 0) {
            burger.resetPosition();
        }
        if (burger.getPosY() == (getHeight() - burger.getAlto())) {
            burger.resetPosition();
        }

        //checks fat guy collision with applet X
        if (gordo.getPosX() <= getWidth() / 5) {
            gordo.stop();
        } else if (gordo.getPosX() >= getWidth() - gordo.getAncho()) {
            gordo.stop();
        }
        //checks fat guy collision with applet Y
        if (gordo.getPosY() <= 0) {
            gordo.setDirection(gordo.getDOWN());

        } else if (gordo.getPosY() >= getHeight() - gordo.getAlto()) {
            gordo.setDirection(gordo.getUP());
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
        if (gordo != null && burger != null) {

            if (pausado) {
                g.drawString(gordo.getPAUSADO(), gordo.getPosX() - gordo.getAncho() / 2, gordo.getPosY() + gordo.getAlto() / 2);
            }
            g.drawImage(gordo.getImagen(), gordo.getPosX(), gordo.getPosY(), this);

            if (burger.isInCollision()) {
                g.drawString(gordo.getDESAPARECE(), burger.getPosX() - burger.getAncho() / 2, burger.getPosY() + burger.getAlto() / 2);
            }
            g.drawImage(burger.getImagen(), burger.getPosX(), burger.getPosY(), this);

            g.drawString("Score: " + burger.getCont(), 25, 40);
            g.drawString("Timer: " + timer, 25, 80);
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
        Malo nuevaBurger = new Malo();

        nuevaBurger.setLado(lado);

        //Posiciona al nuevo paraguas aleatoriamente en la parte superior del applet
//        nuevaBurger.randomResetSide(getHeight(), getWidth());
        nuevaBurger.resetPosition();

        //establece la velocidad del objeto de manera aleatoria entre 3 y 6 px
        nuevaBurger.setSpeed(0);

        return nuevaBurger;
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

    /**
     * Metodo que lee a informacion de un archivo y lo agrega a un vector.
     *
     * @throws IOException
     */
    public void cargarJuego() throws IOException {
        BufferedReader fileIn;
        try {
            fileIn = new BufferedReader(new FileReader(nombreArchivo));
        } catch (FileNotFoundException e) {
            File puntos = new File(nombreArchivo);
            PrintWriter fileOut = new PrintWriter(puntos);
            fileOut.println("100,demo");
            fileOut.close();
            fileIn = new BufferedReader(new FileReader(nombreArchivo));
        }
        String dato = fileIn.readLine();

        while (dato != null) {
            
            dato = fileIn.readLine();
        }
        fileIn.close();
        cargar = false; 
    }

    /**
     * Metodo que agrega la informacion del vector al archivo.
     *
     * @throws IOException
     */
    public void grabarJuego() throws IOException {
        PrintWriter fileOut = new PrintWriter(new FileWriter(nombreArchivo));
        
        //guardo posicion posicion burger
        String infoBurger = burger.getPosX() + divisor + burger.getPosY(); 
        String infoGordo = gordo.getPosX() + divisor + gordo.getPosY();
        String infoGeneral = score + divisor + timer + divisor; 
        fileOut.println(infoBurger); 
        fileOut.println(infoGordo); 
        fileOut.println(infoGeneral); 
       
        fileOut.close();
        guardar = false; 
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        //presiono flecha izquierda
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (gordo.getPosX() <= getWidth() / 5) {
                gordo.stop();
            } else {
                gordo.moveLeft();
            }
            //Presiono flecha derecha
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (gordo.getPosX() >= getWidth() - gordo.getAncho()) {
                gordo.stop();
            } else {
                gordo.moveRight();
            }
            //presiono p
        } else if (e.getKeyCode() == KeyEvent.VK_P) {
            pausado = !pausado;
        } else if (e.getKeyCode() == KeyEvent.VK_G) {
            guardar = true; 
        } else if (e.getKeyCode() == KeyEvent.VK_C) {
            cargar = true; 
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        gordo.stop();
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        //Mouse coordinates
        int mx = me.getX();
        int my = me.getY();

        //luego checo si le pique a la hamburguesa
        burger.randomHurl();
        timer = 0;
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
