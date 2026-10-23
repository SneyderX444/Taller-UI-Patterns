package main;

import content.TarjetaElemento;
import input.ValidadorForgiving;
import navigation.BreadcrumbNavigator;
import state.GestorSesionUI;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        casoNavegacion();
        casoContenido();
        casoForgiving();
        casoCompleteness();
        casoLazyRegistrationYToast();
        casoEticaSinDarkPatterns();
    }

    // Caso 1
    private static void casoNavegacion() {
        System.out.println("--- 1. PATRÓN DE NAVEGACIÓN (BREADCRUMBS + TABS) ---");
        BreadcrumbNavigator nav = new BreadcrumbNavigator(List.of("Catálogo", "Reservas", "Mi cuenta"));
        System.out.println("Ruta actual: " + nav.getRuta());
        nav.seleccionarPestana("Catálogo");
        System.out.println("Ruta actual: " + nav.getRuta());
        nav.entrarSubcategoria("Servidor Principal");
        System.out.println("Ruta actual: " + nav.getRuta());
        nav.seleccionarPestana("Reservas");
        nav.entrarSubcategoria("Laboratorio de Redes");
        System.out.println("Ruta actual: " + nav.getRuta());
        nav.irANivel(1);
        System.out.println("Ruta actual (clic en breadcrumb 'Reservas'): " + nav.getRuta());
        System.out.println();
    }

    // Caso 2
    private static void casoContenido() {
        System.out.println("--- 2. PATRÓN DE CONTENIDO Y ESTRUCTURA (PROGRESSIVE DISCLOSURE) ---");
        TarjetaElemento tarjeta = new TarjetaElemento("Servidor Principal", "Activo")
                .agregarDetalle("IP", "192.168.1.10")
                .agregarDetalle("Puerto", "8080")
                .agregarDetalle("RAM", "4GB/16GB");
        tarjeta.setVerAvanzado(false);
        System.out.println(tarjeta.renderizar());
        System.out.println("[Usuario solicita ver detalles avanzados...]");
        tarjeta.setVerAvanzado(true);
        System.out.println(tarjeta.renderizar());
        System.out.println();
    }

    // Caso 3
    private static void casoForgiving() {
        System.out.println("--- 3. PATRÓN DE ENTRADA DE DATOS (FORGIVING FORMAT) ---");
        ValidadorForgiving v = new ValidadorForgiving();
        String[] entradas = {"+57 (310) 999-8877", "310 999 8877", "57-310-999-8877", "(310)9998877"};
        for (String e : entradas) {
            System.out.println("Entrada del usuario: \"" + e + "\"");
            System.out.println("Teléfono normalizado sin error: \"" + v.normalizarTelefono(e) + "\"");
        }
        System.out.println("Fecha \"5/11/2026\" -> \"" + v.normalizarFecha("5/11/2026") + "\"");
        System.out.println("Fecha \"05-11-26\" -> \"" + v.normalizarFecha("05-11-26") + "\"");
        System.out.println();
    }

    // Caso 4
    private static void casoCompleteness() {
        System.out.println("--- 4. PATRÓN DE FEEDBACK Y ESTADO (COMPLETENESS METER) ---");
        GestorSesionUI g = new GestorSesionUI();
        System.out.println(g.renderizarMedidor());
        g.ingresarCampo(GestorSesionUI.CAMPO_EQUIPO, "Laboratorio de Redes");
        System.out.println(g.renderizarMedidor());
        g.ingresarCampo(GestorSesionUI.CAMPO_TELEFONO, "+57 (300) 123-4567");
        System.out.println(g.renderizarMedidor());
        String correo = "estudiante@udistrital.edu.co";
        System.out.println("Usuario ingresa correo: \"" + correo + "\"");
        g.ingresarCampo(GestorSesionUI.CAMPO_CORREO, correo);
        System.out.println(g.renderizarMedidor());
        System.out.println();
    }

    // Caso 5
    private static void casoLazyRegistrationYToast() {
        System.out.println("--- 5. LAZY REGISTRATION + TOAST ---");
        GestorSesionUI g = new GestorSesionUI();
        g.ingresarCampo(GestorSesionUI.CAMPO_EQUIPO, "Laboratorio de Redes");
        g.ingresarCampo(GestorSesionUI.CAMPO_TELEFONO, "+57 (300) 123-4567");
        g.ingresarCampo(GestorSesionUI.CAMPO_CORREO, "estudiante@udistrital.edu.co");
        System.out.println("(La reserva se armó sin iniciar sesión)");
        System.out.println("Usuario intenta guardar sin haber iniciado sesión... usuarioActivo = "
                + g.isUsuarioActivo());
        g.guardarReserva();
        System.out.println("Borrador temporal conservado: " + g.tieneBorrador());
        System.out.println("Usuario completa registro rápido -> Sesión activa = true");
        g.registroRapido();
        System.out.println("Usuario intenta guardar... usuarioActivo = " + g.isUsuarioActivo());
        g.guardarReserva();
        System.out.println();
    }

    // Restricción ética
    private static void casoEticaSinDarkPatterns() {
        System.out.println("--- EXTRA: CERO DARK PATTERNS (SALIDA EN UN SOLO PASO) ---");
        GestorSesionUI g = new GestorSesionUI();
        g.registroRapido();
        g.ingresarCampo(GestorSesionUI.CAMPO_EQUIPO, "Osciloscopio");
        System.out.println("Usuario solicita cancelar la reserva (1 paso):");
        g.cancelarReserva();
        System.out.println("Usuario solicita cerrar sesión (1 paso):");
        g.cerrarSesion();
    }
}
