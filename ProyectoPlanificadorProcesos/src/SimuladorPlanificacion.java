
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class SimuladorPlanificacion {
    ArrayList<Proceso> procesos;
    int tipoPlanificacion;
    int algoritmo;
    int tiempoSimulacion;
    int quantum;
    Random random;

    public SimuladorPlanificacion() {
        procesos = new ArrayList<>();
        random = new Random();
    }

    public void iniciarSimulacion() {
        Scanner scanner = new Scanner(System.in);

        seleccionarTipoPlanificacion(scanner);
        seleccionarAlgoritmo(scanner);
        generarProcesos();
        ejecutarSimulacion();

        mostrarReporteFinal();

        System.out.println("\nEscribe 0 para reiniciar la simulación o cualquier otro número para salir.");
        int opcionReinicio = scanner.nextInt();
        if (opcionReinicio == 0) {
            procesos.clear();
            System.out.println("Simulación reiniciada.");
            iniciarSimulacion(); // Reinicia la simulación
        }

        scanner.close();
    }

    private void seleccionarTipoPlanificacion(Scanner scanner) {
        System.out.println("Selecciona tipo de planificación (1: Apropiativo, 2: No apropiativo): ");
        tipoPlanificacion = scanner.nextInt();
    }

    private void seleccionarAlgoritmo(Scanner scanner) {
        System.out.println("Selecciona el algoritmo (1: Round Robin, 2: Prioridades, 3: Múltiples colas de prioridad, 4: Proceso más corto primero, 5: Planificación garantizada, 6: Boletos de Lotería, 7: Participación equitativa): ");
        algoritmo = scanner.nextInt();
    }

    private void generarProcesos() {
        int numProcesos = random.nextInt(10) + 1;
        tiempoSimulacion = random.nextInt(11) + 15;
        quantum = random.nextInt(3) + 2;

        for (int i = 0; i < numProcesos; i++) {
            int tiempoEjecucion = random.nextInt(8) + 3;
            int estado = random.nextInt(3) + 1;
            int prioridad = random.nextInt(5) + 1;
            int boletos = prioridad * 10;
            procesos.add(new Proceso(i + 1, tiempoEjecucion, estado, prioridad, boletos));
        }

        System.out.println("Tabla de control de procesos (PCB):");
        procesos.forEach(Proceso::mostrarInfo);
    }

    private void ejecutarSimulacion() {
        int tiempoTranscurrido = 0;
        int cambiosDeProceso = 0;

        while (tiempoTranscurrido < tiempoSimulacion) {
            boolean procesosActivos = false;

            switch (algoritmo) {
                case 1: ejecutarRoundRobin(); break;
                case 2: ejecutarPrioridades(); break;
                case 3: ejecutarMultiplesColas(); break;
                case 4: ejecutarProcesoMasCorto(); break;
                case 5: ejecutarPlanificacionGarantizada(); break;
                case 6: ejecutarBoletosLoteria(); break;
                case 7: ejecutarParticipacionEquitativa(); break;
                default: System.out.println("Algoritmo no válido."); return;
            }

            if (!procesosActivos) break;
            try { Thread.sleep(200); } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

    private void ejecutarRoundRobin() {
        System.out.println("Ejecutando algoritmo Round Robin...");
        for (Proceso proceso : procesos) {
            if (proceso.tiempoEjecucion > 0 && proceso.estado != 3) {
                int tiempoEjec = Math.min(proceso.tiempoEjecucion, quantum);
                proceso.tiempoEjecucion -= tiempoEjec;
                System.out.printf("Proceso %d ejecutándose por %d unidades de tiempo\n", proceso.id, tiempoEjec);
            }
        }
    }

    private void ejecutarPrioridades() {
        System.out.println("Ejecutando algoritmo de Prioridades...");
        procesos.sort((p1, p2) -> Integer.compare(p2.prioridad, p1.prioridad));
        ejecutarRoundRobin(); // Prioridades con quantum
    }

    private void ejecutarMultiplesColas() {
        System.out.println("Ejecutando algoritmo de Múltiples Colas de Prioridad...");
        procesos.sort((p1, p2) -> Integer.compare(p2.prioridad, p1.prioridad));
        ejecutarRoundRobin();
    }

    private void ejecutarProcesoMasCorto() {
        System.out.println("Ejecutando Proceso más corto primero...");
        procesos.sort((p1, p2) -> Integer.compare(p1.tiempoEjecucion, p2.tiempoEjecucion));
        ejecutarRoundRobin();
    }

    private void ejecutarPlanificacionGarantizada() {
        System.out.println("Ejecutando Planificación garantizada...");
        int numUsuarios = 3; // Suponiendo 3 usuarios
        for (Proceso proceso : procesos) {
            int tiempoAsignado = tiempoSimulacion / numUsuarios;
            proceso.tiempoEjecucion -= tiempoAsignado;
            System.out.printf("Proceso %d recibió %d unidades de tiempo\n", proceso.id, tiempoAsignado);
        }
    }

    private void ejecutarBoletosLoteria() {
        System.out.println("Ejecutando algoritmo de Boletos de Lotería...");
        int totalBoletos = procesos.stream().mapToInt(p -> p.boletos).sum();
        int boletoGanador = random.nextInt(totalBoletos);
        int acumulado = 0;
        for (Proceso proceso : procesos) {
            acumulado += proceso.boletos;
            if (acumulado >= boletoGanador && proceso.tiempoEjecucion > 0) {
                proceso.tiempoEjecucion -= quantum;
                System.out.printf("Proceso %d ganó la CPU por la lotería\n", proceso.id);
                break;
            }
        }
    }

    private void ejecutarParticipacionEquitativa() {
        System.out.println("Ejecutando algoritmo de Participación Equitativa...");
        int tiempoPorUsuario = tiempoSimulacion / procesos.size();
        for (Proceso proceso : procesos) {
            proceso.tiempoEjecucion -= tiempoPorUsuario;
            System.out.printf("Proceso %d asignado con %d unidades de tiempo (participación equitativa)\n", proceso.id, tiempoPorUsuario);
        }
    }

    private void mostrarReporteFinal() {
        System.out.println("\nInforme final de ejecución:");
        for (Proceso proceso : procesos) {
            if (proceso.tiempoEjecucion <= 0) {
                System.out.printf("Proceso %d terminó.\n", proceso.id);
            } else {
                System.out.printf("Proceso %d en estado %s.\n", proceso.id, proceso.estado == 3 ? "Bloqueado" : "Listo");
            }
        }
    }
}
