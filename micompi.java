public class micompi {
    nodo p;
    public void ejecutarAnalisis() {
        lexico lexico = new lexico();
        lexico.imprimirNodos();
        if (!lexico.errorEncontrado) {
            System.out.println("Análisis léxico terminado");
            sintaxis sintaxis = new sintaxis();
            if (!sintaxis.errorSintactico) {
                System.out.println("Análisis sintáctico terminado");
            }
        }
    }
}
