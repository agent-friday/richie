package agent_friday.richie;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * A {@code Loan} class designed to specifically deal with some extras fields that are commonly
 * associated with mortgages and property ownership
 */
public class Mortgage extends Loan {

  /* Monthly loan payment plus HOA Fees and one month's portion of property tax */
  private BigDecimal overallMonthly;
  private BigDecimal hoaDues;
  private BigDecimal propertyTaxes;

  /**
   * @return the Home Owner's Association dues
   */
  public BigDecimal getHoaDues() {
    return hoaDues;
  }

  /**
   * @param hoaDues the Home Owner's Association dues
   */
  public void setHoaDues(BigDecimal hoaDues) {
    this.hoaDues = hoaDues;
  }

  /**
   * @return the annual property taxes
   */
  public BigDecimal getPropertyTaxes() {
    return propertyTaxes;
  }

  /**
   * @param propertyTaxes the annual property taxes
   */
  public void setPropertyTaxes(BigDecimal propertyTaxes) {
    this.propertyTaxes = propertyTaxes;
  }

  /**
   * @return the desired overall monthly payment including any HOA fees and property taxes
   */
  public BigDecimal getOverallMonthly() {
    return overallMonthly;
  }

  /**
   * @param overallMonthly the desired overall monthly payment inclusive of any HOA fees and
   *                       property taxes
   */
  public void setOverallMonthly(BigDecimal overallMonthly) {
    this.overallMonthly = overallMonthly;
  }

  /**
   * Calculates the what the monthly payment amount will be for the given loan amount, annual
   * interest rate, and term, and also updates the overall monthly payment to reflect any HOA fees
   * or property taxes.
   *
   * @return the monthly loan payment exclusive of any HOA fees or property taxes
   * @see Loan#calcMonthly()
   */
  @Override
  public BigDecimal calcMonthly() {
    BigDecimal monthly = new BigDecimal(super.calcMonthly().toPlainString());

    if (getHoaDues() != null) {
      monthly = monthly.add(getHoaDues());
    }

    if (getPropertyTaxes() != null) {
      monthly = monthly.add(
          getPropertyTaxes().divide(new BigDecimal(Loan.MONTHS_IN_YEAR), MathContext.DECIMAL128));
    }
    setOverallMonthly(monthly);

    return getMonthlyPmt();
  }
}
