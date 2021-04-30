package agent_friday.richie.ui;

import agent_friday.richie.Loan;
import agent_friday.richie.Mortgage;
import java.math.BigDecimal;
import java.math.MathContext;
import javax.swing.JTextField;

public class MortgagePanel extends LoanPanel {

  protected final JTextField hoaTF = new JTextField();
  protected final JTextField taxesTF = new JTextField();
  protected final JTextField overallTF = new JTextField();

  public MortgagePanel() {
    super(new Mortgage());
    init();
  }

  private void init() {
    addTextField("HOA Fees", hoaTF, true);
    addTextField("Property Taxes", taxesTF, true);
    addTextField("Overall Monthly Payment", overallTF, true);
  }

  @Override
  public void calcLoanAmt() {
    setMortgageAmounts();

    super.calcLoanAmt();

    updateOverallMonthly();
  }

  private void setMortgageAmounts() {
    Mortgage mort = (Mortgage) loan;

    String taxes = getMoneyString(taxesTF);
    if (taxes != null && !taxes.isEmpty()) {
      mort.setPropertyTaxes(new BigDecimal(taxes));
    }

    String hoa = getMoneyString(hoaTF);
    if (hoa != null && !hoa.isEmpty()) {
      mort.setHoaDues(new BigDecimal(hoa));
    }

    String overall = getMoneyString(overallTF);
    if (overall != null && !overall.isEmpty()) {
      mort.setOverallMonthly(new BigDecimal(overall));
    }

    if (mort.getOverallMonthly() != null) {
      BigDecimal monthlyMortPayment = mort.getOverallMonthly();
      if (mort.getHoaDues() != null) {
        monthlyMortPayment = monthlyMortPayment.subtract(mort.getHoaDues());
      }

      if (mort.getPropertyTaxes() != null) {
        monthlyMortPayment = monthlyMortPayment.subtract(mort.getPropertyTaxes()
            .divide(new BigDecimal(Loan.MONTHS_IN_YEAR), MathContext.DECIMAL128));
      }

      mort.setMonthlyPmt(monthlyMortPayment);
      monthlyTF.setText(CURRENCY_FORMAT.format(monthlyMortPayment));
    }
  } // End setMortgageAmounts

  @Override
  public void calcMonthlyPmt() {
    setMortgageAmounts();
    super.calcMonthlyPmt();
    updateOverallMonthly();
  }

  private void updateOverallMonthly() {
    Mortgage mort = (Mortgage) loan;
    overallTF.setText(CURRENCY_FORMAT.format(mort.getOverallMonthly()));
  }
}
