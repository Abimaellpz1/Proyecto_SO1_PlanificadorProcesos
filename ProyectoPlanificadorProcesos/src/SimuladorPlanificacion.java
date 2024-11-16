package src;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class SimuladorPlanificacion {
    ArrayList<PCB> procesos;
    int algoritmo;
    int quantum;
    Random random;

    public SimuladorPlanificacion() {
        procesos = new ArrayList<>();
        random = new Random();
    }

    public void iniciarSimulacion() {
        seleccionarAlgoritmo();
        generarProcesos();
        ejecutarSimulacion();
        mostrarReporteFinal();
    }

    private void seleccionarAlgoritmo() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Selecciona el algoritmo de planificación:");
        System.out.println("1. Round Robin");
        System.out.println("2. Prioridades");
        System.out.println("3. Múltiples Colas");
        System.out.println("4. Proceso Más Corto Primero");
        System.out.println("5. Planificación garantizada");
        System.out.println("6. Boletos de Lotería");
        System.out.println("7. Participación equitativa");
        algoritmo = scanner.nextInt();

        if (algoritmo == 1) {
            System.out.println("Seleccionaste Round Robin. Ingresa el quantum:");
            quantum = scanner.nextInt();
        }
    }

    private void generarProcesos() {
        int numProcesos = random.nextInt(5) + 5; // Generar entre 5 y 10 procesos
        for (int i = 0; i < numProcesos; i++) {
            int tiempoEjecucion = random.nextInt(8) + 3;
            int estado = 2; // Estado "Listo"
            int prioridad = random.nextInt(5) + 1;
            int boletos = prioridad * 10;
            procesos.add(new PCB(i + 1, tiempoEjecucion, estado, prioridad, boletos));
        }

        System.out.println("Procesos generados:");
        procesos.forEach(PCB::mostrarInfo);
    }

    private void ejecutarSimulacion() {
        System.out.println("Iniciando simulación...");
        boolean todosTerminados = false;

        while (!todosTerminados) {
            // Verificar si todos los procesos han terminado o están marcados como "Terminado"
            todosTerminados = procesos.stream().allMatch(p -> p.tiempoEjecucion <= 0 || p.estado == 4);
            if (todosTerminados) break;

            // Elegir un proceso aleatorio para que termine y generar un nuevo proceso
            int procesoTerminadoIndex = random.nextInt(procesos.size());
            PCB procesoTerminado = procesos.get(procesoTerminadoIndex);
            if (procesoTerminado.tiempoEjecucion > 0) {
                // El proceso termina o se suspende
                procesoTerminado.estado = 4; // Estado "Terminado"
                System.out.printf("Proceso %d ha terminado o ha sido suspendido.%n", procesoTerminado.id);

                // Generar un nuevo proceso aleatorio
                generarNuevoProceso();

                // Reejecutar el algoritmo con la lista actualizada
                ejecutarAlgoritmo();
            }
        }
    }

    private void generarNuevoProceso() {
        int tiempoEjecucion = random.nextInt(8) + 3;
        int prioridad = random.nextInt(5) + 1;
        int boletos = prioridad * 10;
        int estado = 2; // Listo
        PCB nuevoProceso = new PCB(procesos.size() + 1, tiempoEjecucion, estado, prioridad, boletos);
        procesos.add(nuevoProceso);

        System.out.printf("Se generó un nuevo proceso %d con tiempo %d, prioridad %d.%n",
                nuevoProceso.id, nuevoProceso.tiempoEjecucion, nuevoProceso.prioridad);
    }

    private void ejecutarAlgoritmo() {
        // Ejecutar según el algoritmo seleccionado
        switch (algoritmo) {
            case 1: // Round Robin
                ejecutarRoundRobin();
                break;
            case 2: // Prioridades
                ejecutarPrioridades();
                break;
            case 3: // Múltiples Colas
                ejecutarMultiplesColas();
                break;
            case 4: // Proceso Más Corto Primero
                ejecutarProcesoMasCortoPrimero();
                break;
            case 5: // Planificación garantizada
                ejecutarPlanificacionGarantizada();
                break;
            case 6: // Boletos de Lotería
                ejecutarBoletosDeLoteria();
                break;
            case 7: // Participación equitativa
                ejecutarParticipacionEquitativa();
                break;
            default:
                System.out.println("Algoritmo no válido.");
                return;
        }
    }

    private void ejecutarRoundRobin() {
        System.out.println("Ejecutando con Round Robin...");
        for (PCB proceso : procesos) {
            if (proceso.tiempoEjecucion > 0) {
                proceso.estado = 1; // Estado "Ejecutando"
                int tiempoConsumido = Math.min(proceso.tiempoEjecucion, quantum);
                proceso.tiempoEjecucion -= tiempoConsumido;

                System.out.printf("Ejecutando proceso %d por %d unidades de tiempo. Tiempo restante: %d%n",
                        proceso.id, tiempoConsumido, proceso.tiempoEjecucion);

                if (proceso.tiempoEjecucion <= 0) {
                    proceso.estado = 4; // Terminado
                    System.out.printf("Proceso %d ha terminado.%n", proceso.id);
                }
            }
        }
    }

    private void ejecutarPrioridades() {
        System.out.println("Ejecutando con Prioridades...");
        procesos.sort((p1, p2) -> Integer.compare(p2.prioridad, p1.prioridad));
        for (PCB proceso : procesos) {
            if (proceso.tiempoEjecucion > 0) {
                proceso.estado = 1; // Estado "Ejecutando"
                proceso.tiempoEjecucion = 0; // Simula que el proceso termina inmediatamente
                proceso.estado = 4; // Terminado
                System.out.printf("Proceso %d ha terminado con prioridad %d.%n", proceso.id, proceso.prioridad);
            }
        }
    }

    private void ejecutarMultiplesColas() {
        System.out.println("Ejecutando con Múltiples Colas...");
        for (PCB proceso : procesos) {
            if (proceso.tiempoEjecucion > 0) {
                proceso.estado = 1; // Estado "Ejecutando"
                proceso.tiempoEjecucion = 0; // Simula que el proceso termina
                proceso.estado = 4; // Terminado
                System.out.printf("Proceso %d ha terminado en su cola.%n", proceso.id);
            }
        }
    }

    private void ejecutarProcesoMasCortoPrimero() {
        System.out.println("Ejecutando con Proceso Más Corto Primero...");
        procesos.sort((p1, p2) -> Integer.compare(p1.tiempoEjecucion, p2.tiempoEjecucion));
        for (PCB proceso : procesos) {
            if (proceso.tiempoEjecucion > 0) {
                proceso.estado = 1; // Estado "Ejecutando"
                proceso.tiempoEjecucion = 0; // Simula que el proceso termina
                proceso.estado = 4; // Terminado
                System.out.printf("Proceso %d ha terminado con tiempo más corto.%n", proceso.id);
            }
        }
    }

    private void ejecutarPlanificacionGarantizada() {
        System.out.println("Ejecutando con Planificación Garantizada...");
        int totalUsuarios = procesos.size();
        for (PCB proceso : procesos) {
            int tiempoAsignado = 100 / totalUsuarios;
            proceso.tiempoEjecucion -= tiempoAsignado;
            System.out.printf("Proceso %d recibe %d%% de la CPU.%n", proceso.id, tiempoAsignado);
        }
    }

    private void ejecutarBoletosDeLoteria() {
        System.out.println("Ejecutando con Boletos de Lotería...");
        ArrayList<PCB> candidatos = new ArrayList<>();
        for (PCB proceso : procesos) {
            for (int i = 0; i < proceso.boletos; i++) {
                candidatos.add(proceso);
            }
        }

        PCB ganador = candidatos.get(random.nextInt(candidatos.size()));
        ganador.estado = 1; // Estado "Ejecutando"
        ganador.tiempoEjecucion = 0; // Simula que el proceso termina
        ganador.estado = 4; // Terminado
        System.out.printf("Proceso %d ha ganado el boleto de lotería y ha terminado.%n", ganador.id);
    }

    private void ejecutarParticipacionEquitativa() {
        System.out.println("Ejecutando con Participación Equitativa...");
        int totalUsuarios = procesos.size();
        int tiempoPorUsuario = 100 / totalUsuarios;

        for (PCB proceso : procesos) {
            proceso.tiempoEjecucion -= tiempoPorUsuario;
            System.out.printf("Proceso %d recibe %d%% de la CPU.%n", proceso.id, tiempoPorUsuario);
        }
    }

    private void mostrarReporteFinal() {
        System.out.println("\nReporte Final:");
        procesos.forEach(PCB::mostrarInfo);
    }

    public static void main(String[] args) {
        SimuladorPlanificacion simulador = new SimuladorPlanificacion();
        simulador.iniciarSimulacion();
    }
}

class PCB {
    int id;
    int tiempoEjecucion;
    int estado; // 1 - Ejecutando, 2 - Listo, 3 - Bloqueado, 4 - Terminado
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
        System.out.printf("Proceso ID: %d, Tiempo de Ejecución: %d, Estado: %d, Prioridad: %d, Boletos: %d%n",
                id, tiempoEjecucion, estado, prioridad, boletos);
    }
}
