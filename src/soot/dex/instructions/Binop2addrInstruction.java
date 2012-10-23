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
import org.jf.dexlib.Code.TwoRegisterInstruction;
import org.jf.dexlib.Code.Format.Instruction12x;

import soot.Local;
import soot.Value;
import soot.dex.DexBody;
import soot.dex.IDalvikTyper;
import soot.dex.tags.DoubleOpTag;
import soot.dex.tags.FloatOpTag;
import soot.dex.tags.IntOpTag;
import soot.dex.tags.LongOpTag;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.Jimple;
import soot.jimple.internal.JAssignStmt;

public class Binop2addrInstruction extends TaggedInstruction {

   
  
  
    public Binop2addrInstruction (Instruction instruction, int codeAdress) {
        super(instruction, codeAdress);
    }

    public void jimplify (DexBody body) {
        if(!(instruction instanceof Instruction12x))
            throw new IllegalArgumentException("Expected Instruction12x but got: "+instruction.getClass());

        Instruction12x binOp2AddrInstr = (Instruction12x)instruction;
        int dest = binOp2AddrInstr.getRegisterA();

        Local source1 = body.getRegisterLocal(binOp2AddrInstr.getRegisterA());
        Local source2 = body.getRegisterLocal(binOp2AddrInstr.getRegisterB());

        Value expr = getExpression(source1, source2);

        AssignStmt assign = Jimple.v().newAssignStmt(body.getRegisterLocal(dest), expr);
        assign.addTag(getTag());

        defineBlock(assign);
        tagWithLineNumber(assign);
        body.add(assign);
        if (IDalvikTyper.ENABLE_DVKTYPER) {
          int op = (int)instruction.opcode.value;
          if (!(op >= 0xb0 && op <= 0xcf)) {
            throw new RuntimeException ("wrong value of op: 0x"+ Integer.toHexString(op) +". should be between 0xb0 and 0xcf.");
          }
          BinopExpr bexpr = (BinopExpr)expr;
          JAssignStmt jassign = (JAssignStmt)assign;
          body.dalvikTyper.setType(bexpr.getOp1Box(), op1BinType[op-0xb0]);
          body.dalvikTyper.setType(bexpr.getOp2Box(), op2BinType[op-0xb0]);
          body.dalvikTyper.setType(jassign.leftBox, resBinType[op-0xb0]);
        }
    }

    private Value getExpression(Local source1, Local source2) {
        switch(instruction.opcode) {
        case ADD_LONG_2ADDR:
          setTag (new LongOpTag());
          return Jimple.v().newAddExpr(source1, source2);
        case ADD_FLOAT_2ADDR:
          setTag (new FloatOpTag());
          return Jimple.v().newAddExpr(source1, source2);
        case ADD_DOUBLE_2ADDR:
          setTag (new DoubleOpTag());
          return Jimple.v().newAddExpr(source1, source2);
        case ADD_INT_2ADDR:
          setTag (new IntOpTag());
          return Jimple.v().newAddExpr(source1, source2);

        case SUB_LONG_2ADDR:
          setTag (new LongOpTag());
          return Jimple.v().newSubExpr(source1, source2);
        case SUB_FLOAT_2ADDR:
          setTag (new FloatOpTag());
          return Jimple.v().newSubExpr(source1, source2);
        case SUB_DOUBLE_2ADDR:
          setTag (new DoubleOpTag());
          return Jimple.v().newSubExpr(source1, source2);
        case SUB_INT_2ADDR:
          setTag (new IntOpTag());
            return Jimple.v().newSubExpr(source1, source2);

        case MUL_LONG_2ADDR:
          setTag (new LongOpTag());
          return Jimple.v().newMulExpr(source1, source2);
        case MUL_FLOAT_2ADDR:
          setTag (new FloatOpTag());
          return Jimple.v().newMulExpr(source1, source2);
        case MUL_DOUBLE_2ADDR:
          setTag (new DoubleOpTag());
          return Jimple.v().newMulExpr(source1, source2);
        case MUL_INT_2ADDR:
          setTag (new IntOpTag());
            return Jimple.v().newMulExpr(source1, source2);

        case DIV_LONG_2ADDR:
          setTag (new LongOpTag());
          return Jimple.v().newDivExpr(source1, source2);
        case DIV_FLOAT_2ADDR:
          setTag (new FloatOpTag());
          return Jimple.v().newDivExpr(source1, source2);
        case DIV_DOUBLE_2ADDR:
          setTag (new DoubleOpTag());
          return Jimple.v().newDivExpr(source1, source2);
        case DIV_INT_2ADDR:
          setTag (new IntOpTag());
            return Jimple.v().newDivExpr(source1, source2);

        case REM_LONG_2ADDR:
          setTag (new LongOpTag());
          return Jimple.v().newRemExpr(source1, source2);
        case REM_FLOAT_2ADDR:
          setTag (new FloatOpTag());
          return Jimple.v().newRemExpr(source1, source2);
        case REM_DOUBLE_2ADDR:
          setTag (new DoubleOpTag());
          return Jimple.v().newRemExpr(source1, source2);
        case REM_INT_2ADDR:
          setTag (new IntOpTag());
            return Jimple.v().newRemExpr(source1, source2);

        case AND_LONG_2ADDR:
          setTag (new LongOpTag());
          return Jimple.v().newAndExpr(source1, source2);
        case AND_INT_2ADDR:
          setTag (new IntOpTag());
            return Jimple.v().newAndExpr(source1, source2);

        case OR_LONG_2ADDR:
          setTag (new LongOpTag());
          return Jimple.v().newOrExpr(source1, source2);
        case OR_INT_2ADDR:
          setTag (new IntOpTag());
            return Jimple.v().newOrExpr(source1, source2);

        case XOR_LONG_2ADDR:
          setTag (new LongOpTag());
          return Jimple.v().newXorExpr(source1, source2);
        case XOR_INT_2ADDR:
          setTag (new IntOpTag());
            return Jimple.v().newXorExpr(source1, source2);

        case SHL_LONG_2ADDR:
          setTag (new LongOpTag());
          return Jimple.v().newShlExpr(source1, source2);
        case SHL_INT_2ADDR:
          setTag (new IntOpTag());
            return Jimple.v().newShlExpr(source1, source2);

        case SHR_LONG_2ADDR:
          setTag (new LongOpTag());
          return Jimple.v().newShrExpr(source1, source2);
        case SHR_INT_2ADDR:
          setTag (new IntOpTag());
            return Jimple.v().newShrExpr(source1, source2);

        case USHR_LONG_2ADDR:
          setTag (new LongOpTag());
          return Jimple.v().newUshrExpr(source1, source2);
        case USHR_INT_2ADDR:
          setTag (new IntOpTag());
            return Jimple.v().newUshrExpr(source1, source2);

        default :
            throw new RuntimeException("Invalid Opcode: " + instruction.opcode);
        }
    }

    @Override
    boolean overridesRegister(int register) {
        TwoRegisterInstruction i = (TwoRegisterInstruction) instruction;
        int dest = i.getRegisterA();
        return register == dest;
    }


}
