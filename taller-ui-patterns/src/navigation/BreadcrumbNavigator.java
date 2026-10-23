package navigation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Patrón de Navegación: Breadcrumbs + Tabs.
 * Mantiene la ruta jerárquica actual (Inicio > Sección > Subsección).
 */
public class BreadcrumbNavigator {

    private static final String RAIZ = "Inicio";
    private static final String SEPARADOR = " > ";

    private final List<String> pestanas;
    private final List<String> ruta = new ArrayList<>();

    public BreadcrumbNavigator(List<String> pestanas) {
        this.pestanas = new ArrayList<>(pestanas);
        irAInicio();
    }

    /** Cambia de pestaña: la ruta se reinicia a Inicio > Pestaña. */
    public void seleccionarPestana(String pestana) {
        if (!pestanas.contains(pestana)) {
            return; // Sin error hacia el usuario: se conserva la ubicación actual.
        }
        irAInicio();
        ruta.add(pestana);
    }

    /** Entra a un nivel más profundo de la sección actual. */
    public void entrarSubcategoria(String subcategoria) {
        if (ruta.size() > 1 && subcategoria != null && !subcategoria.isBlank()) {
            ruta.add(subcategoria);
        }
    }

    /** Equivale a hacer clic en un elemento del breadcrumb (0 = Inicio). */
    public void irANivel(int indice) {
        if (indice >= 0 && indice < ruta.size()) {
            while (ruta.size() > indice + 1) {
                ruta.remove(ruta.size() - 1);
            }
        }
    }

    public void volver() {
        if (ruta.size() > 1) {
            ruta.remove(ruta.size() - 1);
        }
    }

    public void irAInicio() {
        ruta.clear();
        ruta.add(RAIZ);
    }

    public String getRuta() {
        return String.join(SEPARADOR, ruta);
    }

    public List<String> getPestanas() {
        return Collections.unmodifiableList(pestanas);
    }
}
