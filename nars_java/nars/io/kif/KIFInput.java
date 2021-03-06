/*
 * Copyright (C) 2014 me
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package nars.io.kif;

import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import nars.core.NAR;
import nars.io.Input;
import nars.io.TextInput;

/**
 * http://sigmakee.cvs.sourceforge.net/viewvc/sigmakee/sigma/suo-kif.pdf
 * http://sigma-01.cim3.net:8080/sigma/Browse.jsp?kb=SUMO&lang=EnglishLanguage&flang=SUO-KIF&term=subclass
 * @author me
 */
public class KIFInput implements Input {

    private final KIF kif;
    private final Iterator<Formula> formulaIterator;
    boolean closed = false;
    private final NAR nar;

    public KIFInput(NAR n, String kifPath) throws Exception {
        kif = new KIF(kifPath);
        formulaIterator = kif.getFormulas().iterator();

        n.addInputChannel(this);
        
        this.nar = n;
                
    }

    Map<String, Integer> knownOperators = new HashMap();
    Map<String, Integer> unknownOperators = new HashMap();
    
    protected void emit(final String statement) {
        new TextInput(nar, statement);
        //System.out.println(statement);
    }
    
    @Override
    public boolean nextInput() {
        if (!formulaIterator.hasNext()) {
            closed = true;
            return false;
        }
        
        Formula f = formulaIterator.next();
        if (f == null) {
            closed = true;
            return false;
        }

        String root = f.car(); //root operator
        
        List<String> a = f.argumentsToArrayList(1);
        
        if (root.equals("subclass")) {            
            if (a.size()!=2) {
                System.err.println("subclass expects 2 arguments");
            }
            else {
                emit("<" + a.get(0) + " --> " + a.get(1) + ">.");
 
            }
        } else if (root.equals("instance")) {
            if (a.size()!=2) {
                System.err.println("instance expects 2 arguments");
            }    
            else {
                emit("<" + a.get(0) + " {-- " + a.get(1) + ">.");
            }
        }
        else if (root.equals("relatedInternalConcept")) {
            /*(documentation relatedInternalConcept EnglishLanguage "Means that the two arguments are related concepts within the SUMO, i.e. there is a significant similarity of meaning between them. To indicate a meaning relation between a SUMO concept and a concept from another source, use the Predicate relatedExternalConcept.")            */

            if (a.size()!=2) {
                System.err.println("relatedInternalConcept expects 2 arguments");
            }    
            else {
                emit("<" + a.get(0) + " <-> " + a.get(1) + ">.");
            }            
        }
        /*else if (root.equals("domain")) {
            
            //(domain domain 1 Relation)
            //(domain domain 2 PositiveInteger)
            //(domain domain 3 SetOrClass)            
                        
            if (a == null) {
                
            }
            else if (a.size() != 3) {
                System.err.println("domain expects 3 arguments, got: " + a);
            }
            else {
                new TextInput(nar, "<" + a.get(2) + " --] " + a.get(0) + ">.");
            }
        }
        else if (root.equals("range")) {
            
            //for now, consider Range as the function inheriting from the result type
            //there may be other ways of expressing this more clearly
            new TextInput(nar, "<" + a.get(0) + " --> " + a.get(1) + ">.");
        }
        */
        else if (root.equals("disjoint")) {
             //"(||," <term> {","<term>} ")"      // disjunction
            emit("<(||," + a.get(0) + ","+ a.get(1) +")>.");
        }
        else if (root.equals("disjointRelation")) {            
            emit("<(||," + a.get(0) + ","+ a.get(1) +")>.");            
        }
        else if (root.equals("subrelation")) {            
            //for now, use similarity+inheritance but more clear expression is possible
            emit("<" + a.get(0) + " <-> " + a.get(1) + ">.");
            emit("<" + a.get(0) + " --> " + a.get(1) + ">.");
        }
        else {
            /*System.out.println("??" + f);
            System.out.println();*/
            if (unknownOperators.containsKey(root))
                unknownOperators.put(root, unknownOperators.get(root)+1);
            else
                unknownOperators.put(root, 1);
            return true;
        }
        
        if (knownOperators.containsKey(root))
            knownOperators.put(root, knownOperators.get(root)+1);
        else
            knownOperators.put(root, 1);

        
        //  => Implies
        //  <=> Equivalance
        
/*Unknown operators: {=>=466, rangeSubclass=5, inverse=1, relatedInternalConcept=7, documentation=128, range=29, exhaustiveAttribute=1, trichotomizingOn=4, subrelation=22, not=2, partition=12, contraryAttribute=1, subAttribute=2, disjoint=5, domain=102, disjointDecomposition=2, domainSubclass=9, <=>=70}*/

        return true;
    }

    public Map<String, Integer> getKnownOperators() {
        return knownOperators;
    }

    
    public Map<String,Integer> getUnknownOperators() {
        return unknownOperators;
    }
    

    @Override
    public boolean isClosed() {
        return closed;
    }

}
