import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Carta {

    private int indice;
    private boolean tapada;

    
    public Carta(Random r, int cantidadBarajas) {
        int totalCartas = 52 * cantidadBarajas;
        int numeroAleatorio = r.nextInt(totalCartas) + 1;
        
        this.indice = (numeroAleatorio - 1) % 52 + 1;
        this.tapada = false;
    }

    public void mostrarCarta(int x, int y, JPanel pnl, boolean tapadaInicial) {
        this.tapada = tapadaInicial;

        
        ImageIcon imagen = obtenerImagen();
        JLabel lblCarta = new JLabel(imagen);

        
        lblCarta.setBounds(x, y, imagen.getIconWidth(), imagen.getIconHeight());

        
        lblCarta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evento) {
                if (evento.getClickCount() == 2) {
                    JOptionPane.showMessageDialog(
                        pnl,
                        "Carta: " + getNombre() + " de " + getPinta() + "\nValor: " + obtenerValor(),
                        "Detalle de Carta",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                } 
                
                else if (evento.getClickCount() == 1) {
                    tapada = !tapada;
                    lblCarta.setIcon(obtenerImagen());

                    pnl.revalidate();
                    pnl.repaint();
                }
            }
        });

        pnl.add(lblCarta);
    }

    
    private ImageIcon obtenerImagen() {
        String rutaImagen;
        if (tapada) {
            rutaImagen = "imagenes/TAPADA.JPG";
        } else {
            rutaImagen = "imagenes/CARTA" + indice + ".JPG";
        }
        return new ImageIcon(getClass().getResource(rutaImagen));
    }

    public int obtenerValor() {
        NombreCarta nombre = getNombre();
        if (nombre == NombreCarta.AS || nombre == NombreCarta.JACK || 
            nombre == NombreCarta.QUEEN || nombre == NombreCarta.KING) {
            return 10;
        } else {
            return nombre.ordinal() + 1;
        }
    }

    // Getters
    public Pinta getPinta() {
        if (indice <= 13) {
            return Pinta.TREBOL;
        } else if (indice <= 26) {
            return Pinta.PICA;
        } else if (indice <= 39) {
            return Pinta.CORAZON;
        } else {
            return Pinta.DIAMANTE;
        }
    }

    public NombreCarta getNombre() {
        int residuo = indice % 13;
        if (residuo == 0) {
            residuo = 13;
        }
        return NombreCarta.values()[residuo - 1];
    }

    public boolean isTapada() {
        return tapada;
    }
}