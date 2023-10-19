
import java.util.ArrayList;

public class sintaxis extends lexico {

    boolean errorSintactico = false;
    ArrayList<Variables> listaVariables = new ArrayList<>();
    String tipoDato;

    public sintaxis() {
        try {
            if (cabeza == null) {
                errorSintactico = true;
                System.out.println("Codigo sin tokens");
            } else {
                p = cabeza;
                while (p != null) {
                    if (p.token == 203) {// main
                        p = p.sig;
                        if (p.token == 117) {// (
                            p = p.sig;
                            if (p.token == 118) {// )
                                p = p.sig;
                                if (p.token == 119) {// {
                                    p = p.sig;
                                    variables();
                                    if (!errorSintactico) {
                                        while ((p.token == 201 || p.token == 202 || p.token == 204
                                                || p.token == 206 || p.token == 214
                                                || p.token == 100) && !errorSintactico) {
                                            statement();
                                        }

                                        ImprimirLista();
                                        if (p.token != 120 && !errorSintactico) {
                                            System.out.println("Se espera '}'");
                                        }
                                        if (p.sig != null && !errorSintactico) {
                                            System.out.println("Statement fuera de main");
                                            errorSintactico = true;
                                        }
                                    }
                                } else {
                                    System.out.println("Se espera '{'");
                                    errorSintactico = true;
                                }
                            } else {
                                System.out.println("Se espera ')'");
                                errorSintactico = true;
                            }
                        } else {
                            System.out.println("Se espera '('");
                            errorSintactico = true;
                        }
                    } else {
                        System.out.println("Se espera 'main'");
                        errorSintactico = true;
                    }
                    break;
                }
            }
        } catch (NullPointerException e) {
            errorSintactico = true;
            System.out.println("Catch exception Se espera '}'");
        }
    }

    private void variables() {
        if (p.token == 207) {// new
            p = p.sig;
            tipos();
            if (!errorSintactico) {
                if (p.token == 100) {
                    ValidarRepeticion();
                    GuardarVariable();
                    p = p.sig;
                    while (p.token == 124 && !errorSintactico) {// ,
                        p = p.sig;
                        if (p.token != 100) {
                            System.out.println("Se espera un identificador");
                            errorSintactico = true;
                        } else {
                            ValidarRepeticion();
                            GuardarVariable();
                            p = p.sig;
                        }
                    }
                    if (!errorSintactico) {
                        if (p.token == 125) {
                            p = p.sig;

                            if (p.token == 207 || p.token == 209 || p.token == 208 || p.token == 213 || p.token == 212) {//new
                                variables();
                            }
                        } else {
                            System.out.println("Se espera ;");
                            errorSintactico = true;
                        }
                    }

                } else {
                    System.out.println("Se espera un identificador");
                    errorSintactico = true;
                }
            }

        } else {
            System.out.println("Se espera 'new'");
            errorSintactico = true;
        }
    }

    private void tipos() {
        if (p.token != 209 && p.token != 208 && p.token != 213 && p.token != 212) {
            System.out.println("Se espera un tipo de variable");
            errorSintactico = true;
        } else {
            tipoDato = p.lexema;
            p = p.sig;
        }
    }

