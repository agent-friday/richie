package agent_friday.richie;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;

public class ClientFrame extends JFrame implements FocusListener {

  private Loan loan = new Loan();
  private final NumberFormat usFormat = NumberFormat.getInstance(Locale.US);
  private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
  private final BigDecimal hundred = new BigDecimal(100);

  private final JCheckBox isMortgage = new JCheckBox("Mortgage?");
  private final JTextField interestTF = new JTextField();
  private final JTextField loanTF = new JTextField();
  private final JTextField termTF = new JTextField();
  private final JTextField monthlyTF = new JTextField();
  private final JTextField hoaTF = new JTextField();
  private final JTextField taxesTF = new JTextField();
  private final JTextField overallTF = new JTextField();

  public ClientFrame() {
    super("Loan Calculator");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    init();
  }

  private void init() {
    this.setSize(400, 375);
    this.setLocationRelativeTo(null);

    JLabel interestLabel = new JLabel("Annual Interest Rate (%)");
    interestLabel.setLabelFor(interestTF);

    JLabel loanLabel = new JLabel("Loan Amount");
    loanLabel.setLabelFor(loanTF);
    loanTF.addFocusListener(this);

    JLabel termLabel = new JLabel("Loan Term");
    termLabel.setLabelFor(termTF);

    JLabel monthlyLabel = new JLabel("Monthly Loan Payment");
    monthlyLabel.setLabelFor(monthlyTF);
    monthlyTF.addFocusListener(this);

    JLabel hoaLabel = new JLabel("HOA Fees");
    hoaLabel.setLabelFor(hoaTF);
    hoaTF.setEnabled(false);
    hoaTF.addFocusListener(this);

    JLabel taxesLabel = new JLabel("Property Taxes");
    taxesLabel.setLabelFor(taxesTF);
    taxesTF.setEnabled(false);
    taxesTF.addFocusListener(this);

    JLabel overallLabel = new JLabel("Overall Monthly Payment");
    overallLabel.setLabelFor(overallTF);
    overallTF.setEnabled(false);
    overallTF.addFocusListener(this);

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton monthlyButton = new JButton("Calculate Monthly");
    JButton loanAmtButton = new JButton("Calculate Loan Amount");

    isMortgage.addChangeListener((ChangeEvent e) -> checkStateChange());
    monthlyButton.addActionListener((ActionEvent e) -> calcMonthlyPmt());
    loanAmtButton.addActionListener((ActionEvent e) -> calcLoanAmt());

    buttonPanel.add(monthlyButton);
    buttonPanel.add(loanAmtButton);

    JPanel panel = new JPanel(new GridBagLayout());

    Insets insets = new Insets(5, 5, 5, 5);
    GridBagConstraints leftColConstraints = new GridBagConstraints(
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

    GridBagConstraints rightColConstraints = new GridBagConstraints(
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

    panel.add(loanLabel, leftColConstraints);
    panel.add(loanTF, rightColConstraints);

    panel.add(interestLabel, leftColConstraints);
    panel.add(interestTF, rightColConstraints);

    panel.add(termLabel, leftColConstraints);
    panel.add(termTF, rightColConstraints);

    panel.add(monthlyLabel, leftColConstraints);
    panel.add(monthlyTF, rightColConstraints);

    panel.add(isMortgage,
        new GridBagConstraints(1, GridBagConstraints.RELATIVE, GridBagConstraints.REMAINDER, 1, 1.0,
            0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, insets, 0, 0));

    panel.add(hoaLabel, leftColConstraints);
    panel.add(hoaTF, rightColConstraints);

    panel.add(taxesLabel, leftColConstraints);
    panel.add(taxesTF, rightColConstraints);

    panel.add(overallLabel, leftColConstraints);
    panel.add(overallTF, rightColConstraints);

    panel.add(buttonPanel,
        new GridBagConstraints(0, GridBagConstraints.RELATIVE, GridBagConstraints.REMAINDER, 1, 1.0,
            0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, insets, 0,
            0));

    this.add(panel);
  }

  private String getMoneyString(JTextField jtf) {
    String value = jtf.getText();
    try {
      if (value != null && !value.isEmpty()) {
        value = currencyFormat.parse(jtf.getText()).toString();
      }
    } catch (ParseException e) {
      showError(e);
      value = null;
    }

    return value;
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
      monthlyTF.setText(currencyFormat.format(monthlyMortPayment));
    }
  } // End setMortgageAmounts

  private void updateOverallMonthly() {
    Mortgage mort = (Mortgage) loan;

    overallTF.setText(currencyFormat.format(mort.getOverallMonthly()));
  }

  private void calcLoanAmt() {
    if (isMortgage.isSelected()) {
      setMortgageAmounts();
    }

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

    loanTF.setText(currencyFormat.format(loan.calcLoan()));

    if (isMortgage.isSelected()) {
      updateOverallMonthly();
    }
  } // End calcLoanAmt

  private void calcMonthlyPmt() {
    if (isMortgage.isSelected()) {
      setMortgageAmounts();
    }

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

    monthlyTF.setText(currencyFormat.format(loan.calcMonthly()));
  }

  private void parseLoanTerm() throws NumberFormatException {
    String term = termTF.getText();
    if (term == null || term.isEmpty()) {
      showError("Interest rate cannot be null.");
      return;
    }

    loan.setTerm(Integer.parseInt(term));
  }

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
  }

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
          tf.setText(currencyFormat.format(getNumber(tf.getText())));
        }
      } catch (ParseException parseException) {
        showError("Could not parse amount.");
      }
    }
  }

  private Number getNumber(String text) throws ParseException {
    Number n;

    try {
      n = usFormat.parse(text);
    } catch (ParseException e) {
      n = currencyFormat.parse(text);
    }

    return n;
  }

  private void checkStateChange() {
    hoaTF.setEnabled(isMortgage.isSelected());
    taxesTF.setEnabled(isMortgage.isSelected());
    overallTF.setEnabled(isMortgage.isSelected());

    if (isMortgage.isSelected()) {
      loan = new Mortgage();
    } else {
      loan = new Loan();
    }
  }

  private void showError(Object error) {
    JOptionPane.showConfirmDialog(this, error, "Error", JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.ERROR_MESSAGE);
  }
}
