package input;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Patrón de Entrada de Datos: Forgiving Format.
 * Acepta cualquier formato razonable y lo normaliza internamente.
 * Nunca lanza excepciones ni rechaza al usuario por el formato.
 */
public class ValidadorForgiving {

    private static final Pattern NO_DIGITO = Pattern.compile("\\D");
    private static final Pattern PREFIJO_PAIS = Pattern.compile("^57(?=\\d{10}$)");
    private static final Pattern PREFIJO_TRONCAL = Pattern.compile("^0(?=\\d{10}$)");

    // Día, mes, año con cualquier separador (/, -, ., espacio) o año primero.
    private static final Pattern FECHA_DMA =
            Pattern.compile("^\\s*(\\d{1,2})\\D+(\\d{1,2})\\D+(\\d{2}|\\d{4})\\s*$");
    private static final Pattern FECHA_AMD =
            Pattern.compile("^\\s*(\\d{4})\\D+(\\d{1,2})\\D+(\\d{1,2})\\s*$");

    /** "+57 (310) 999-8877" -> "3109998877". Entrada nula o vacía devuelve "". */
    public String normalizarTelefono(String entrada) {
        if (entrada == null) {
            return "";
        }
        String soloDigitos = NO_DIGITO.matcher(entrada).replaceAll("");
        if (soloDigitos.startsWith("00")) {
            soloDigitos = soloDigitos.substring(2);
        }
        soloDigitos = PREFIJO_PAIS.matcher(soloDigitos).replaceFirst("");
        soloDigitos = PREFIJO_TRONCAL.matcher(soloDigitos).replaceFirst("");
        return soloDigitos;
    }

    /** Un celular colombiano normalizado tiene 10 dígitos. */
    public boolean telefonoCompleto(String telefonoNormalizado) {
        return telefonoNormalizado != null && telefonoNormalizado.matches("\\d{10}");
    }

    /** Acepta 5/11/2026, 05-11-26, 2026.11.05, "5 11 2026" -> "2026-11-05". */
    public String normalizarFecha(String entrada) {
        if (entrada == null) {
            return "";
        }
        Matcher amd = FECHA_AMD.matcher(entrada);
        if (amd.matches()) {
            return formatear(amd.group(1), amd.group(2), amd.group(3));
        }
        Matcher dma = FECHA_DMA.matcher(entrada);
        if (dma.matches()) {
            String anio = dma.group(3);
            if (anio.length() == 2) {
                anio = "20" + anio;
            }
            return formatear(anio, dma.group(2), dma.group(1));
        }
        return entrada.trim(); // No se bloquea: se conserva lo ingresado.
    }

    /** Correo: recorta espacios y pasa a minúsculas. */
    public String normalizarCorreo(String entrada) {
        return entrada == null ? "" : entrada.trim().toLowerCase();
    }

    private String formatear(String anio, String mes, String dia) {
        return String.format("%s-%02d-%02d", anio, Integer.parseInt(mes), Integer.parseInt(dia));
    }
}
