import java.util.Scanner;

public class MemoriaMejorAjuste {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        int NUM_PARTITIONS;
        int[] PARTITION_SIZES;
        int SYSTEM_PARTITION_SIZE;
        int TOTAL_MEMORY;
        int[] memory;
        String[] process_names;

        System.out.print("Ingrese la cantidad de particiones: ");
        NUM_PARTITIONS = scanner.nextInt();
        
        PARTITION_SIZES = new int[NUM_PARTITIONS];
        for (int i = 0; i < NUM_PARTITIONS; i++) {
            System.out.print("Ingrese el tamaño para la partición " + (i + 1) + ": ");
            PARTITION_SIZES[i] = scanner.nextInt();
        }

        System.out.print("Ingrese el tamaño para la partición del sistema operativo: ");
        SYSTEM_PARTITION_SIZE = scanner.nextInt();

        TOTAL_MEMORY = 0;
        for (int size : PARTITION_SIZES) {
            TOTAL_MEMORY += size;
        }
        TOTAL_MEMORY += SYSTEM_PARTITION_SIZE;

        memory = new int[NUM_PARTITIONS + 1];
        for (int i = 0; i < memory.length; i++) {
            memory[i] = -1;
        }

        process_names = new String[NUM_PARTITIONS + 1];
        for (int i = 0; i < process_names.length; i++) {
            process_names[i] = "";
        }

        while (true) {
            System.out.println("Estado de la memoria: " + TOTAL_MEMORY);
            System.out.println("Partición del sistema: Sistema Operativo (" + SYSTEM_PARTITION_SIZE + " MB)");

            for (int i = 0; i < NUM_PARTITIONS; i++) {
                if (memory[i] == -1) {
                    System.out.println("Partición " + PARTITION_SIZES[i] + ": Libre");
                } else {
                    System.out.println("Partición " + PARTITION_SIZES[i] + ": Proceso '" + process_names[i] + "' (" + memory[i] + " MB)");
                }
            }

            System.out.println("\n1. Asignar proceso a partición (Siguiente ajuste)");
            System.out.println("2. Liberar partición");
            System.out.println("3. Salir");

            int choice = scanner.nextInt();

            if (choice == 1) {
                System.out.print("Ingrese el tamaño del proceso: ");
                int processSize = scanner.nextInt();
                System.out.print("Ingrese el nombre del proceso: ");
                scanner.nextLine();  // Consume the newline character
                String processName = scanner.nextLine();

                if (processSize <= TOTAL_MEMORY) {
                    int best_fit_index = findBestFitPartition(processSize, memory, PARTITION_SIZES);

                    if (best_fit_index != -1) {
                        memory[best_fit_index] = processSize;
                        process_names[best_fit_index] = processName;
                        System.out.println("Proceso '" + processName + "' asignado a la partición " + best_fit_index + ": " + memory[best_fit_index] + " MB");
                        TOTAL_MEMORY -= processSize;
                    } else {
                        System.out.println("No se pudo asignar el proceso a ninguna partición.");
                    }
                } else {
                    System.out.println("El tamaño del proceso excede la memoria total.");
                }
            } else if (choice == 2) {
                System.out.print("Ingrese el índice de la partición a liberar: ");
                int partitionIndex = scanner.nextInt();

                if (partitionIndex >= 0 && partitionIndex < NUM_PARTITIONS && memory[partitionIndex] != -1) {
                    process_names[partitionIndex] = "";
                    TOTAL_MEMORY += memory[partitionIndex];
                    memory[partitionIndex] = -1;
                    System.out.println("Partición liberada.");
                } else {
                    System.out.println("Índice de partición inválido o la partición ya está libre.");
                }
            } else if (choice == 3) {
                break;
            } else {
                System.out.println("Opción inválida.");
            }
            
            // Crear una nueva partición con el sobrante acumulado si todas las particiones están ocupadas
            if (areAllPartitionsOccupied(memory)) {
                int accumulated_memory = 0;
                for (int m : memory) {
                    if (m != -1) {
                        accumulated_memory += m;
                    }
                }
                
                int new_partition_size = accumulated_memory;
                if (new_partition_size > 0) {
                    int[] new_partition_sizes = new int[PARTITION_SIZES.length + 1];
                    System.arraycopy(PARTITION_SIZES, 0, new_partition_sizes, 0, PARTITION_SIZES.length);
                    new_partition_sizes[PARTITION_SIZES.length] = new_partition_size;
                    PARTITION_SIZES = new_partition_sizes;
                    
                    int[] new_memory = new int[memory.length + 1];
                    System.arraycopy(memory, 0, new_memory, 0, memory.length);
                    new_memory[memory.length] = -1;
                    memory = new_memory;
                    
                    String[] new_process_names = new String[process_names.length + 1];
                    System.arraycopy(process_names, 0, new_process_names, 0, process_names.length);
                    new_process_names[process_names.length] = "";
                    process_names = new_process_names;
                    
                    NUM_PARTITIONS++;
                    System.out.println("Se creó una nueva partición de tamaño " + new_partition_size + " MB.");
                    TOTAL_MEMORY += new_partition_size;
                }
            }
        }

        System.out.println("Saliendo del programa.");
    }

    private static int findBestFitPartition(int processSize, int[] memory, int[] PARTITION_SIZES) {
        int best_fit_index = -1;
        int smallest_free_space = Integer.MAX_VALUE;

        for (int i = 0; i < memory.length - 1; i++) {
            if (memory[i] == -1 && PARTITION_SIZES[i] >= processSize) {
                int free_space = PARTITION_SIZES[i] - processSize;
                if (free_space < smallest_free_space) {
                    best_fit_index = i;
                    smallest_free_space = free_space;
                }
            }
        }

        return best_fit_index;
    }

    private static boolean areAllPartitionsOccupied(int[] memory) {
        for (int m : memory) {
            if (m == -1) {
                return false;
            }
        }
        return true;
    }
}
