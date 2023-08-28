import java.util.Scanner;

public class MemoriaPrimerAjuste {
    public static void main(String[] args) {
        int MT = 2000;
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite el numero de particiones que desea: ");
        int part = scanner.nextInt();

        int[] particiones = new int[part];
        String[] nombrepart = new String[part];
        int[] parttamano = new int[part];
        int[] tamanoproceso = new int[part];

        for (int i = 0; i < part; i++) {
            nombrepart[i] = "";
            particiones[i] = i;
            tamanoproceso[i] = 0;
        }

        parttamano[0] = 100;
        tamanoproceso[0] = 100;
        nombrepart[0] = "WINDOWS";

        for (int i = 1; i < part; i++) {
            System.out.print("Ingrese el tamano de la particion " + (i + 1) + ": ");
            parttamano[i] = scanner.nextInt();
        }

        int op = 1;
        while (op != 5) {
            System.out.println("1. Ingresar Proceso.");
            System.out.println("2. Estado de particiones.");
            System.out.println("3. Ver espacio disponible en memoria.");
            System.out.println("4. Procesos.");
            System.out.println("5. Salir.");
            System.out.print("\nIngrese la opcion que desea: ");
            op = scanner.nextInt();
            switch (op) {
                case 1: {
                    String nombreProceso;
                    int tamanoProceso;
                    System.out.print("Ingrese el nombre de su proceso: ");
                    nombreProceso = scanner.next();
                    System.out.print("Ingrese el tamano de su proceso: ");
                    tamanoProceso = scanner.nextInt();

                    boolean asignado = false;

                    for (int i = 0; i < part; i++) {
                        if (nombrepart[i].equals("") && tamanoproceso[i] == 0) {
                            if (tamanoProceso <= parttamano[i]) {
                                tamanoproceso[i] = tamanoProceso;
                                nombrepart[i] = nombreProceso;
                                System.out.println("Su proceso ha sido asignado en la particion " + (i + 1));
                                asignado = true;
                                break;
                            }
                        }
                    }

                    if (!asignado) {
                        System.out.println("Su proceso no pudo ser asignado a ninguna particion");
                    }
                    break;
                }

                case 2: {
                    for (int i = 0; i < part; i++) {
                        if (tamanoproceso[i] == 0 && nombrepart[i].equals("")) {
                            System.out.println("La particion " + (i + 1) + " esta: Vacia. " +
                                    "libre: " + (parttamano[i] - tamanoproceso[i]));
                        } else {
                            System.out.println("La particion " + (i + 1) + " esta: Ocupada. " +
                                    "libre: " + (parttamano[i] - tamanoproceso[i]));
                        }
                    }
                    break;
                }

                case 3: {
                    int suma = 0;
                    for (int i = 0; i < part; i++) {
                        suma += tamanoproceso[i];
                    }

                    System.out.println("La memoria restante es " + (MT - suma));
                    break;
                }
                case 4: {
                    for (int i = 0; i < part; i++) {
                        if (nombrepart[i].equals("") && tamanoproceso[i] == 0) {
                            System.out.println("En la particion " + (i + 1) + " no hay ningun proceso");
                        } else {
                            System.out.println("En la particion " + (i + 1) + " esta el proceso " + nombrepart[i] +
                                    " con un tamano de " + tamanoproceso[i]);
                        }
                    }
                    break;
                }
                default:
                    System.out.println("Digite una opcion correcta");
            }
        }
    }
}
