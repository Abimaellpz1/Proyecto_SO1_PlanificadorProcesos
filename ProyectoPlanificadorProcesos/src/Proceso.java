
package src;
public class Proceso {
    int id;
    int tiempoEjecucion;
    int estado; // 1: en ejecución, 2: listo, 3: bloqueado
    int prioridad;
    int boletos;

    public Proceso(int id, int tiempoEjecucion, int estado, int prioridad, int boletos) {
        this.id = id;
        this.tiempoEjecucion = tiempoEjecucion;
        this.estado = estado;
        this.prioridad = prioridad;
        this.boletos = boletos;
    }

    public void mostrarInfo() {
        System.out.printf("Proceso ID: %d | Tiempo de Ejecución: %d | Estado: %s | Prioridad: %d | Boletos: %d\n",
                          id, tiempoEjecucion, estado == 1 ? "Ejecución" : estado == 2 ? "Listo" : "Bloqueado", prioridad, boletos);
    }
}
