package com.example.java;

import java.util.LinkedList;
import java.util.Queue;

import static com.example.java.Main.execute;

public class RoundRobin {

	//Job queue for all jobs/processes
    private Queue<Job> scheduler = new LinkedList<Job>();

    @Override
    public String toString() {
    	String output = "*************JOB QUEUE**************\n";
    	int x = 0;
    	for (Job element : scheduler) {
    		  output += String.format("* Process: %-1d | %-19s *\n", x,element.toString());
    		  x++;
    		}
    	output += "************************************";
        return output;
    }

	//Add method for adding all jobs/processes to scheduler
    public void add(Job job) {
        this.scheduler.add(job);
    }

	//Run method to run each job/process for a given quantum number.
    public void run(int quantum, int tracker[]) {

		//Array y holds the state of each register so round robin can pick up in the same location on the second round
    	int y[] = {0, 0, 0, 0, 0, 0};
		//Two-dimensional array pcb holds the status array y for each process. pcb[x][5] != 1 means that the process has not encountered a HALT instruction yet
    	int[] pcb[] = {y,y,y,y,y};
    	while( (pcb[0][5] != 1) || (pcb[1][5] != 1) || (pcb[2][5] != 1) || (pcb[3][5] != 1) || (pcb[4][5] != 1) ) {
    		
    		int processNum = 0;
	        for (int x = 0; x < 10; x+=2) {
        		if(pcb[processNum][5] == 1)
        			;
        		else if( (tracker[x] != tracker[x+1] && pcb[processNum][3] == tracker[x]) || pcb[processNum][3] == 0)
        			pcb[processNum] = execute(tracker[x], tracker[x + 1], pcb[processNum],quantum,processNum);
        		else if(tracker[x] != tracker[x+1] && pcb[processNum][3] != tracker[x] && pcb[processNum][3] !=0)
        			pcb[processNum] = execute(pcb[processNum][3], tracker[x + 1], pcb[processNum],quantum,processNum);
             	if(pcb[processNum][5] == 1) {
             		tracker[x] = 0;
             		tracker[x+1] = 0;
             	}
             	if(processNum == this.scheduler.size() - 1 && ((pcb[0][5] != 1) || (pcb[1][5] != 1) || (pcb[2][5] != 1) || (pcb[3][5] != 1) || (pcb[4][5] != 1)))
             		System.out.println("Done with Process: " + processNum + "\nContinuing with process: " + 0 );
             	else if(processNum == this.scheduler.size() - 1 && (pcb[0][5] != 1) || (pcb[1][5] != 1) || (pcb[2][5] != 1) || (pcb[3][5] != 1) || (pcb[4][5] != 1))
             		System.out.println("Done with Process: " + processNum + "\nContinuing with process: " + (processNum+1) );
             	else if( !((pcb[0][5] != 1) || (pcb[1][5] != 1) || (pcb[2][5] != 1) || (pcb[3][5] != 1) || (pcb[4][5] != 1)) )
             		System.out.println("Done with ALL Processes!");
             	
	            processNum++;
	                     
	        }
    	}
    }
}

