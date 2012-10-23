/* Soot - a Java Optimization Framework
 * Copyright (C) 2012 Michael Markert, Frank Hartmann
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 */

package soot.dex.instructions;

import org.jf.dexlib.Code.Instruction;
import org.jf.dexlib.Code.SingleRegisterInstruction;

import soot.Local;
import soot.dex.DexBody;
import soot.dex.IDalvikTyper;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.Jimple;
import soot.jimple.internal.JAssignStmt;

public class MonitorEnterInstruction extends DexlibAbstractInstruction {

    public MonitorEnterInstruction (Instruction instruction, int codeAdress) {
        super(instruction, codeAdress);
    }

    public void jimplify (DexBody body) {
        int reg = ((SingleRegisterInstruction) instruction).getRegisterA();
        Local object = body.getRegisterLocal(reg);
        EnterMonitorStmt s = Jimple.v().newEnterMonitorStmt(object);
        defineBlock(s);
        tagWithLineNumber(s);
        body.add(s);
        if (IDalvikTyper.ENABLE_DVKTYPER) {
          int op = (int)instruction.opcode.value;
          body.dalvikTyper.captureMonitor((JAssignStmt)s);
        }
    }
}
