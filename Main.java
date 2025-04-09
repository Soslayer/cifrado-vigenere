import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        BigVigenere bv = new BigVigenere("123456");
        String mensaje = "Hola123";
        String cifrado = bv.encrypt(mensaje);
        System.out.println("Mensaje cifrado: " + cifrado);

        String descifrado = bv.decrypt(cifrado);
        System.out.println("Mensaje descifrado: " + descifrado);

        bv.reEncrypt();

        System.out.println("\n      Medición de tiempo con mensaje largo (clave fija)     ");

        BigVigenere bv2 = new BigVigenere("1234567890");
        String mensajeLargo = generarMensajeLargo(10000);

        long startEncrypt = System.nanoTime();
        String cifradoLargo = bv2.encrypt(mensajeLargo);
        long endEncrypt = System.nanoTime();

        long startDecrypt = System.nanoTime();
        String descifradoLargo = bv2.decrypt(cifradoLargo);
        long endDecrypt = System.nanoTime();

        long tiempoEncrypt = endEncrypt - startEncrypt;
        long tiempoDecrypt = endDecrypt - startDecrypt;

        System.out.println("Tiempo de cifrado (ns): " + tiempoEncrypt);
        System.out.println("Tiempo de descifrado (ns): " + tiempoDecrypt);
        System.out.println("¿El mensaje fue descifrado correctamente?: " + mensajeLargo.equals(descifradoLargo));

        System.out.println("\n      Comparación con distintos tamaños de clave L      ");

        int[] longitudesClave = {10, 50, 100, 500, 1000, 5000};
        for (int L : longitudesClave) {
            String clave = generarClaveNumerica(L);
            BigVigenere bvTest = new BigVigenere(clave);

            long t1 = System.nanoTime();
            String c = bvTest.encrypt(mensajeLargo);
            long t2 = System.nanoTime();

            long t3 = System.nanoTime();
            String d = bvTest.decrypt(c);
            long t4 = System.nanoTime();

            long tiempoEnc = t2 - t1;
            long tiempoDec = t4 - t3;

            System.out.println("L = " + L);
            System.out.println("  Tiempo cifrado:    " + tiempoEnc + " ns");
            System.out.println("  Tiempo descifrado: " + tiempoDec + " ns");
            System.out.println("  ¿Es Correcto?: " + mensajeLargo.equals(d));
            System.out.println("----------------------------------------");
        }
    }

    public static String generarMensajeLargo(int longitud) {
        String base = "abcABC123ñÑ";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < longitud; i++) {
            sb.append(base.charAt(i % base.length()));
        }
        return sb.toString();
    }

    public static String generarClaveNumerica(int longitud) {
        StringBuilder clave = new StringBuilder();
        for (int i = 0; i < longitud; i++) {
            int digito = (int)(Math.random() * 10);
            clave.append(digito);
        }
        return clave.toString();
    }
}

class BigVigenere {
    private int[] key;
    private char[][] alphabet;
    private String characters = "abcdefghijklmnñopqrstuvwxyzABCDEFGHIJKLMNñOPQRSTUVWXYZ0123456789";

    public BigVigenere() {
        generateAlphabet();
    }

    public BigVigenere(String numericKey) {
        setKey(numericKey);
        generateAlphabet();
    }

    private void setKey(String numericKey) {
        key = new int[numericKey.length()];
        for (int i = 0; i < numericKey.length(); i++) {
            key[i] = Character.getNumericValue(numericKey.charAt(i));
        }
    }

    private void generateAlphabet() {
        int size = characters.length();
        alphabet = new char[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                alphabet[i][j] = characters.charAt((i + j) % size);
            }
        }
    }

    public String encrypt(String message) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            int row = characters.indexOf(c);
            if (row == -1) {
                result.append(c);
                continue;
            }
            int col = key[i % key.length];
            result.append(alphabet[row][col]);
        }
        return result.toString();
    }

    public String decrypt(String encryptedMessage) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < encryptedMessage.length(); i++) {
            char c = encryptedMessage.charAt(i);
            int col = key[i % key.length];
            int row = -1;
            for (int r = 0; r < alphabet.length; r++) {
                if (alphabet[r][col] == c) {
                    row = r;
                    break;
                }
            }
            if (row == -1) {
                result.append(c);
            } else {
                result.append(characters.charAt(row));
            }
        }
        return result.toString();
    }

    public void reEncrypt() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingresa el mensaje encriptado: ");
        String mensajeEncriptado = scanner.nextLine();

        String mensajeDescifrado = decrypt(mensajeEncriptado);
        System.out.println("Mensaje descifrado: " + mensajeDescifrado);

        System.out.print("Ingresa la nueva clave numérica: ");
        String nuevaClave = scanner.nextLine();

        setKey(nuevaClave);

        String nuevoMensaje = encrypt(mensajeDescifrado);

        System.out.println("Nuevo mensaje cifrado: " + nuevoMensaje);
    }

    public char search(int position) {
        int filas = alphabet.length;
        int columnas = alphabet[0].length;
        int contador = 0;

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (contador == position) {
                    return alphabet[i][j];
                }
                contador++;
            }
        }
        return '?';
    }

    public char optimalSearch(int position) {
        int filas = alphabet.length;
        int columnas = alphabet[0].length;
        int total = filas * columnas;

        if (position < 0 || position >= total) {
            return '?';
        }

        int fila = position / columnas;
        int columna = position % columnas;

        return alphabet[fila][columna];
    }
}
