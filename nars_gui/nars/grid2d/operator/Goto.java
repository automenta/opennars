/*
 * Sample.java
 *
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
package nars.grid2d.operator;

import java.util.ArrayList;

import nars.entity.Task;
import nars.grid2d.Grid2DSpace;
import nars.language.Term;
import nars.operator.Operation;
import nars.operator.Operator;

/**
 *  A class used as a template for Operator definition.
 * TODO: memory.registerOperator(new Goto("^goto"));
 */
public class Goto extends Operator {

    public Goto(Grid2DSpace space, String name) {
        super(name);
    }

    /** called from Operator */
    public ArrayList<Task> execute(Task task) {
        Operation content = (Operation) task.getContent();
        Operator op = content.getOperator();
         
        Term[] arg = content.getArguments();
        System.out.println("Executed: " + op);
        for (Term t : arg) {
            System.out.println(" --- " + t);
        }
        
        if(nars.grid2d.Grid2DSpace.world_used) {
            //ok lets start pathfinding tool
            //nars.grid2d.Grid2DSpace.pathFindAndGoto(arg);
        }
        
        return null;
    }

}
