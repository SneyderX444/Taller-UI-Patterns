# Taller para la Casa – User Interface Patterns (Java)

Sistema de Configuración y Reservas Universitarias. Autor: Sneyder — Universidad Distrital Francisco José de Caldas.

## Estructura
```
src/
├── navigation/BreadcrumbNavigator.java   (Breadcrumbs + Tabs)
├── content/TarjetaElemento.java          (Progressive Disclosure)
├── input/ValidadorForgiving.java         (Forgiving Format)
├── state/GestorSesionUI.java             (Lazy Registration, Completeness Meter, Toast)
└── main/Main.java                        (5 casos de prueba + extra ético)
```

## Compilar y ejecutar
```
mkdir out
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -cp out main.Main
```
Requiere JDK 17 o superior. La salida de referencia está en `salida_consola.txt`.

## Cómo se evitan los Dark Patterns
La implementación nunca culpa ni bloquea al usuario: `ValidadorForgiving` normaliza teléfonos y fechas en cualquier formato sin lanzar errores, y el formulario se arma sin sesión (Lazy Registration), pidiendo cuenta solo al guardar y conservando un borrador para no perder el avance (sin Roach Motel). `cancelarReserva()` y `cerrarSesion()` se ejecutan en un solo paso, sin pantallas intermedias, y sus mensajes son neutros ("Reserva cancelada.", "Sesión cerrada."), sin Confirmshaming. El Completeness Meter muestra exactamente qué falta y no existen costos ocultos ni elementos engañosos (sin Drip Pricing ni Misdirection): cada acción hace solo lo que dice.
