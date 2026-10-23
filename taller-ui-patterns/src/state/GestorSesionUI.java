package state;

import input.ValidadorForgiving;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Patrón de Entrada (Lazy Registration) y Feedback y Estado
 * (Completeness Meter + Toast).
 */
public class GestorSesionUI {

    public static final String CAMPO_EQUIPO = "Equipo a reservar";
    public static final String CAMPO_TELEFONO = "Teléfono";
    public static final String CAMPO_CORREO = "Correo de contacto";

    private final ValidadorForgiving validador = new ValidadorForgiving();
    private final Map<String, String> formulario = new LinkedHashMap<>();
    private final List<String> toasts = new ArrayList<>();

    private Map<String, String> borrador = null;
    private boolean usuarioActivo = false;
    private boolean reservaGuardada = false;

    public GestorSesionUI() {
        formulario.put(CAMPO_EQUIPO, "");
        formulario.put(CAMPO_TELEFONO, "");
        formulario.put(CAMPO_CORREO, "");
    }

    // ---------- Formulario (sin exigir sesión) ----------

    public void ingresarCampo(String campo, String valor) {
        if (!formulario.containsKey(campo)) {
            return;
        }
        String normalizado;
        if (CAMPO_TELEFONO.equals(campo)) {
            normalizado = validador.normalizarTelefono(valor);
        } else if (CAMPO_CORREO.equals(campo)) {
            normalizado = validador.normalizarCorreo(valor);
        } else {
            normalizado = valor == null ? "" : valor.trim();
        }
        formulario.put(campo, normalizado);
    }

    public String getCampo(String campo) {
        return formulario.getOrDefault(campo, "");
    }

    // ---------- Completeness Meter ----------

    private boolean campoListo(String campo) {
        String v = formulario.get(campo);
        if (v == null || v.isBlank()) {
            return false;
        }
        return !CAMPO_TELEFONO.equals(campo) || validador.telefonoCompleto(v);
    }

    public int camposListos() {
        int n = 0;
        for (String campo : formulario.keySet()) {
            if (campoListo(campo)) {
                n++;
            }
        }
        return n;
    }

    /** 0%, 33%, 67% o 100% para 3 campos. */
    public int calcularProgreso() {
        return Math.round(camposListos() * 100f / formulario.size());
    }

    public List<String> camposFaltantes() {
        List<String> faltan = new ArrayList<>();
        for (String campo : formulario.keySet()) {
            if (!campoListo(campo)) {
                faltan.add(campo);
            }
        }
        return faltan;
    }

    public String renderizarMedidor() {
        int progreso = calcularProgreso();
        if (progreso == 100) {
            return "[Progreso: 100%] ¡Formulario completo!";
        }
        return "[Progreso: " + progreso + "%] Campos listos: " + camposListos()
                + " de " + formulario.size() + " (Falta: " + String.join(", ", camposFaltantes()) + ")";
    }

    // ---------- Lazy Registration ----------

    public boolean isUsuarioActivo() {
        return usuarioActivo;
    }

    public boolean tieneBorrador() {
        return borrador != null;
    }

    /**
     * Intenta guardar la reserva. Sin sesión: conserva un borrador temporal
     * y solicita crear cuenta. Con sesión: guarda y emite el Toast.
     */
    public boolean guardarReserva() {
        if (!usuarioActivo) {
            borrador = new LinkedHashMap<>(formulario);
            System.out.println("[Lazy Registration] Avance guardado en borrador temporal. "
                    + "Por favor cree su cuenta para finalizar.");
            return false;
        }
        reservaGuardada = true;
        borrador = null;
        emitirToast("Reserva guardada exitosamente.");
        return true;
    }

    /** Registro rápido: activa la sesión y recupera el borrador. */
    public void registroRapido() {
        usuarioActivo = true;
        if (borrador != null) {
            formulario.putAll(borrador);
        }
    }

    // ---------- Salidas directas, un solo paso, mensajes neutros ----------

    public void cancelarReserva() {
        formulario.replaceAll((k, v) -> "");
        borrador = null;
        reservaGuardada = false;
        emitirToast("Reserva cancelada.");
    }

    public void cerrarSesion() {
        usuarioActivo = false;
        emitirToast("Sesión cerrada.");
    }

    public boolean isReservaGuardada() {
        return reservaGuardada;
    }

    // ---------- Toast ----------

    public void emitirToast(String mensaje) {
        toasts.add(mensaje);
        System.out.println("[Toast de Estado] " + mensaje);
    }

    public List<String> getToasts() {
        return List.copyOf(toasts);
    }
}
