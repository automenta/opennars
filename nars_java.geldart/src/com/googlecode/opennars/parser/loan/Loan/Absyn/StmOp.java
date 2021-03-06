package com.googlecode.opennars.parser.loan.Loan.Absyn; // Java Package generated by the BNF Converter.

public class StmOp extends Stm {
  public final Term term_;
  public final ListTerm listterm_;

  public StmOp(Term p1, ListTerm p2) { term_ = p1; listterm_ = p2; }

  public <R,A> R accept(com.googlecode.opennars.parser.loan.Loan.Absyn.Stm.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o instanceof com.googlecode.opennars.parser.loan.Loan.Absyn.StmOp) {
      com.googlecode.opennars.parser.loan.Loan.Absyn.StmOp x = (com.googlecode.opennars.parser.loan.Loan.Absyn.StmOp)o;
      return this.term_.equals(x.term_) && this.listterm_.equals(x.listterm_);
    }
    return false;
  }

  public int hashCode() {
    return 37*(this.term_.hashCode())+this.listterm_.hashCode();
  }


}
