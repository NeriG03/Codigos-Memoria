import java.util.Scanner;
import java.util.ArrayList;

public class MemoriaSigAjuste {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int NUM_PARTITIONS;
        ArrayList<Integer> PARTITION_SIZES = new ArrayList<>();
        int SYSTEM_PARTITION_SIZE = 0;
        int TOTAL_MEMORY = 0;
        ArrayList<Integer> memory = new ArrayList<>();
        ArrayList<String> process_names = new ArrayList<>();
        int last_allocated_partition = 0;
        int accumulated_memory = 0;

        System.out.print("Ingrese la cantidad de particiones: ");
        NUM_PARTITIONS = scanner.nextInt();

        for (int i = 0; i < NUM_PARTITIONS; i++) {
            System.out.print("Ingrese el tamaño para la partición " + (i + 1) + ": ");
            int size = scanner.nextInt();
            PARTITION_SIZES.add(size);
            memory.add(-1);
            process_names.add("");
            TOTAL_MEMORY += size;
        }

        System.out.print("Ingrese el tamaño para la partición del sistema operativo: ");
        SYSTEM_PARTITION_SIZE = scanner.nextInt();
        TOTAL_MEMORY += SYSTEM_PARTITION_SIZE;

        while (true) {
            System.out.println("Estado de la memoria: " + TOTAL_MEMORY);
            System.out.println("Partición del sistema: Sistema Operativo (" + SYSTEM_PARTITION_SIZE + " MB)");

            for (int i = 0; i < NUM_PARTITIONS; i++) {
                if (memory.get(i) == -1) {
                    System.out.println("Partición " + PARTITION_SIZES.get(i) + ": Libre");
                } else {
                    System.out.println("Partición " + PARTITION_SIZES.get(i) + ": Proceso '" +
                                       process_names.get(i) + "' (" + memory.get(i) + " MB)");
                }
            }

            System.out.println("\n1. Asignar proceso a partición (Siguiente ajuste)");
            System.out.println("2. Liberar partición");
            System.out.println("3. Mostrar nueva partición con acumulados");
            System.out.println("4. Salir");

            int choice = scanner.nextInt();

            if (choice == 1) {
                System.out.print("Ingrese el tamaño del proceso: ");
                int processSize = scanner.nextInt();
                scanner.nextLine();  
                System.out.print("Ingrese el nombre del proceso: ");
                String processName = scanner.nextLine();

                if (processSize <= TOTAL_MEMORY) {
                    boolean allocated = false;

                    for (int j = 0; j < NUM_PARTITIONS; j++) {
                        int i = last_allocated_partition % NUM_PARTITIONS;
                        if (memory.get(i) == -1 && PARTITION_SIZES.get(i) >= processSize) {
                            memory.set(i, processSize);
                            process_names.set(i, processName);
                            allocated = true;
                            last_allocated_partition = (i + 1) % NUM_PARTITIONS;

                            System.out.println("Proceso '" + processName + "' asignado a la partición " +
                                               i + ": " + memory.get(i) + " MB");

                            TOTAL_MEMORY -= processSize;
                            accumulated_memory += PARTITION_SIZES.get(i) - processSize;

                            break;
                        }

                        last_allocated_partition = (i + 1) % NUM_PARTITIONS;
                    }

                    if (!allocated) {
                        System.out.println("No se pudo asignar el proceso a ninguna partición.");
                    }
                } else {
                    System.out.println("El tamaño del proceso excede la memoria total.");
                }
            } else if (choice == 2) {
                System.out.print("Ingrese el índice de la partición a liberar: ");
                int partitionIndex = scanner.nextInt();

                if (partitionIndex >= 0 && partitionIndex < NUM_PARTITIONS && memory.get(partitionIndex) != -1) {
                    process_names.set(partitionIndex, "");
                    TOTAL_MEMORY += memory.get(partitionIndex);
                    memory.set(partitionIndex, -1);
                    System.out.println("Partición liberada.");
                } else {
                    System.out.println("Índice de partición inválido o la partición ya está libre.");
                }
            } else if (choice == 3) {
                showAccumulatedPartition();
            } else if (choice == 4) {
                break;
            } else {
                System.out.println("Opción inválida.");
            }

            if (memory.stream().noneMatch(memorySize -> memorySize == -1)) {
                int new_partition_size = accumulated_memory;
                if (new_partition_size > 0) {
                    PARTITION_SIZES.add(new_partition_size);
                    memory.add(-1);
                    process_names.add("");
                    accumulated_memory = 0;
                    NUM_PARTITIONS++;
                    System.out.println("Se creó una nueva partición de tamaño " + new_partition_size + " MB.");
                    TOTAL_MEMORY += new_partition_size;
                }
            }
        }

        System.out.println("Saliendo del programa.");
    }

    public static void showAccumulatedPartition() {
        
    }
}