    private void statement() {
        switch (p.token) {
            case 214:// getvalue()
                p = p.sig;
                if (p.token == 117) {
                    p = p.sig;
                    if (p.token == 118) {
                        p = p.sig;
                        if (p.token == 125) {
                            p = p.sig;
                        } else {
                            System.out.println("Se espera ';'");
                            errorSintactico = true;
                        }
                    } else {
                        System.out.println("Se espera ')'");
                        errorSintactico = true;
                    }
                } else {
                    System.out.println("Se espera '('");
                    errorSintactico = true;
                }
                break;

            case 100: // Asignación
                String tipoVariable = obtenerTipoVariable(p.lexema); // Obtener el ID
                p = p.sig;
                if (p.token == 123) {
                    p = p.sig;
                    String valorAsignado = p.lexema;
                    exp_simple();
                    if (!errorSintactico) {
                        if (tipoVariable != null) {
                           
                            if (validarTipoDato(tipoVariable, valorAsignado)) {
                            } else {
                                System.out.println("Error en el renglón " + p.renglon + ": El tipo de dato no coincide para la variable '" + tipoVariable + "'.");
                            }
                        } else {
                            System.out.println("Error en el renglón " + p.renglon + ": La variable no ha sido definida.");
                        }
                        if (p.token == 125) {
                            p = p.sig;

                        } else {
                            System.out.println("Se espera ';'");
                            errorSintactico = true;
                        }
                    }
                } else {
                    System.out.println("Se espera '='");
                    errorSintactico = true;
                }
                break;

            case 201:// if
                p = p.sig;
                if (p.token == 117) {
                    p = p.sig;
                    exp_cond();
                    if (!errorSintactico) {
                        if (p.token == 118) {
                            p = p.sig;
                            if (p.token == 119) {
                                p = p.sig;
                                while ((p.token == 201 || p.token == 202 || p.token == 204
                                        || p.token == 206 || p.token == 214
                                        || p.token == 100) && !errorSintactico) {
                                    statement();
                                }
                                if (!errorSintactico) {
                                    if (p.token == 120) {
                                        p = p.sig;
                                        if (p.token == 202) {
                                            p = p.sig;
                                            if (p.token == 119) {
                                                p = p.sig;
                                                while ((p.token == 201 || p.token == 202 || p.token == 204
                                                        || p.token == 206 || p.token == 214
                                                        || p.token == 100) && !errorSintactico) {
                                                    statement();
                                                }
                                                if (!errorSintactico) {
                                                    if (p.token == 120) {
                                                        p = p.sig;
                                                    } else {
                                                        System.out.println("Se espera '}'");
                                                        errorSintactico = true;

                                                    }
                                                }

                                            } else {
                                                System.out.println("Se espera '{'");
                                                errorSintactico = true;
                                            }
                                        }
                                    } else {
                                        System.out.println("Se espera '}'");
                                        errorSintactico = true;
                                    }
                                }
                            } else {
                                System.out.println("Se espera '{'");
                                errorSintactico = true;
                            }
                        } else {
                            System.out.println("Se espera ')'");
                            errorSintactico = true;
                        }
                    }
                } else {
                    System.out.println("Se espera '('");
                    errorSintactico = true;
                }
                break;
            case 204:// while
                p = p.sig;
                if (p.token == 117) {
                    p = p.sig;
                    exp_cond();
                    if (!errorSintactico) {
                        if (p.token == 118) {
                            p = p.sig;
                            if (p.token == 119) {
                                p = p.sig;
                                while ((p.token == 201 || p.token == 202 || p.token == 204
                                        || p.token == 206 || p.token == 214
                                        || p.token == 100) && !errorSintactico) {
                                    statement();
                                }
                                if (!errorSintactico) {
                                    if (p.token == 120) {
                                        p = p.sig;

                                    } else {
                                        System.out.println("Se espera '}'");
                                        errorSintactico = true;
                                    }
                                }

                            } else {
                                System.out.println("Se espera '{'");
                                errorSintactico = true;
                            }
                        } else {
                            System.out.println("Se espera ')'");
                            errorSintactico = true;
                        }
                    }

                } else {
                    System.out.println("Se espera '('");
                    errorSintactico = true;
                }
                break;
            case 202:
                System.out.println("Else sin un if");
                errorSintactico = true;
                break;
            case 206:// print
                p = p.sig;
                if (p.token == 117) {
                    p = p.sig;
                    if (p.token == 100 || p.token == 122) {
                        p = p.sig;
                        while (p.token == 124) {
                            p = p.sig;
                            if (p.token != 100 && p.token != 122) {
                                System.out.println("Se espera un identificador o cadena de texto");
                                errorSintactico = true;
                            } else {
                                p = p.sig;
                            }
                        }
                        if (!errorSintactico) {
                            if (p.token == 118) {
                                p = p.sig;
                                if (p.token == 125) {
                                    p = p.sig;
                                } else {
                                    System.out.println("Se espera ';'");
                                    errorSintactico = true;
                                }
                            } else {
                                System.out.println("Se espera ')'");
                                errorSintactico = true;
                            }
                        }
                    } else {
                        System.out.println("Se espera un identificador");
                        errorSintactico = true;
                    }
                } else {
                    System.out.println("Se espera '('");
                    errorSintactico = true;
                }
                break;
            default:
                System.out.println("Se espera un statement válido");
                errorSintactico = true;
                break;

        }
    }

    private void exp_simple() {
        if (p.token == 103 || p.token == 104) {
            signo();
        }
        termino();
        if (!errorSintactico) {
            if (p.token == 103 || p.token == 104) {
                op_aditivo();
                if (!errorSintactico) {
                    exp_simple();
                }

            }
        }

    }

    private void termino() {
        factor();
        if (!errorSintactico) {
            while (p.token == 105 || p.token == 106) {
                op_multi();
                if (!errorSintactico) {
                    factor();
                }

            }
        }

    }

    private void factor() {
        switch (p.token) {
            case 100:
                p = p.sig;
                break;

            case 101:
                p = p.sig;
                break;

            case 102:
                p = p.sig;
                break;

            case 117:
                p = p.sig;
                exp_simple();
                if (!errorSintactico) {
                    if (p.token == 118) {
                        p = p.sig;
                    } else {
                        System.out.println("Se espera ')'");
                        errorSintactico = true;
                    }
                }

                break;

            case 116:
                p = p.sig;
                factor();
                break;
            case 122:
                p = p.sig;
                break;
            default:
                System.out.println("Se espera un factor valido");
                errorSintactico = true;
        }
    }

