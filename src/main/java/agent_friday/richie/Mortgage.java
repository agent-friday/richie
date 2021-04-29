package agent_friday.richie;

import java.math.BigDecimal;
import java.math.MathContext;

public class Mortgage extends Loan {

  private BigDecimal overallMonthly;
  private BigDecimal hoaDues;
  private BigDecimal propertyTaxes;

  public BigDecimal getHoaDues() {
    return hoaDues;
  }

  public void setHoaDues(BigDecimal hoaDues) {
    this.hoaDues = hoaDues;
  }

  public BigDecimal getPropertyTaxes() {
    return propertyTaxes;
  }

  public void setPropertyTaxes(BigDecimal propertyTaxes) {
    this.propertyTaxes = propertyTaxes;
  }

  public BigDecimal getOverallMonthly() {
    return overallMonthly;
  }

  public void setOverallMonthly(BigDecimal overallMonthly) {
    this.overallMonthly = overallMonthly;
  }

  @Override
  public BigDecimal calcMonthly() {
    BigDecimal monthly = super.calcMonthly();

    if (getHoaDues() != null) {
      monthly = monthly.add(getHoaDues());
    }

    if (getPropertyTaxes() != null) {
      monthly = monthly.add(
          getPropertyTaxes().divide(new BigDecimal(Loan.MONTHS_IN_YEAR), MathContext.DECIMAL128));
    }
    setOverallMonthly(monthly);

    return getOverallMonthly();
  }
}
