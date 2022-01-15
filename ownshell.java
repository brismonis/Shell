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

    // pipe()  creates a pipe, a unidirectional data channel that can be used for interprocess communication.  The array pipefd is
    //    used to return two file descriptors referring to the ends of the pipe.  pipefd[0] refers to  the  read  end  of  the  pipe.
    //    pipefd[1]  refers  to the write end of the pipe.  Data written to the write end of the pipe is buffered by the kernel until
    //    it is read from the read end of the pipe.

    final static int stdin = 0;
	final static int stdout = 1;
    
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
            
            // TODO: pipes splitten 
            String[] pipe_input = br.readLine().split(" \\| ");
            int pipes = pipe_input.length; // pipe counter
            //System.out.println(pipes);
            // System.out.println(Arrays.toString(pipe_input));
            String[][] chain_input = new String[pipe_input.length][];
            // Erst die Verkettungen && splitten 
            for(int p=0; p < pipe_input.length; p++) {
                //chain_input[p] = pipe_input[p].split("\\s+&&\\s+");
                chain_input[p] = pipe_input[p].split(" && ");
                // chain_input[0] ist 1. pipe, chain_input[1] ist 2. pipe usw. (i < pipes)
                System.out.println(Arrays.toString(chain_input[p]) + p); 
            }
            //System.out.println(Arrays.toString(chain_input));

            // chain_input Matrix: 0        1
            //      0  [nano a] [nano b]
            //      1  [nano c]

            // Zeilen Spalten Tiefe -> Tiefe 0 ist 1. pipe usw.
            String[][][] final_input = new String[chain_input.length][][];
            for(int a=0; a < chain_input.length; a++) {
                final_input[a] = new String[chain_input[a].length][]; // Zeilen
                //System.out.println(Arrays.toString(final_input[a]) + a); 

                for(int b=0; b < chain_input[a].length; b++) {
                String arry[] = chain_input[a][b].split(" ");
                // System.out.println(arry[0]);
                // System.out.println(arry[1]);
                final_input[a][b] = arry;

                //final_input[a] = chain_input[a][b].split(" ");
                // System.out.println(final_input[0][0][0]); // ist nano 
                // System.out.println(final_input[0][0][1]); // ist a
                // das heißt: 1. pipe ist erste Stelle 0, 2. pipe ist erste Stelle 
                // [0][0][0] bis [0][0][chain_input[0][0].length] usw. also dritte Stelle

                }

            }

            // chain_input Matrix: 0        1       
            //      0  [nano a] [nano b]
            //      1  [nano c]

            //Aufgabe 2: Verkettung mit && ermöglichen
            // Matrix erstellen
            //String[] input = br.readLine().split("\\s+&&\\s+");
            //System.out.println(Arrays.toString(input));

            // System.out.print(Arrays.toString(input)); gibt [nano a, nano b, ..]
            //String[][][] final_input = new String[chain_input.length][][];

            for(int i = 0; i<chain_input.length; i++){
                for(int f = 0; f<chain_input[i].length; f++) {
                    
                    //final_input[i][f] = chain_input[i][f].split("\\s+");


                }

                //\\s+ will split your string on one or more spaces
                // gibt [nano, a][nano, b]
                //System.out.println(Arrays.toString(chain_input[i])); 
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

            // Aufgabe V1/3b Pipes

            // Die erste Pipe "|" verbindet stdout von "du" mit stdin des nächsten Programms, nämlich "grep".
            // die zu untersuchenden Dateien für grep sind dann stdin von der vorherigen Pipe 
            int[] pipefd = new int[2];// array für pipe()
            
            
            
            
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
                            //System.out.println("chain_input[v] ist:" + chain_input[v]);
                            int uml_ret = umlenken(chain_input[v]);
                            //System.out.println("Return von Umlenken ist:" + test);
                            if (uml_ret < 0) {
                                System.out.println("Fehler beim Umlenken.");
                                //break outerloop;
                            } else if(uml_ret > 0) {
                                int index = -1;
                                for(int c=0; c<chain_input[v].length; c++) {
                                    if (chain_input[v][c].equals("<")) {
                                        index = c;
                                        break;
                                    } else if(chain_input[v][c].equals(">")) {
                                        index = c;
                                        break;
                                    }
                                }
                                String[] new_arry = new String[index];

                                for (int i = 0, k = 0; i < index; i++) {
                                    //if (i != index) {
                                      new_arry[k] = chain_input[v][i]; // copying elements to new array
                                      k++;
                                    //}
                                }
                                // System.out.println("prog[v] ist: " + prog[v]);
                                // System.out.println("new_arry ist: " + Arrays.toString(new_arry));
                                execv(prog[v], new_arry); 
                            } else {
                                // System.out.println("prog[v] ist: " + prog[v]);
                                // System.out.println("chain_input[v] ist:" + Arrays.toString(chain_input[v]));
                                execv(prog[v], chain_input[v]); 
                            }
                        //    if (in != 0) {
                        //        dup2(in, fd_in);
                        //    }
                            // Der Kind-Prozess tauscht nun mittels einem Aufruf an execv() seinen Programmcode (den der Shell) gegen den Code aus einer Datei aus.
                            // Der Dateiname ist ein Parameter beim Aufruf von execv(), das außerdem die Wortliste der Kommandozeile erhält (incl. dem Programmnamen selbst)
                            // String rmv_stdin [] = chain_input[v];
                            
                            // if (Arrays.asList(chain_input[v]).contains("<")) {
                                
                            // }
                            // String rmv_stdin1 [] = new String [1];
                            // String rmv_stdin2 [] = new String [1];
                            

                            // System.out.println(Arrays.toString(new_arry));
                            //execv(prog[v], chain_input[v]); 
                            //execv(prog[v], new_arry); 
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
       
//AUFGABE V1/3a stdin und stdout umlenken
    static int umlenken (String[] chain_input_std) {
        //AUFGABE 3 a&b
            //stdin und stdout umlenken mit < (stdin), > (stdout) und | (pipes)
            // "Umlenken" ist der Fachjargon für: Schließen der bisherigen Datei, neu öffnen einer anderen unter derselben Filedeskriptor-Nummer.
            // Der Filedeskriptor, der von einem erfolgreichen Aufruf von [to open()] zurückgegeben wird, ist der Filedeskriptor mit der niedrigsten Nummer, der derzeit nicht für den Prozess geöffnet ist.
        int stdin_new = 0; //standard
        int stdout_new= 1; //standard
        // int index_out=0;
        // int index_in=0;
        boolean stdinUmlenken = false;
        boolean stdoutUmlenken = false;

        int newfd = 0;
        //System.out.println("chain_input_std ist:" + Arrays.toString(chain_input_std));

        for (int i = 0; i < chain_input_std.length; i++) {
            //System.out.println(chain_input_std[i]);
            //for (int j = 0; j < chain_input_std[i].length; j++) { //chain_input[i].length gibt Anzahl Spalten der Reihe i
                //System.out.println(chain_input_std[i][j]);

                    if (chain_input_std[i].equals("<")) { //stdin umleiten
                        // System.out.println("schleife für < greift");
                        stdinUmlenken = true;
                        //index_out = j;
                        //String prog_in = chain_input[i][j + 1]; 
                        String after_stdin = chain_input_std[i+1]; //was nach < folgt soll geöffnet werden
                        // System.out.println(prog2);
                        // System.out.println(after_stdin);
                        // File inFile = new File(path2);
                        //dup2 = duplicate a file descriptor like int dup2(int oldfd, int newfd);
                        
                        //stdin_new = dup2(stdin, newfd); // backup origin stdin
                        // doku:
                        // int dup2(int oldfd, int newfd);
                       // The dup2() system call performs the same task as
                      // dup(), but instead of
                      // using the lowest-numbered unused file descriptor, it
                       // uses the descriptor number specified in newfd. If the
                       // descriptor newfd was previously open, it is silently
                       // closed before being reused.
                        close(stdin);
        
                        newfd = open(after_stdin, O_RDONLY); //öffne neue Datei
                        if (newfd < 0) {
                            return -1;
                        } 
                    //    String[] buffer = new String[input.length - 2];
                    //    for (int k = 0; k < buffer.length; k++) {
                    //        buffer[k] = input[k];
                    //    }
                    //    input = buffer;
                    //    break;
                        // System.out.println("1");
                        // return 1;
                    } else if(chain_input_std[i].equals(">")) {
                        // System.out.println("schleife für > greift");
                        stdoutUmlenken = true;
                        String after_stdout = chain_input_std[i+1];
                        //stdout_new = dup2(stdout, newfd);
                        close(stdout);
                        newfd = open(after_stdout, O_WRONLY | O_CREAT);
                        if (newfd < 0) {
                            return -1;
                        } 
                        
                    } 
                    // else {
                    //     System.out.println("0 - no < or > detected");
                        
                    // }
                }
        //}
        if (stdinUmlenken == true || stdoutUmlenken == true) {
            // System.out.println("1");
            return 1;
        } else {
            return 0;
        }
        
    }
    

    // static int umlenken (String[][] chain_input_std) {
    //     //AUFGABE 3 a&b
    //         //stdin und stdout umlenken mit < (stdin), > (stdout) und | (pipes)
    //         // "Umlenken" ist der Fachjargon für: Schließen der bisherigen Datei, neu öffnen einer anderen unter derselben Filedeskriptor-Nummer.
    //         // Der Filedeskriptor, der von einem erfolgreichen Aufruf von [to open()] zurückgegeben wird, ist der Filedeskriptor mit der niedrigsten Nummer, der derzeit nicht für den Prozess geöffnet ist.
    //     int stdin_new = 0; //standard
    //     int stdout_new= 1; //standard
    //     int index_out=0;
    //     int index_in=0;
    //     boolean stdinUmlenken = false;
    //     boolean stdoutUmlenken = false;

    //     int newfd = 0;

    //     for (int i = 0; i < chain_input_std.length; i++) {
    //         System.out.println(chain_input_std[i]);
    //         for (int j = 0; j < chain_input_std[i].length; j++) { //chain_input[i].length gibt Anzahl Spalten der Reihe i
    //             System.out.println(chain_input_std[i][j]);

    //                 if (chain_input_std[i][j].equals("<")) { //stdin umleiten
    //                     System.out.println("schleife für < greift");
    //                     stdinUmlenken = true;
    //                     //index_out = j;
    //                     //String prog_in = chain_input[i][j + 1]; 
    //                     String after_stdin = chain_input_std[i][j + 1]; //was nach < folgt soll geöffnet werden
    //                     // System.out.println(prog2);
    //                     // System.out.println(after_stdin);
    //                     // File inFile = new File(path2);
    //                     //dup2 = duplicate a file descriptor like int dup2(int oldfd, int newfd);
                        
    //                     stdin_new = dup2(stdin, newfd); // backup origin stdin
    //                     // doku:
    //                     // int dup2(int oldfd, int newfd);
    //                    // The dup2() system call performs the same task as
    //                   // dup(), but instead of
    //                   // using the lowest-numbered unused file descriptor, it
    //                    // uses the descriptor number specified in newfd. If the
    //                    // descriptor newfd was previously open, it is silently
    //                    // closed before being reused.
    //                     close(stdin);
        
    //                     newfd = open(after_stdin, O_RDONLY); //öffne neue Datei
    //                     if (newfd < 0) {
    //                         return -1;
    //                     } else {
    //                         System.out.println("1");
    //                         return 1;
    //                     }
    //                 //    String[] buffer = new String[input.length - 2];
    //                 //    for (int k = 0; k < buffer.length; k++) {
    //                 //        buffer[k] = input[k];
    //                 //    }
    //                 //    input = buffer;
    //                 //    break;
    //                     // System.out.println("1");
    //                     // return 1;
    //                 } else if(chain_input_std[i][j].equals(">")) {
    //                     stdoutUmlenken = true;
    //                     String after_stdout = chain_input_std[i][j + 1];
    //                     stdout_new = dup2(stdout, newfd);
    //                     close(stdout);
    //                     newfd = open(after_stdout, O_WRONLY | O_CREAT);
    //                     if (newfd < 0) {
    //                         return -1;
    //                     } else {
    //                         System.out.println("1");
    //                         return 1;
    //                     }
                        
    //                 } else {
    //                     System.out.println("0 - no < or > detected");
    //                     return 0;
    //                 }
    //             }
    //     }
    //     System.out.println("1");
    //     return 1;
    // }
    
}