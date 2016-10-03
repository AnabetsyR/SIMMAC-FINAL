package com.example.java;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;


public class Main {


    //Main memory to store data and instructions
    private static int [] mainMemory = new int[512];
    public static void main(String[] args) {

        System.out.println("Welcome to SIMMAC!");

        mainMemory[201] = 100;
        
        mainMemory[301] = 20;
        
        mainMemory[303] = 100;
        mainMemory[304] = 1;

        mainMemory[400] = 10;
        mainMemory[401] = 5;
        mainMemory[402] = 2;
        mainMemory[403] = 2;
        mainMemory[404] = 3;
        mainMemory[405] = 7;
        mainMemory[406] = 8;
        mainMemory[407] = 2;
        mainMemory[408] = 2;
        mainMemory[409] = 19;
        mainMemory[410] = 1;
        mainMemory[411] = 1;
        mainMemory[412] = 3;
        mainMemory[413] = 9;
        mainMemory[414] = 20;
        mainMemory[415] = 1;
        mainMemory[416] = 6;
        mainMemory[417] = 25;
        mainMemory[418] = 4;
        mainMemory[419] = 8;
        mainMemory[420] = 15;
        mainMemory[421] = 20;
        mainMemory[422] = 10;
        mainMemory[423] = 30;
        mainMemory[424] = 12;
        mainMemory[425] = 9;
        mainMemory[426] = 6;
        mainMemory[427] = 21;
        mainMemory[428] = 25;
        mainMemory[429] = 1;
        mainMemory[430] = 50;
        mainMemory[431] = 10;
        mainMemory[432] = 5;
        mainMemory[433] = 8;
        mainMemory[434] = 4;
        mainMemory[435] = 1;
        mainMemory[436] = 11;
        mainMemory[437] = 23;
        mainMemory[438] = 4;
        mainMemory[439] = 17;
        mainMemory[440] = 35;
        
        
        //Gets the quantum number from user
        Scanner reader = new Scanner(System.in);
        System.out.println("Please, enter the quantum number: ");
        int quantum = reader.nextInt();
        System.out.println("Quantum: " + quantum + "\n");
        
        String filename = "testing.txt";
        BufferedReader process = null;
        
        
        //Tracker holds the start and end points of each process stored in main memory
        int[] tracker;
        int start = 0;
        int end = 0;
        try {
            String sCurrentLine;
            
            process = new BufferedReader(new FileReader(filename));
            
            tracker = new int[10];
            int processNumber = 0;
            int counter = 3;
            start = counter;
            int counter1 = 3;
            while ((sCurrentLine = process.readLine()) != null) {
                String a[] = sCurrentLine.split(" ");
                tracker[processNumber] = start +3;
                
                while (counter1 < a.length + counter) {
                    mainMemory[counter1] = Integer.parseInt(a[counter1 - counter], 16);
                    counter1++;
                }
                
                end = counter1;
                tracker[processNumber + 1] = end;
                counter = counter1;
                start = counter;
                processNumber = processNumber + 2;
            }


            //The following lines add each job to the job queue and run round robin
            RoundRobin rr = new RoundRobin();
            Job job0 = new Job(tracker[0], tracker[1]);
            rr.add(job0);
            System.out.println("Adding Job 0: " + job0);
            
            Job job1 = new Job(tracker[2], tracker[3]);
            rr.add(job1);
            System.out.println("Adding Job 1: " + job1);
            
            Job job2 = new Job(tracker[4], tracker[5]);
            rr.add(job2);
            System.out.println("Adding Job 2: " + job2);

            Job job3 = new Job(tracker[6], tracker[7]);
            rr.add(job3);
            System.out.println("Adding Job 3: " + job3);

            Job job4 = new Job(tracker[8], tracker[9]);
            rr.add(job4);
            System.out.println("Adding Job 4: " + job4);
            System.out.println(rr);
            rr.run(quantum, tracker); // Most important line!
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (process != null) process.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    //Execute method where all the opcodes are processed
    static int[] execute(int start, int end, int state[], int quantum, int processNum) {

        //Sets the state for each register. This state is used later in the process control block
        int ACC = state[0];
        int SAR = state[1];
        int SDR = state[2];
        int PSIAR = state[3];
        int TMPR = state[4];
        int HALT = 0;
        {

            
            for (PSIAR = start; PSIAR < end; PSIAR++) {
            	if(quantum == 0) {
            		//System.out.println("quantum: " +quantum); // Remove comment to see quantum iterations
            		break;
            	}
            	else {
            		//System.out.println("quantum: " +quantum); // Remove comment to see quantum iterations
            		quantum--;
            	}
                //Each instruction is decoded and divided into opcode and operand
                int high = mainMemory[PSIAR] >> 16;
                int low = mainMemory[PSIAR] & 0xFFFF;

                //Gets opcode from instruction
                int opcode = high;

                //Gets address for instruction
                SAR = low;
                
                if (opcode == 10) {
                    //Instruction is ADD
                    ACC = ACC + Integer.valueOf(mainMemory[SAR]);
                    TMPR = ACC;
                    ACC = PSIAR;
                    PSIAR = ACC;
                    ACC = TMPR;
                    SDR = Integer.valueOf(mainMemory[SAR + 1]);
                    TMPR = SDR;
                } else if (opcode == 20) {
                    //Instruction is SUB
                    ACC -= Integer.valueOf(mainMemory[SAR]);
                    TMPR = ACC;
                    ACC = PSIAR;
                    PSIAR = ACC;
                    ACC = TMPR;
                    SDR = Integer.valueOf(mainMemory[SAR + 1]);
                    TMPR = SDR;
                } else if (opcode == 30) {
                    //Instruction is LOAD
                    ACC = Integer.valueOf(mainMemory[SAR]);
                    TMPR = ACC;
                    ACC = PSIAR;
                    PSIAR = ACC;
                    ACC = TMPR;
                    SDR = Integer.valueOf(mainMemory[SAR]);
                    ACC = SDR;
                } else if (opcode == 70) {
                    //Instruction is LOAD IMMEDIATE.
                    ACC = ACC + Integer.valueOf(SAR);
                    TMPR = ACC;
                    ACC = PSIAR + 1;
                    PSIAR = ACC;
                    ACC = TMPR;
                    SDR = Integer.valueOf(mainMemory[SAR + 1]);
                    ACC = SDR;
                } else if (opcode == 40) {
                    //Instruction is STORE
                    TMPR = ACC;
                    ACC = PSIAR;
                    PSIAR = ACC;
                    ACC = TMPR;
                    SDR = ACC;
                    mainMemory[SAR] = SDR;//WRITE
                } else if (opcode == 50) {
                    //Instruction is BRANCH
                    SDR = Integer.valueOf(mainMemory[SAR]);
                    PSIAR = SDR;
                } else if (opcode == 5) {
                    //Instruction is a HALT
                    System.out.println("***End of Process " + processNum  + " ***");
                    HALT = 1;
                    System.out.println("ACC: " + ACC);
                    System.out.println("TMPR: " + TMPR);
                    System.out.println("SAR: " + SAR);
                    System.out.println("SDR: " + SDR);
                    System.out.println("PSIAR: " + PSIAR + "\n\n");
                } else if (opcode == 60) {
                    //Instruction is CONDITIONAL BRANCH
                    if (ACC == 0) {
                    	;
                    } else {
                        PSIAR = SAR - 1;
                    }
                }
            }
        }
        return new int[]{
                ACC,
                SAR,
                SDR,
                PSIAR,
                TMPR,
                HALT
        };
    }
}
