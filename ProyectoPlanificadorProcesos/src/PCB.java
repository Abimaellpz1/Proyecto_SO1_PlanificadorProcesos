package src;

class PCB {
    int id;
    int tiempoEjecucion;
    int estado; // 1: Ejecutando, 2: Listo, 4: Terminado
    int prioridad;
    int boletos;

    public PCB(int id, int tiempoEjecucion, int estado, int prioridad, int boletos) {
        this.id = id;
        this.tiempoEjecucion = tiempoEjecucion;
        this.estado = estado;
        this.prioridad = prioridad;
        this.boletos = boletos;
    }

    public void mostrarInfo() {
        System.out.printf("Proceso %d - Tiempo de ejecución: %d, Prioridad: %d, Boletos: %d, Estado: %d%n",
                id, tiempoEjecucion, prioridad, boletos, estado);
    }
}