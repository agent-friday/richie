package agent_friday.richie;

import java.math.BigDecimal;
import java.math.MathContext;

public class Loan {

  public static final int MONTHS_IN_YEAR = 12;

  private int term;
  private BigDecimal principal;
  private BigDecimal monthlyPmt;
  private BigDecimal annualInterest;

  /**
   * @return the loan term in months
   */
  public int getTerm() {
    return term;
  }

  /**
   * @param term the length of the loan term in months
   */
  public void setTerm(int term) {
    this.term = term;
  }

  public BigDecimal getPrincipal() {
    return principal;
  }

  public void setPrincipal(BigDecimal principal) {
    this.principal = principal;
  }

  public BigDecimal getMonthlyPmt() {
    return monthlyPmt;
  }

  public void setMonthlyPmt(BigDecimal monthlyPmt) {
    this.monthlyPmt = monthlyPmt;
  }

  public BigDecimal getAnnualInterest() {
    return annualInterest;
  }

  public void setAnnualInterest(BigDecimal annualInterest) {
    this.annualInterest = annualInterest;
  }

  /**
   * Calculates the what the monthly payment amount will be for the given loan amount, annual
   * interest rate, and term, using the following formula:
   * <p>
   * <pre>
   *       i(PV)(1 + i)^n
   * PMT = --------------
   *       (1 + i)^n - 1
   * </pre>
   * <p>
   * where,
   * <p>
   * PV = initial loan amount<br /> i = monthly interest rate (annual interest rate / 12)<br /> n =
   * loan term in months<br />
   *
   * @return the monthly payment
   */
  public BigDecimal calcMonthly() {
    final BigDecimal one = new BigDecimal(1);

    // i
    final BigDecimal monthlyInt = annualInterest.divide(new BigDecimal(MONTHS_IN_YEAR),
        MathContext.DECIMAL128);

    // 1 + i
    final BigDecimal monthlyIntAndOne = monthlyInt.add(one);

    // (1 + i)^n
    final BigDecimal ratePowN = monthlyIntAndOne.pow(term);

    setMonthlyPmt(monthlyInt.multiply(principal).multiply(ratePowN)
        .divide(ratePowN.subtract(one), MathContext.DECIMAL128));

    return this.monthlyPmt;
  }
}
