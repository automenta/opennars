/*
 * Copyright (C) 2008  Pei Wang
 *
 * This file is part of Open-NARS.
 *
 * Open-NARS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * Open-NARS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Open-NARS.  If not, see <http://www.gnu.org/licenses/>.
 */
package nars.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import nars.io.TextInput;
import nars.io.TextOutput;

;

/**
 * Run Reasoner
 * <p>
 * Runs a NAR with input. useful for command line or batch functionality; 
 * TODO check duplicated code with {@link nars.main.NARS}
 * <p>
 * Manage the internal working thread. Communicate with Reasoner only.
 */
public class NARRun {

    /**
     * The nar
     */
    NAR nar;
    private boolean logging;
    private PrintStream out = System.out;
    private boolean dumpLastState = true;
    int maxTime = 0;
    /**
     * Flag to distinguish the two running modes of the project.
     */
    private static boolean standAlone = false;

    /**
     * The entry point of the standalone application.
     * <p>
     * Create an instance of the class, then run the {@link #init(String[])} and
     * {@link #run()} methods.
     *
     * @param args optional argument used : one input file
     */
    public static void main(String args[]) {
        NARRun nars = new NARRun();
        CommandLineArguments.decode(args, nars.getReasoner());
        nars.runInference(args);
        // TODO only if single run ( no reset in between )
        if (nars.dumpLastState) {
            System.out.println("\n==== Dump Last State ====\n"
                    + nars.nar.toString());
        }
    }

    public NARRun() {
        init();
    }

    /**
     * non-static equivalent to {@link #main(String[])} : run to completion from
     * an input file
     */
    public void runInference(String args[]) {
        init(args);
        run();
    }

    /**
     * initialize from an input file
     */
    public void init(String[] args) {
        if (args.length > 0) {
            try {
                TextInput fileInput = new TextInput(nar, new File(args[0]));
            } catch (FileNotFoundException ex) {
                System.err.println("NARRun.init: " + ex);
            }
        }
        else {
            new TextInput(nar, new BufferedReader(new InputStreamReader(System.in)));
        }
        new TextOutput(nar, new PrintWriter(out, true));
    }

    /**
     * non-static equivalent to {@link #main(String[])} : run to completion from
     * a BufferedReader
     */
    public void runInference(BufferedReader r, BufferedWriter w) {
        init(r, w);
        run();
    }

    private void init(BufferedReader r, BufferedWriter w) {
        TextInput experienceReader = new TextInput(nar, r);
        nar.addOutputChannel(new TextOutput(nar,
                new PrintWriter(w, true)));
    }

    /**
     * Initialize the system at the control center.<p>
     * Can instantiate multiple reasoners
     */
    public final void init() {
        nar = new NAR();
    }

    /**
     * Run to completion: repeatedly execute NARS working cycle, until Inputs
     * are Finished, or 1000 steps. This method is called when the Runnable's
     * thread is started.
     */
    public void run() {
        while (true) {
            log("NARSBatch.run():"
                    + " step " + nar.getTime()
                    + " " + nar.isFinishedInputs());
            
            try {
                nar.tick();
            }
            catch (Exception e) {
                System.err.println(e);
            }
            
            log("NARSBatch.run(): after tick"
                    + " step " + nar.getTime()
                    + " " + nar.isFinishedInputs());
            
            if (maxTime > 0) {
                if (nar.isFinishedInputs()
                        || nar.getTime() == maxTime) {
                    break;
                }
            }
        }
    }

    public void setPrintStream(PrintStream out) {
        this.out = out;
    }

    private void log(String mess) {
        if (logging) {
            System.out.println("/ " + mess);
        }
    }

    public NAR getReasoner() {
        return nar;
    }
    

//    /**
//     * Whether the project running as an application.
//     *
//     * @return true for application; false for applet.
//     */
//    public static boolean isStandAlone() {
//        return standAlone;
//    }
//
//    public static void setStandAlone(boolean standAlone) {
//        NARRun.standAlone = standAlone;
//    }
}
