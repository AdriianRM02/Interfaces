package buscaminas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.sun.tools.javac.Main;
/**
 * Simulacion de un Buscaminas en eclipse, practica con Javadoc.
 * @author adrian
 * @version 1.1
 * @since 1.0
 * @see {@linktourl https://buscaminas.eu/} 
 */
public class Buscaminas {
	public static void main(String[] args) {
		MiFrame f = new MiFrame();
		f.setVisible(true);
		f.setTitle("Ventana principal");
		f.setSize(675, 675);
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(false);
	}
}

/**
 * Clase para dividir el JFRame en 3 partes, tendremos la Clase Header, la Clase Medio y la Clase Autor.
 * @author adrian
 * see {@link Header} {@link Medio} {@link Autor}
 *
 */
class MiFrame extends JFrame {
	public MiFrame() {
		Medio medio = new Medio();
		add(medio, BorderLayout.CENTER);

		Header header = new Header(medio);
		add(header, BorderLayout.NORTH);

		Autor autor = new Autor();
		autor.setBackground(Color.BLACK);
		add(autor, BorderLayout.SOUTH);
	}
}

/**
 * En esta clase definimos el titulo junto al nivel de dificultad
 * @author adrian
 *
 */
class Header extends JPanel {
	JButton comenzar = new JButton("Comenzar");
	
	public Header(Medio medio) {
		setLayout(new FlowLayout(FlowLayout.CENTER));

		JLabel bienvenido = new JLabel("Bienvenido al Buscaminas");
		bienvenido.setFont(new Font("Arial", Font.BOLD, 11));

		JComboBox<String> opciones = new JComboBox<String>();
		opciones.addItem("Basico");
		opciones.addItem("Medio");
		opciones.addItem("Experto");

		comenzar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				comprobarLVL(opciones, medio);
			}
		});

		add(bienvenido);
		add(opciones);
		add(comenzar);
	}

	/**
	 * Indica el numero de casillas segun el nivel seleccionado.
	 * @param opciones Total de opciones posibles a seleccionar en el JComboBox.
	 * @param medio Objeto de la clase Medio.
	 * @throws IO Exception
	 */
	public static void comprobarLVL(JComboBox<String> opciones, Medio medio) {
		if (opciones.getSelectedItem().equals("Basico")) {
			medio.removeAll();

			medio.setLayout(new GridLayout(10, 10));
			medio.tam = 10;
			medio.tablero = new JButton[10][10];
			medio.crearTablero();
			medio.updateUI();
		} else if (opciones.getSelectedItem().equals("Medio")) {
			medio.removeAll();

			medio.setLayout(new GridLayout(15, 15));
			medio.tam = 15;
			medio.tablero = new JButton[15][15];
			medio.crearTablero();
			medio.updateUI();
		} else if (opciones.getSelectedItem().equals("Experto")) {
			medio.removeAll();

			medio.setLayout(new GridLayout(20, 20));
			medio.tam = 20;
			medio.tablero = new JButton[20][20];
			medio.crearTablero();
			medio.updateUI();
		}
	}
}

/**
 * Clase autor donde colocaremos nuestro nombre completo
 * @author adrian
 *
 */
class Autor extends JPanel {
	public Autor() {
		setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel nombre = new JLabel("Autor: Adrián Rebolledo Moreno");
		nombre.setFont(new Font("Arial", Font.ITALIC, 15));
		nombre.setForeground(Color.WHITE);
		add(nombre);
	}
}

/**
 * Clase Medio donde creo el tablero
 * @author adrian
 * 
 * 
 */
class Medio extends JPanel {
	private boolean bandera = false;
	protected int tam = 10;
	protected int minas = 0;
	
	protected JButton[][] tablero = new JButton[tam][tam];

