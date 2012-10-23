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
import org.jf.dexlib.Code.ThreeRegisterInstruction;
import org.jf.dexlib.Code.Format.Instruction23x;

import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.Local;
import soot.LongType;
import soot.Type;
import soot.dex.DexBody;
import soot.dex.IDalvikTyper;
import soot.dex.tags.DoubleOpTag;
import soot.dex.tags.FloatOpTag;
import soot.dex.tags.LongOpTag;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.Expr;
import soot.jimple.Jimple;
import soot.jimple.internal.JAssignStmt;

public class CmpInstruction extends TaggedInstruction {

    public CmpInstruction (Instruction instruction, int codeAdress) {
        super(instruction, codeAdress);
    }

    public void jimplify (DexBody body) {
        if(!(instruction instanceof Instruction23x))
            throw new IllegalArgumentException("Expected Instruction23x but got: "+instruction.getClass());

        Instruction23x cmpInstr = (Instruction23x)instruction;
        int dest = cmpInstr.getRegisterA();

        Local first = body.getRegisterLocal(cmpInstr.getRegisterB());
        Local second = body.getRegisterLocal(cmpInstr.getRegisterC());

        Expr cmpExpr;
        Type type = null;
        switch (instruction.opcode) {
        case CMPL_DOUBLE:
          setTag (new DoubleOpTag());
          type = DoubleType.v();
          cmpExpr = Jimple.v().newCmplExpr(first, second);
          break;
        case CMPL_FLOAT:
          setTag (new FloatOpTag());
          type = FloatType.v();
            cmpExpr = Jimple.v().newCmplExpr(first, second);
            break;
        case CMPG_DOUBLE:
          setTag (new DoubleOpTag());
          type = DoubleType.v();
          cmpExpr = Jimple.v().newCmpgExpr(first, second);
          break;
        case CMPG_FLOAT:
          setTag (new FloatOpTag());
          type = FloatType.v();
            cmpExpr = Jimple.v().newCmpgExpr(first, second);
            break;
        case CMP_LONG:
          setTag (new LongOpTag());
          type = LongType.v();
          cmpExpr = Jimple.v().newCmpExpr(first, second);
          break;
        default:
            System.out.println ("no opcode for CMP: 0x"+ Integer.toHexString(instruction.opcode.value));
            System.exit(-1);
            cmpExpr = Jimple.v().newCmpExpr(first, second);
        }

        AssignStmt assign = Jimple.v().newAssignStmt(body.getRegisterLocal(dest), cmpExpr);
        assign.addTag(getTag());

        defineBlock(assign);
        tagWithLineNumber(assign);
        body.add(assign);
        if (IDalvikTyper.ENABLE_DVKTYPER) {
          getTag().getName();
          BinopExpr bexpr = (BinopExpr)cmpExpr;
          body.dalvikTyper.setType(bexpr.getOp1Box(), type);
          body.dalvikTyper.setType(bexpr.getOp2Box(), type);
          body.dalvikTyper.setType(((JAssignStmt)assign).leftBox, IntType.v());
        }
    }

    @Override
    boolean overridesRegister(int register) {
        ThreeRegisterInstruction i = (ThreeRegisterInstruction) instruction;
        int dest = i.getRegisterA();
        return register == dest;
    }

}
