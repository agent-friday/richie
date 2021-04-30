package agent_friday.richie;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Class to represent a loan and perform a couple of calculations based on a provided values.
 * <p>
 * Formulas used were taken from
 * <a href="https://www.calculatorsoup.com/calculators/financial/loan-calculator.php">
 * https://www.calculatorsoup.com/calculators/financial/loan-calculator.php</a>
 */
public class Loan {

  public static final int MONTHS_IN_YEAR = 12;

  private int term; // in months
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
    this.term = Math.abs(term);
  }

  /**
   * @return the initial loan amount
   */
  public BigDecimal getPrincipal() {
    return principal;
  }

  /**
   * @param principal the initial loan amount
   */
  public void setPrincipal(BigDecimal principal) {
    this.principal = principal;
  }

  /**
   * @return the monthly repayment amount
   */
  public BigDecimal getMonthlyPmt() {
    return monthlyPmt;
  }

  /**
   * @param monthlyPmt the monthly repayment amount
   */
  public void setMonthlyPmt(BigDecimal monthlyPmt) {
    this.monthlyPmt = monthlyPmt;
  }

  /**
   * @return the annual interest rate
   */
  public BigDecimal getAnnualInterest() {
    return annualInterest;
  }

  /**
   * @param annualInterest the annual interest rate
   */
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
    final BigDecimal monthlyInt = getAnnualInterest().divide(new BigDecimal(MONTHS_IN_YEAR),
        MathContext.DECIMAL128);

    // 1 + i
    final BigDecimal monthlyIntAndOne = monthlyInt.add(one);

    // (1 + i)^n
    final BigDecimal ratePowN = monthlyIntAndOne.pow(getTerm());

    setMonthlyPmt(monthlyInt.multiply(getPrincipal()).multiply(ratePowN)
        .divide(ratePowN.subtract(one), MathContext.DECIMAL128));

    return getMonthlyPmt();
  }

  /**
   * Calculates the available loan amount given the desired monthly payment, loan term, and annual
   * interest rate, using the following formula:
   * <p>
   * <pre>
   *            __             __
   *       PMT  |         1     |
   * PV = ----- | 1 - --------- |
   *        i   |     (1 + i)^n |
   *            __             __
   * </pre>
   * <p>
   * where,
   * <p>
   * PMT = desired monthly payment<br /> i = monthly interest rate (annual interest rate / 12)<br />
   * n = loan term in months<br />
   *
   * @return the loan amount that is possible
   */
  public BigDecimal calcLoan() {
    final BigDecimal one = new BigDecimal(1);

    // i
    final BigDecimal monthlyInt = getAnnualInterest().divide(new BigDecimal(MONTHS_IN_YEAR),
        MathContext.DECIMAL128);

    // 1 + i
    final BigDecimal monthlyIntAndOne = monthlyInt.add(one);

    // (1 + i)^n
    final BigDecimal ratePowN = monthlyIntAndOne.pow(getTerm());

    setPrincipal(getMonthlyPmt().divide(monthlyInt, MathContext.DECIMAL128)
        .multiply(one.subtract(one.divide(ratePowN, MathContext.DECIMAL128))));

    return getPrincipal();
  }

  /**
   * Calculates the loan term given the initial loan amount, the annual interest rate, and the
   * monthly payment, using the following formula
   * <p>
   * <pre>
   *        __             __
   *        |      1        |
   *        | ------------- |
   *     ln |      i(PV)    |
   *        | 1 - -------   |
   *        |       PMT     |
   *        __             __
   * n = -----------------------
   *          ln(1 + i)
   * </pre>
   * <p>
   * where,
   * <p>
   * PV = the initial loan principal<br />PMT = the monthly payment<br /> i = monthly interest rate
   * (annual interest rate / 12)<br />
   *
   * @return the length of the loan in months
   */
  public int calcTerm() {
    final BigDecimal one = new BigDecimal(1);

    // i
    final BigDecimal monthlyInt = getAnnualInterest().divide(new BigDecimal(MONTHS_IN_YEAR),
        MathContext.DECIMAL128);
    // 1 + i
    final BigDecimal monthlyIntAndOne = monthlyInt.add(one);

    final BigDecimal natLog = BigDecimal.valueOf(Math.log(monthlyIntAndOne.doubleValue()));

    final BigDecimal numerator = BigDecimal.valueOf(Math.log(one.subtract(
        monthlyInt.multiply(getPrincipal()).divide(getMonthlyPmt(), MathContext.DECIMAL128))
        .doubleValue()));

    setTerm(numerator.divide(natLog, MathContext.DECIMAL128).intValue());
    return getTerm();
  }
}