	/**
	 * Creamos el tablero del buscaminas segun el nivel de dificultad seleccionado.
	 * @throws IOException si el tamanio del tablero es igual a cero
	 */
	public void crearTablero() {
		int filas = tam;
		int columnas = tam;
		int tamanioTablero = filas*columnas;
		for (int i = 0; i < filas; i++) {
			for (int j = 0; j < columnas; j++) {

				tablero[i][j] = new JButton();
				tablero[i][j].putClientProperty("identificador", 0);

				generarMinas(tablero[i][j]);
				banderas(tablero[i][j], i, j, tam);
				add(tablero[i][j]);
			}
		}
	}

	/**
	 * Metodo para controlar las banderas y casillas pulsadas en el tablero
	 * @param matriz Tablero para controlar las casillas
	 * @param i Filas del tablero
	 * @param j Columnas del tablero
	 * @param tam Tamanio del tablero
	 */
	private void banderas(JButton matriz, int i, int j, int tam) {

		matriz.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				boolean bandera = false;
				int tamanioTablero = tam*tam;
				/*
				 * Controlamos que al finalizar el estado del juego (Ganador o Perdedor)
				 * la casilla no pueda volver a ser seleccionada
				 */
				if (tablero[i][j].isEnabled() == false && tablero[i][j].getIcon() == null) {
					ImageIcon icon = new ImageIcon("");
					tablero[i][j].setIcon(icon);
				}
				
				// Al pulsar click derecho colocaremos la bandera en dicha casilla
				if (e.getButton() == MouseEvent.BUTTON3 && matriz.getIcon() == (null)) {
					ImageIcon icon = new ImageIcon("src/imagenes/bandera.png");
					matriz.setIcon(icon);
					bandera = true;
				}
				
				// Al pulsar click derecho quitamos la bandera en dicha casilla
				if (e.getButton() == MouseEvent.BUTTON3
						&& matriz.getIcon().toString().equals("src/imagenes/bandera.png") && bandera == false) {
					matriz.setIcon(null);
				}
				/*
				 * En el caso de que la casilla tiene el id = 1 el jugador pierde ya que
				 * asignamos ese id en el metodo colocarMinas();
				 */
				if (e.getButton() == MouseEvent.BUTTON1 && matriz.getClientProperty("identificador").equals(1)
						&& matriz.getIcon() == (null)) {

					ImageIcon icon = new ImageIcon("src/imagenes/mina.png");

					matriz.setIcon(icon);
					findelJuego(tablero);
					JOptionPane.showMessageDialog(null, "Has perdido");

					// muestro donde estan todas las minas ocultas al perder
					mostrarTodaslasMinas(icon);
				} 
				if (e.getButton() == MouseEvent.BUTTON1 && !(matriz.getClientProperty("identificador").equals(1))
						&& matriz.getIcon() == (null)) {
					// descubrimos las casillas
					mostrarNumeros(i, j);
					// contadorCasillas(tamanioTablero, i, j, tablero);

					for (int i = 0; i < tablero.length; i++) {
						for (int j = 0; j < tablero.length; j++) {
							if (tablero[i][j].isEnabled() == false) {
								tamanioTablero--;
							}
						}
					}

					// Si el numero de casillas disponibles contienen minas el juego habra terminado
					if (minas == tamanioTablero) {
						JOptionPane.showMessageDialog(null, "¡Has ganado!");
					}
				}
			}
		});
	}

	/**
	 * Metodo para recorrer las casillas del tablero
	 * @param i Filas del tablero
	 * @param j Columnas del tablero
	 */
	public void mostrarNumeros(int i, int j) {
		tablero[i][j].setEnabled(false);
		int fila1, fila2, columna1, columna2;

		// Sentencia para recorrer una a una las casillas del tablero
		if (i - 1 < 0) {
			fila1 = 0;
		} else {
			fila1 = i - 1;
		}
		if (i + 1 > tablero.length - 1) {
			fila2 = tablero.length - 1;
		} else {
			fila2 = i + 1;
		}
		if (j - 1 < 0) {
			columna1 = 0;
		} else {
			columna1 = j - 1;
		}
		if (j + 1 > tablero.length - 1) {
			columna2 = tablero.length - 1;
		} else {
			columna2 = j + 1;
		}
		mostrarMinasCasilla(i, j, tablero);
		
		if ((tablero[i][j].getIcon() == (null))) {	
			for (int f = fila1; f <= fila2; f++) {
				for (int c = columna1; c <= columna2; c++) {
					
					// si el tablero no tiene icono y no esta descubierto se descubren recursivamente
					if (tablero[f][c].isEnabled() == true) {
						if (tablero[f][c].getIcon() == (null)) {
							mostrarNumeros(f, c);
						}
					}
				}
			}
		}
	}

	/**
	 * Muestra el numero de minas alrededor de la casilla selccionada, en caso de que no haya mina se desplegara el tablero recursivamente
	 * @param i filas del tablero
	 * @param j columnas del tablero
	 * @param tablero Tablero para saber en que casilla se ha pulsado
	 */
	public static void mostrarMinasCasilla(int i, int j, JButton[][] tablero) {
		int minas = 0;
		int fila1, fila2, columna1, columna2;

		// Sentencia para recorrer una a una las casillas del tablero
		if (i - 1 < 0) {
			fila1 = 0;
		} else {
			fila1 = i - 1;
		}
		if (i + 1 > tablero.length - 1) {
			fila2 = tablero.length - 1;
		} else {
			fila2 = i + 1;
		}
		if (j - 1 < 0) {
			columna1 = 0;
		} else {
			columna1 = j - 1;
		}
		if (j + 1 > tablero.length - 1) {
			columna2 = tablero.length - 1;
		} else {
			columna2 = j + 1;
		}

		if (!tablero[i][j].getClientProperty("identificador").equals(1)) {
			for (int x = fila1; x <= fila2; x++) {
				for (int y = columna1; y <= columna2; y++) {
					if (tablero[x][y].getClientProperty("identificador").equals(1)) {
						minas++;
					}

					// Colocamos el numero segun el numero de minas que haya alreddor de la casilla
					// 1-8
					if (minas != 0) {
						ImageIcon icon = new ImageIcon("src/imagenes/" + minas + ".png");
						tablero[i][j].setIcon(icon);
						tablero[i][j].setDisabledIcon(icon);
					}
				}
			}
		}
	}

	/**
	 * Muestra las minas una vez el jugador ha perdido
	 * @param icon Imagen de la mina
	 */
	public void mostrarTodaslasMinas(ImageIcon icon) {
		for (int i = 0; i < tablero.length; i++) {
			for (int j = 0; j < tablero.length; j++) {
				if (tablero[i][j].getClientProperty("identificador").equals(1)) {
					tablero[i][j].setIcon(icon);
					tablero[i][j].setBackground(Color.red);
					tablero[i][j].setDisabledIcon(icon);
					
				}
			}
		}
	}

	/**
	 * Reduce el numero de casillas para que cuando el numero de casillas sea igual al numero de minas obtengamos el ganador. 
	 * @param tamanioTablero numero de filas x numero de columnas
	 * @param i Filas del tablero
	 * @param j Columnas del tablero
	 * @param tablero Tablero para saber en que casilla se ha pulsado
	 */
	public void contadorCasillas(int tamanioTablero, int i, int j, JButton[][] tablero) {
		if (tablero[i][j].isEnabled()) {
			tamanioTablero--;
		}
	}
	
	/**
	 * Genera el numero de minas segun la dificultad seleccionada
	 * @param tablero Tablero para controlar las casillas
	 */
	public void generarMinas(JButton tablero) {
		if (1 > (int) (Math.random() * 10)) {
			tablero.putClientProperty("identificador", 1);
			minas++;
		}
	}

	/**
	 * Indica el final del juego y por tanto desactiva todas las casillas
	 * @param tablero Tablero para controlas las casillas
	 */
	private void findelJuego(JButton[][] tablero) {
		for (int i = 0; i < tablero.length; i++) {
			for (int j = 0; j < tablero.length; j++) {
				tablero[i][j].setEnabled(false);
				
			}
		}
	}
}