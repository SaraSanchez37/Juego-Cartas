import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class FrmJuego extends JFrame {

    private JPanel pnlJugador1, pnlJugador2;
    private JTabbedPane tpJugadores;

    private Jugador jugador1 = new Jugador();
    private Jugador jugador2 = new Jugador();

    private String nombreJugador1 = "Jugador 1";
    private String nombreJugador2 = "Jugador 2";
    private int cantidadBarajas = 1;

    public FrmJuego() {
        setSize(500, 300);
        setTitle("Juego de Cartas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JButton btnRepartir = new JButton("Repartir");
        btnRepartir.setBounds(10, 10, 100, 25);
        add(btnRepartir);

        JButton btnVerificar = new JButton("Verificar");
        btnVerificar.setBounds(120, 10, 100, 25);
        add(btnVerificar);

        tpJugadores = new JTabbedPane();
        tpJugadores.setBounds(10, 45, 470, 200);
        add(tpJugadores);

        pnlJugador1 = new JPanel();
        pnlJugador1.setBackground(new Color(209, 164, 255));
        tpJugadores.add(nombreJugador1, pnlJugador1);

        pnlJugador2 = new JPanel();
        pnlJugador2.setBackground(new Color(201, 235, 150));
        tpJugadores.add(nombreJugador2, pnlJugador2);

        
        btnRepartir.addActionListener(evento -> {
            pedirDatosEIniciar();
        });

        btnVerificar.addActionListener(evento -> {
            verificar();
        });
    }

    private void pedirDatosEIniciar() { 
        String inputNombre1 = JOptionPane.showInputDialog(this, "Ingrese el nombre del Jugador 1:", nombreJugador1);
        if (inputNombre1 != null && !inputNombre1.isEmpty()) {
            nombreJugador1 = inputNombre1;
        }

        String inputNombre2 = JOptionPane.showInputDialog(this, "Ingrese el nombre del Jugador 2:", nombreJugador2);
        if (inputNombre2 != null && !inputNombre2.isEmpty()) {
            nombreJugador2 = inputNombre2;
        }

        tpJugadores.setTitleAt(0, nombreJugador1);
        tpJugadores.setTitleAt(1, nombreJugador2);

        String inputBarajas = JOptionPane.showInputDialog(this, "Ingrese la cantidad de barajas a usar:", "1");
        try {
            if (inputBarajas != null) {
                int barajas = Integer.parseInt(inputBarajas);
                if (barajas > 0) {
                    cantidadBarajas = barajas;
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valor inválido. Se usará 1 baraja por defecto.");
            cantidadBarajas = 1;
        }

        repartir();
    }

    private void repartir() {
        jugador1.repartir(cantidadBarajas);
        jugador1.mostrar(pnlJugador1);

        jugador2.repartir(cantidadBarajas);
        jugador2.mostrar(pnlJugador2);
    }

    private void verificar() {
        Jugador jugadorActual;
        String nombreActual;

        if (tpJugadores.getSelectedIndex() == 0) {
            jugadorActual = jugador1;
            nombreActual = nombreJugador1;
        } else {
            jugadorActual = jugador2;
            nombreActual = nombreJugador2;
        }

        String grupos = jugadorActual.getGrupos();
        String escaleras = jugadorActual.getEscaleras();
        int puntaje = jugadorActual.getPuntaje();

        String mensaje = "Resultados para: " + nombreActual + "\n\n"
                        + grupos + "\n\n" 
                        + escaleras + "\n\n" 
                        + "Puntaje de cartas libres: " + puntaje;

        JOptionPane.showMessageDialog(this, mensaje, "Verificación de Juego", JOptionPane.INFORMATION_MESSAGE);
    }
}