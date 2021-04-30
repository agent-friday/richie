package agent_friday.richie.ui;

import agent_friday.richie.Loan;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LoanPanel extends JPanel implements FocusListener {

  protected static final BigDecimal hundred = new BigDecimal(100);

  protected static final NumberFormat US_FORMAT = NumberFormat.getInstance(Locale.US);
  protected static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.US);

  protected static final Insets insets = new Insets(5, 5, 5, 5);
  protected static final GridBagConstraints leftColConstraints = new GridBagConstraints(
      GridBagConstraints.RELATIVE, // col
      GridBagConstraints.RELATIVE, // row
      1, // grid-width
      1, // grid-height
      0.0, // weight-x
      0.0, // weight-y
      GridBagConstraints.EAST, // anchor
      GridBagConstraints.NONE, // fill
      insets, // Insets
      0,// ipad-x
      0 // ipad-y
  );

  protected static final GridBagConstraints rightColConstraints = new GridBagConstraints(
      GridBagConstraints.RELATIVE, // col
      GridBagConstraints.RELATIVE, // row
      GridBagConstraints.REMAINDER, // grid-width
      1, // grid-height
      1.0, // weight-x
      0.0, // weight-y
      GridBagConstraints.WEST, // anchor
      GridBagConstraints.HORIZONTAL, // fill
      insets, // Insets
      0, // ipad-x
      0 // ipad-y
  );

  protected Loan loan;

  protected final JTextField interestTF = new JTextField();
  protected final JTextField loanTF = new JTextField();
  protected final JTextField termTF = new JTextField();
  protected final JTextField monthlyTF = new JTextField();

  public LoanPanel() {
    loan = new Loan();
    init();
  }

  public LoanPanel(Loan loan) {
    this.loan = loan;
    init();
  }

  private void init() {
    this.setLayout(new GridBagLayout());

    addTextField("Loan Amount", loanTF, true);
    addTextField("Annual Interest Rate (%)", interestTF, false);
    addTextField("Loan Term (Months)", termTF, false);
    addTextField("Monthly Loan Payment", monthlyTF, true);
  }

  protected void addTextField(String label, JTextField textField, boolean addFocusListener) {
    JLabel jLabel = new JLabel(label);
    jLabel.setLabelFor(textField);
    if (addFocusListener) {
      textField.addFocusListener(this);
    }
    add(jLabel, leftColConstraints);
    add(textField, rightColConstraints);
  }

  public void calcLoanAmt() {
    String pmt = getMoneyString(monthlyTF);
    if (pmt != null && !pmt.isEmpty()) {
      loan.setMonthlyPmt(new BigDecimal(pmt));
    } else {
      showError("Monthly payment can't be empty.");
      return;
    }

    try {
      parseInterestRate();
      parseLoanTerm();
    } catch (ParseException | NumberFormatException e) {
      showError(e);
      return;
    }

    loanTF.setText(CURRENCY_FORMAT.format(loan.calcLoan()));

  } // End calcLoanAmt

  public void calcMonthlyPmt() {
    String amt = getMoneyString(loanTF);
    if (amt != null && !amt.isEmpty()) {
      loan.setPrincipal(new BigDecimal(amt));
    } else {
      showError("Loan amount cannot be empty.");
      return;
    }

    try {
      parseInterestRate();
      parseLoanTerm();
    } catch (ParseException | NumberFormatException e) {
      showError(e);
      return;
    }

    monthlyTF.setText(CURRENCY_FORMAT.format(loan.calcMonthly()));
  } // End calcMonthlyPmt

  protected String getMoneyString(JTextField jtf) {
    String value = jtf.getText();
    try {
      if (value != null && !value.isEmpty()) {
        value = CURRENCY_FORMAT.parse(jtf.getText()).toString();
      }
    } catch (ParseException e) {
      showError(e);
      value = null;
    }

    return value;
  } // End getMoneyString

  private void parseLoanTerm() throws NumberFormatException {
    String term = termTF.getText();
    if (term == null || term.isEmpty()) {
      showError("Interest rate cannot be null.");
      return;
    }

    loan.setTerm(Integer.parseInt(term));
  } // End parseLoanTerm

  private void parseInterestRate() throws ParseException {
    String rate = interestTF.getText();
    if (rate == null || rate.isEmpty()) {
      showError("Interest rate cannot be null.");
      return;
    }

    rate = NumberFormat.getInstance(Locale.US).parse(rate).toString();

    if (rate != null && !rate.isEmpty()) {
      loan.setAnnualInterest(new BigDecimal(rate).divide(hundred, MathContext.DECIMAL128));
    }
  } // End parseInterestRate

  @Override
  public void focusGained(FocusEvent e) {
    // Intentionally blank
  }

  @Override
  public void focusLost(FocusEvent e) {
    Object src = e.getSource();
    if (src instanceof JTextField) {
      JTextField tf = (JTextField) src;
      try {
        if (tf.getText() != null && !tf.getText().isEmpty()) {
          tf.setText(CURRENCY_FORMAT.format(getNumber(tf.getText())));
        }
      } catch (ParseException parseException) {
        showError("Could not parse amount.");
      }
    }
  }

  private Number getNumber(String text) throws ParseException {
    Number n;

    try {
      n = US_FORMAT.parse(text);
    } catch (ParseException e) {
      n = CURRENCY_FORMAT.parse(text);
    }

    return n;
  }

  protected void showError(Object error) {
    JOptionPane.showConfirmDialog(this, error, "Error", JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.ERROR_MESSAGE);
  }
}
