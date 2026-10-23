package content;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Patrón de Contenido y Estructura: Progressive Disclosure (Acordeón).
 * Solo muestra nombre y estado; los detalles técnicos se revelan bajo demanda.
 */
public class TarjetaElemento {

    private final String nombre;
    private final String estado;
    private final Map<String, String> detalles = new LinkedHashMap<>();
    private boolean verAvanzado = false;

    public TarjetaElemento(String nombre, String estado) {
        this.nombre = nombre;
        this.estado = estado;
    }

    public TarjetaElemento agregarDetalle(String clave, String valor) {
        detalles.put(clave, valor);
        return this;
    }

    public void setVerAvanzado(boolean verAvanzado) {
        this.verAvanzado = verAvanzado;
    }

    public void alternarAvanzado() {
        verAvanzado = !verAvanzado;
    }

    public boolean isVerAvanzado() {
        return verAvanzado;
    }

    public String renderizar() {
        StringBuilder sb = new StringBuilder();
        if (!verAvanzado) {
            sb.append("[Vista Resumen] ").append(nombre).append(" · Estado: ").append(estado);
        } else {
            sb.append("[Acordeón Desplegado] ");
            boolean primero = true;
            for (Map.Entry<String, String> e : detalles.entrySet()) {
                if (!primero) {
                    sb.append(" | ");
                }
                sb.append(e.getKey()).append(": ").append(e.getValue());
                primero = false;
            }
        }
        return sb.toString();
    }
}
