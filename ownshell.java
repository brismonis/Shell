import static cTools.KernelWrapper.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

class ownshell {

    // fd = filedeskriptor = geöffnete Datei (weil jedes Mal öffnen zu aufwendig wäre)
    // enthält drei Infos
    // beim umlenken wird die geöffnete Datei in einen anderen Prozess verschoben
    final static int fd_in = 0;
	final static int fd_out = 1;
    
    public static void main(String[] args) throws Exception {
        //Aufgabe 1.1 Zeile einlesen mit Endlosschleife
		while (true) {
            // Prompt
			System.out.print(">> ");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            // Aufgabe 1.2 Zerlegen der Zeile in Worte und in Liste speichern
			//String[] input = br.readLine().split("\\|\\s+");
            // splitten bei whitespaces
            //String[] input = br.readLine().split("\\s+");
            
            //Aufgabe 2: Verkettung mit && ermöglichen
            // Matrix erstellen
            String[] input = br.readLine().split("\\s+&&\\s+");
            // System.out.print(Arrays.toString(input)); gibt [nano a, nano b, ..]
            String[][] chain_input = new String[input.length][];
            for(int i = 0; i<input.length; i++){
                //\\s+ will split your string on one or more spaces
                chain_input[i] = input[i].split("\\s+");
                // gibt [nano, a][nano, b]
                System.out.print(Arrays.toString(chain_input[i])); 
            }
            //System.out.println(chain_input.length); 
            // für nano a && nano b
            // System.out.println(chain_input[0][0]); // gibt nano
            // System.out.println(chain_input[1][0]); // gibt nano 
            // System.out.println(chain_input[1][1]); // gibt b
            // System.out.println(chain_input[0][1]); // gibt a

            // Matrix: 0        1
            //      0  [nano] [a]
            //      1  [nano] [b]
            
            // bei exit shell verlassen
            if (chain_input[0][0].equals("exit")) {
					System.out.println(">> shell closed");
					exit(0);
            }
            
            //AUFGABE 3 a&b
            //stdin und stdout umlenken mit < (stdin), > (stdout) und | (pipes)
            // "Umlenken" ist der Fachjargon für: Schließen der bisherigen Datei, neu öffnen einer anderen unter derselben Filedeskriptor-Nummer.
            // Der Filedeskriptor, der von einem erfolgreichen Aufruf von [to open()] zurückgegeben wird, ist der Filedeskriptor mit der niedrigsten Nummer, der derzeit nicht für den Prozess geöffnet ist.
            // int pipe_count = chain_input.length-1;
            // //Filedeskriptoren umlenken
            // int stdin_old = 0; //standard
            // int stdout_old = 1; //standard
            // int index_out=0;
            // int index_in=0;
            // boolean stdinUmlenken = false;
            // boolean stdoutUmlenken = false;

            // int newfd = 0;

            // stdin umlenken
            // for (int i = 0; i < chain_input.length; i++) {
            //     for (int j = 0; j < chain_input[i].length; j++) { //chain_input[i].length gibt Anzahl Spalten der Reihe i
            //             if (chain_input[i][j].equals("<")) { //stdin umleiten
            //                 System.out.println("schleife für < greift");
            //                 //TODO: Schließen der bisherigen Datei, neu öffnen der anderen mit gleicher fd Nummer
            //                 //index_out = j;
            //                 //  stdinUmlenken = true;
            //                 String prog2 = chain_input[i][j + 1]; //was nach < folgt soll geöffnet werden
            //                 // File inFile = new File(path2);
            //                 //dup2 = duplicate a file descriptor like int dup2(int oldfd, int newfd);
                            
            //                 stdin_old = dup2(fd_in, newfd); // backup origin stdin
            //                 // doku:
            //                 // int dup2(int oldfd, int newfd);
            //                // The dup2() system call performs the same task as
            //               // dup(), but instead of
            //               // using the lowest-numbered unused file descriptor, it
            //                // uses the descriptor number specified in newfd. If the
            //                // descriptor newfd was previously open, it is silently
            //                // closed before being reused.
            //                 close(fd_in);
            
            //                 open(prog2, O_RDONLY); //öffne neue Datei
            //             //    String[] buffer = new String[input.length - 2];
            //             //    for (int k = 0; k < buffer.length; k++) {
            //             //        buffer[k] = input[k];
            //             //    }
            //             //    input = buffer;
            //             //    break;
            //             } else if(chain_input[i][j].equals(">")) {
            //                 //index_in = j;
            //             }
            //         }
            // }
				// ende stdin umlenken

                    // String path3 = null;
                    // for (int i = 0; i < input.length; i++) {
                    //     if (input[i].equals(">")) {
                    //         stdoutUmlenken = true;
                    //         path3 = input[i + 1];
                    //         stdout_old = dup2(fd_out, 101); // back up original
                    //                                         // stdout
                    //         close(fd_out); // close original stdout
                    //         open(path3, O_WRONLY | O_CREAT); // open or create new
                    //                                             // stdout
                    //         String[] buffer2 = new String[input.length - 2];
                    //         for (int k = 0; k < buffer2.length; k++) {
                    //             buffer2[k] = input[k];
                    //         }
                    //         input = buffer2;
                    //     }
                    // }
                    // Ende stdout umlenken
            
            
            //Aufgabe 1.3
            // die vom System vorgegebenen Pfade werden später nach der auszuführenden Datei durchsucht
            String env = System.getenv("PATH");
            String[] path = env.split(":"); //systempfade
            String[] prog = new String[chain_input.length];
            File file;
            boolean[] valid = new boolean[chain_input.length];
            
            //System wird nach Dateiname durchsucht
            for(int j = 0; j < chain_input.length; j++) {
                for (int i = 0; i < path.length; i++) {
                    file = new File(path[i] + "/" + chain_input[j][0]); // z.B. bin/nano
                    if (file.isFile() && file.canExecute()) {
                        prog[j] = path[i] + "/" + chain_input[j][0];
                        //System.out.println(prog[j]);
                        valid[j] = true;
                        break;
                    } else {
                        valid[j] = false;
                    }
                }
            }
            
            //int retcode = 0;
            outerloop: {
                for(int v = 0; v < chain_input.length; v++) {
                    if (valid[v] == false) {
                        // System.err.println("ERROR: Command " + (v+1) + " has no valid program name.");
                        System.err.println("ERROR: Either no valid program name or command wasn't successfull.");
                        break;
                    } else {
                        //starte Kindprozess
                        System.out.println("Command " + (v+1) + " successfull, starting child process ...");
                        int child_pid = fork();
                        
                        if (child_pid == 0) {
                            if (umlenken(chain_input) != 0) {
                                System.out.println("Fehler beim Umlenken");
                            }
                        //    if (in != 0) {
                        //        dup2(in, fd_in);
                        //    }
                            // Der Kind-Prozess tauscht nun mittels einem Aufruf an execv() seinen Programmcode (den der Shell) gegen den Code aus einer Datei aus.
                            // Der Dateiname ist ein Parameter beim Aufruf von execv(), das außerdem die Wortliste der Kommandozeile erhält (incl. dem Programmnamen selbst)
                            // String rmv_stdin [] = chain_input[v];
                            int index = -1;
                            String[] new_arry = new String[chain_input[v].length - 1];
                            // if (Arrays.asList(chain_input[v]).contains("<")) {
                                
                            // }
                            // String rmv_stdin1 [] = new String [1];
                            // String rmv_stdin2 [] = new String [1];
                            for(int c=0; c<chain_input[v].length; c++) {
                                if (chain_input[v][c].equals("<")) {
                                    index = c;
                                } 
                            }
                            for (int i = 0, k = 0; i < chain_input[v].length; i++) {
                                if (i != index) {
                                  new_arry[k] = chain_input[v][i]; // copying elements to new array
                                  k++;
                                }
                              }

                            // System.out.println(Arrays.toString(new_arry));
                            //execv(prog[v], chain_input[v]); 
                            execv(prog[v], new_arry); 
                            // ersetzt den Kindprozess (shell) durch ein Programm, welchem weitere Parameter uebbergeben werden koennen

                        } else if (child_pid == -1) {
                            System.err.println("An error occurred.");
                            // Aufgabe 2: Wenn ein Programm der Verkettung fehlschlägt, sollen die weiteren Kommandos nicht ausgeführt werden
                            exit(1);
                            //retcode = -1;
                            System.out.println("Ein Programm ist fehlgeschlagen, ganze Kette abgebrochen.");
                            break outerloop; 
                            //error = true;

                        } else { // Elternprozess
        
                            //System.out.println("Command " + (v+1) + " was successfull.");
                            int[] status = new int[1];
                            //waitpid teilt Returncode dem Elternprozess mit
                            waitpid(child_pid, status, 0);
                        }

                    }   
                    }
                }
        }
    }   
       