    private void op_aditivo() {
        switch (p.token) {
            case 103:
                p = p.sig;
                break;
            case 104:
                p = p.sig;
                break;
            case 115:
                p = p.sig;
                break;
            default:
                System.out.println("Se espera un operador aditivo");
                errorSintactico = true;
                break;
        }
    }

    private void op_multi() {
        switch (p.token) {
            case 105:
                p = p.sig;
                break;
            case 106:
                p = p.sig;
                break;
            case 114:
                p = p.sig;
                break;
            default:
                System.out.println("Se espera un operador multiplicativo");
                errorSintactico = true;
                break;
        }
    }

    private void signo() {
        switch (p.token) {
            case 103:
                p = p.sig;
                break;
            case 104:
                p = p.sig;
                break;

            default:
                System.out.println("Se espera signo '+' o '-'");
                errorSintactico = true;
        }
    }

    private void exp_cond() {
        exp_simple();
        op_relacio();
        if (!errorSintactico) {
            exp_simple();
            if (p.token == 114 || p.token == 115) {
                p = p.sig;
                exp_simple();
                op_relacio();
                if (!errorSintactico) {
                    exp_simple();
                }
            }
        }

    }

    private void op_relacio() {
        switch (p.token) {
            case 109:
                p = p.sig;
                break;
            case 108:
                p = p.sig;
                break;
            case 111:
                p = p.sig;
                break;
            case 110:
                p = p.sig;
                break;
            case 113:
                p = p.sig;
                break;
            case 112:
                p = p.sig;
                break;
            default:
                System.out.println("Se espera un operador relacional");
                errorSintactico = true;
                break;
        }
    }

    private void GuardarVariable() {
        Variables nuevaVariable = new Variables(p.renglon, p.lexema, tipoDato);
        listaVariables.add(nuevaVariable);
    }

    private void ImprimirLista() {
        System.out.println("+-----------------+-----------+--------------+");
        System.out.println("| Renglon |    ID     |  Tipo de Dato |");
        System.out.println("+-----------------+-----------+--------------+");
        for (Variables variable : listaVariables) {
            String numeroRenglon = String.format("| %-15d", variable.getNumeroRenglon());
            String id = String.format("| %-20s", variable.getId());
            String tipo = String.format("| %-20s", variable.getTipoDato());
            System.out.println(numeroRenglon + id + tipo + "|");
        }
        System.out.println("+-----------------+-----------+--------------+");
    }

    private void ValidarRepeticion() {
        boolean encontrado = false;

        for (Variables variable : listaVariables) {
            String id = variable.getId();

            // Comparar el ID con el lexema
            if (id.equals(p.lexema)) {
                encontrado = true;
                break;
            }
        }

        if (encontrado) {
            System.out.println("El ID '[" + p.lexema + "]' ya existe en la lista.");
            errorSintactico = true;
        }
    }

    public boolean existeVariable(String nombreVariable) {
        String[] palabrasReservadas = {"int", "string", "float", "boolean"}; // Lista de palabras reservadas

        for (Variables variable : listaVariables) {
            if (variable.getId().equals(nombreVariable)) {
                return true; // Devuelve verdadero si la variable ya ha sido definida
            }
        }

        // Verificar si el nombre de la variable es una palabra reservada
        for (String palabraReservada : palabrasReservadas) {
            if (palabraReservada.equals(nombreVariable)) {
                return true; // Devuelve verdadero si es una palabra reservada
            }
        }

        return false; // Devuelve falso si la variable no ha sido definida y no es una palabra reservada
    }

    public String obtenerTipoVariable(String nombreVariable) {
        for (Variables variable : listaVariables) {
            if (variable.getId().equals(nombreVariable)) {
                return variable.getTipoDato(); // Devuelve el tipo de la variable si ya ha sido definida
            }
        }
        return null; // Devuelve null si la variable no ha sido definida

    }

    public static boolean validarTipoDato(String tipoVariable, String valorAsignado) {
        switch (tipoVariable) {
            case "int ":
            try {
                Integer.parseInt(valorAsignado);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }

            case "float ":
            try {
                Float.parseFloat(valorAsignado);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }

            case "string ":
                // Verificar si el valor está entre comillas dobles
                return valorAsignado.startsWith("\"") && valorAsignado.endsWith("\"");

            case "boolean ":
                // Verificar si el valor es "true" o "false" (sin distinción entre mayúsculas y minúsculas)
                return valorAsignado.equalsIgnoreCase("true") || valorAsignado.equalsIgnoreCase("false");

            default:
                return false;
        }
    }

}
