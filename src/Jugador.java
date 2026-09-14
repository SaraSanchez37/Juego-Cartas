import java.util.Random;

import javax.swing.JPanel;

public class Jugador {

    private final int TOTAL_CARTAS = 10;
    private final int MARGEN = 10;
    private final int DISTANCIA = 40;

    private Carta[] cartas = new Carta[TOTAL_CARTAS];
    private Random r = new Random();

    private final int NUMERO_BARAJAS = 2; // Puedes definir aquí la cantidad

public void repartir(int cantidadBarajas) {
        for (int i = 0; i < TOTAL_CARTAS; i++) {
            cartas[i] = new Carta(r, cantidadBarajas);
        }
}
    public void mostrar(JPanel pnl) {
        pnl.removeAll();
        pnl.setLayout(null);
        int posicion = MARGEN + TOTAL_CARTAS * DISTANCIA;
        
        for (Carta carta : cartas) {
            posicion -= DISTANCIA;
            // Evita el NullPointerException si no se ha llamado a repartir() previamente
            if (carta != null) {
                carta.mostrarCarta(posicion, MARGEN, pnl, false);
            }
        }
        pnl.repaint();
    }

    public String getGrupos() {
        String resultado = "No se encontraron grupos";

        int[] contadores = new int[NombreCarta.values().length];
        boolean hayGrupos = false;
        for (Carta carta : cartas) {
            int posicion = carta.getNombre().ordinal();
            contadores[posicion]++;
            if (!hayGrupos && contadores[posicion] >= 2) {
                hayGrupos = true;
            }
        }

        if (hayGrupos) {
            resultado = "Se encontraron los siguientes grupos:\n";
            // for (int contador : contadores) {
            for (int i = 0; i < contadores.length; i++) {
                // if (contador >= 2) {
                if (contadores[i] >= 2) {
                    resultado += Grupo.values()[contadores[i]] + " de " + NombreCarta.values()[i] + "\n";
                }
            }
        }
        return resultado;
    }
public String getEscaleras() {
    if (cartas == null || cartas[0] == null) {
        return "No hay cartas repartidas";
    }

    // 1. Crear 4 arreglos para separar los valores de las cartas por cada pinta
    int[] treboles = new int[TOTAL_CARTAS];
    int[] picas = new int[TOTAL_CARTAS];
    int[] corazones = new int[TOTAL_CARTAS];
    int[] diamantes = new int[TOTAL_CARTAS];

    // Contadores de cartas por cada pinta
    int cTrebol = 0, cPica = 0, cCorazon = 0, cDiamante = 0;

    // 2. Clasificar los ordinales de las cartas (0 para ACE, 12 para KING)
    for (int i = 0; i < TOTAL_CARTAS; i++) {
        if (cartas[i] != null) {
            int valor = cartas[i].getNombre().ordinal();
            Pinta pinta = cartas[i].getPinta();

            if (pinta == Pinta.TREBOL) {
                treboles[cTrebol] = valor;
                cTrebol++;
            } else if (pinta == Pinta.PICA) {
                picas[cPica] = valor;
                cPica++;
            } else if (pinta == Pinta.CORAZON) {
                corazones[cCorazon] = valor;
                cCorazon++;
            } else if (pinta == Pinta.DIAMANTE) {
                diamantes[cDiamante] = valor;
                cDiamante++;
            }
        }
    }

    // 3. Evaluar secuencias en cada grupo
    String resultado = "";
    resultado += buscarSecuencia(treboles, cTrebol, Pinta.TREBOL);
    resultado += buscarSecuencia(picas, cPica, Pinta.PICA);
    resultado += buscarSecuencia(corazones, cCorazon, Pinta.CORAZON);
    resultado += buscarSecuencia(diamantes, cDiamante, Pinta.DIAMANTE);

    if (resultado.equals("")) {
        return "No se encontraron escaleras";
    }
    return "Se encontraron las siguientes escaleras:\n" + resultado;
}

private String buscarSecuencia(int[] valores, int cantidad, Pinta pinta) {
    if (cantidad < 2) return ""; // Menos de 2 cartas de una misma pinta no pueden hacer escalera

    // A. Ordenamiento Burbuja tradicional de menor a mayor
    for (int i = 0; i < cantidad - 1; i++) {
        for (int j = 0; j < cantidad - 1 - i; j++) {
            if (valores[j] > valores[j + 1]) {
                int aux = valores[j];
                valores[j] = valores[j + 1];
                valores[j + 1] = aux;
            }
        }
    }

    // B. Recorrido para detectar cartas consecutivas seguidas
    String texto = "";
    int racha = 1;
    int inicio = 0;

    for (int i = 0; i < cantidad - 1; i++) {
        int diferencia = valores[i + 1] - valores[i];

        if (diferencia == 1) { // Son consecutivas (ej. 3 y 4)
            if (racha == 1) {
                inicio = i;
            }
            racha++;
        } else if (diferencia > 1) { // Hay un salto (ej. 3 y 6), se rompe la racha
            if (racha >= 2) {
                texto += armarMensaje(valores, inicio, racha, pinta);
            }
            racha = 1;
        }
        // Si la diferencia == 0 (carta repetida), se ignora y continúa la racha actual
    }

    // Evaluar la racha al llegar al final del arreglo
    if (racha >= 2) {
        texto += armarMensaje(valores, inicio, racha, pinta);
    }

    return texto;
}

private String armarMensaje(int[] valores, int inicio, int racha, Pinta pinta) {
    String tipo = "Escalera de " + racha;
    if (racha == 2) tipo = "Pareja en escalera";
    else if (racha == 3) tipo = "Terna en escalera";
    else if (racha == 4) tipo = "Cuarta en escalera";
    else if (racha == 5) tipo = "Quinta en escalera";

    String msg = tipo + " de " + pinta + " (";
    
    // Recorrer los valores de la racha evitando repetir nombres visualmente si había cartas dobles
    int agregados = 0;
    for (int k = inicio; k < inicio + racha + (racha - 1); k++) {
        if (k < valores.length) {
            // Evita imprimir cartas duplicadas en el texto emergente
            if (k > inicio && valores[k] == valores[k - 1]) {
                continue;
            }
            msg += NombreCarta.values()[valores[k]];
            agregados++;
            if (agregados < racha) {
                msg += ", ";
            }
        }
        if (agregados == racha) break;
    }
    msg += ")\n";
    return msg;
}
// Calcula el puntaje sumando solo las cartas que NO forman grupos ni escaleras
    public int getPuntaje() {
        if (cartas == null || cartas[0] == null) {
            return 0;
        }

        // Arreglo para marcar qué cartas forman grupos o escaleras
        boolean[] enCombinacion = new boolean[TOTAL_CARTAS];

        // 1. MARCAR CARTAS EN GRUPOS (Mismo nombre/número)
        for (int i = 0; i < TOTAL_CARTAS; i++) {
            for (int j = i + 1; j < TOTAL_CARTAS; j++) {
                if (cartas[i] != null && cartas[j] != null) {
                    if (cartas[i].getNombre() == cartas[j].getNombre()) {
                        enCombinacion[i] = true;
                        enCombinacion[j] = true;
                    }
                }
            }
        }

        // 2. MARCAR CARTAS EN ESCALERAS (Misma pinta y números consecutivos)
        for (int i = 0; i < TOTAL_CARTAS; i++) {
            for (int j = 0; j < TOTAL_CARTAS; j++) {
                if (i != j && cartas[i] != null && cartas[j] != null) {
                    // Si son de la misma pinta y la diferencia entre sus nombres es exactamente 1
                    boolean mismaPinta = cartas[i].getPinta() == cartas[j].getPinta();
                    int dif = cartas[j].getNombre().ordinal() - cartas[i].getNombre().ordinal();

                    if (mismaPinta && dif == 1) {
                        enCombinacion[i] = true;
                        enCombinacion[j] = true;
                    }
                }
            }
        }

        // 3. SUMAR VALOR DE LAS CARTAS LIBRES
        int sumaPuntaje = 0;
        for (int i = 0; i < TOTAL_CARTAS; i++) {
            // Si la carta no forma grupo ni escalera, suma su valor
            if (!enCombinacion[i] && cartas[i] != null) {
                sumaPuntaje += cartas[i].obtenerValor();
            }
        }

        return sumaPuntaje;
    }
}
