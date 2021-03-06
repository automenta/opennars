package com.googlecode.opennars.parser.loan.Loan.Absyn; // Java Package generated by the BNF Converter.

public class TrmStm extends Term {
  public final Stm stm_;

  public TrmStm(Stm p1) { stm_ = p1; }

  public <R,A> R accept(com.googlecode.opennars.parser.loan.Loan.Absyn.Term.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o instanceof com.googlecode.opennars.parser.loan.Loan.Absyn.TrmStm) {
      com.googlecode.opennars.parser.loan.Loan.Absyn.TrmStm x = (com.googlecode.opennars.parser.loan.Loan.Absyn.TrmStm)o;
      return this.stm_.equals(x.stm_);
    }
    return false;
  }

  public int hashCode() {
    return this.stm_.hashCode();
  }


}