    static int umlenken (String[][] chain_input) {
        //AUFGABE 3 a&b
            //stdin und stdout umlenken mit < (stdin), > (stdout) und | (pipes)
            // "Umlenken" ist der Fachjargon für: Schließen der bisherigen Datei, neu öffnen einer anderen unter derselben Filedeskriptor-Nummer.
            // Der Filedeskriptor, der von einem erfolgreichen Aufruf von [to open()] zurückgegeben wird, ist der Filedeskriptor mit der niedrigsten Nummer, der derzeit nicht für den Prozess geöffnet ist.
        int stdin_old = 0; //standard
        int stdout_old = 1; //standard
        int index_out=0;
        int index_in=0;
        boolean stdinUmlenken = false;
        boolean stdoutUmlenken = false;

        int newfd = 0;

        for (int i = 0; i < chain_input.length; i++) {
            for (int j = 0; j < chain_input[i].length; j++) { //chain_input[i].length gibt Anzahl Spalten der Reihe i
                    if (chain_input[i][j].equals("<")) { //stdin umleiten
                        // System.out.println("schleife für < greift");
                        stdinUmlenken = true;
                        //index_out = j;
                        String prog2 = chain_input[i][j + 1]; //was nach < folgt soll geöffnet werden
                        String after_stdin = chain_input[i][j + 1];
                        // System.out.println(prog2);
                        // System.out.println(after_stdin);
                        // File inFile = new File(path2);
                        //dup2 = duplicate a file descriptor like int dup2(int oldfd, int newfd);
                        
                        stdin_old = dup2(fd_in, newfd); // backup origin stdin
                        // doku:
                        // int dup2(int oldfd, int newfd);
                       // The dup2() system call performs the same task as
                      // dup(), but instead of
                      // using the lowest-numbered unused file descriptor, it
                       // uses the descriptor number specified in newfd. If the
                       // descriptor newfd was previously open, it is silently
                       // closed before being reused.
                        close(fd_in);
        
                        newfd = open(prog2, O_RDONLY); //öffne neue Datei
                        if (newfd < 0) {
                            return -1;
                        }
                    //    String[] buffer = new String[input.length - 2];
                    //    for (int k = 0; k < buffer.length; k++) {
                    //        buffer[k] = input[k];
                    //    }
                    //    input = buffer;
                    //    break;
                    } else if(chain_input[i][j].equals(">")) {
                        //index_in = j;
                    }
                }
        }
        return 0;
    }
    
}