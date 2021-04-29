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
    this.setSize(400, 300);
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

    int row = 0;
    Insets insets = new Insets(5, 5, 5, 5);

    panel.add(loanLabel, new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
        GridBagConstraints.NONE, insets, 0, 0));
    panel.add(loanTF, new GridBagConstraints(1, row, 1, 1, 0.0, 1.0, GridBagConstraints.WEST,
        GridBagConstraints.HORIZONTAL, insets, 0, 0));

    row++;
    panel.add(interestLabel, new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
        GridBagConstraints.NONE, insets, 0, 0));
    panel.add(interestTF, new GridBagConstraints(1, row, 1, 1, 0.0, 1.0, GridBagConstraints.WEST,
        GridBagConstraints.HORIZONTAL, insets, 0, 0));

    row++;
    panel.add(termLabel, new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
        GridBagConstraints.NONE, insets, 0, 0));
    panel.add(termTF, new GridBagConstraints(1, row, 1, 1, 0.0, 1.0, GridBagConstraints.WEST,
        GridBagConstraints.HORIZONTAL, insets, 0, 0));

    row++;
    panel.add(monthlyLabel, new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
        GridBagConstraints.NONE, insets, 0, 0));
    panel.add(monthlyTF, new GridBagConstraints(1, row, 1, 1, 0.0, 1.0, GridBagConstraints.WEST,
        GridBagConstraints.HORIZONTAL, insets, 0, 0));

    row++;
    panel.add(isMortgage, new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
        GridBagConstraints.NONE, insets, 0, 0));

    row++;
    panel.add(hoaLabel, new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
        GridBagConstraints.NONE, insets, 0, 0));
    panel.add(hoaTF, new GridBagConstraints(1, row, 1, 1, 0.0, 1.0, GridBagConstraints.WEST,
        GridBagConstraints.HORIZONTAL, insets, 0, 0));

    row++;
    panel.add(taxesLabel, new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
        GridBagConstraints.NONE, insets, 0, 0));
    panel.add(taxesTF, new GridBagConstraints(1, row, 1, 1, 0.0, 1.0, GridBagConstraints.WEST,
        GridBagConstraints.HORIZONTAL, insets, 0, 0));

    row++;
    panel.add(overallLabel, new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
        GridBagConstraints.NONE, insets, 0, 0));
    panel.add(overallTF, new GridBagConstraints(1, row, 1, 1, 0.0, 1.0, GridBagConstraints.WEST,
        GridBagConstraints.HORIZONTAL, insets, 0, 0));

    panel.add(buttonPanel,
        new GridBagConstraints(0, GridBagConstraints.RELATIVE, GridBagConstraints.REMAINDER, 1, 1.0,
            0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, insets, 0,
            0));

    this.add(panel);
  }

  private void setMortgageAmts() {
    Mortgage mort = (Mortgage) loan;

    String taxes = taxesTF.getText();
    try {
      if (taxes != null && !taxes.isEmpty()) {
        taxes = currencyFormat.parse(taxesTF.getText()).toString();
      }
    } catch (ParseException e) {
      JOptionPane
          .showConfirmDialog(this, e, "Error", JOptionPane.OK_CANCEL_OPTION,
              JOptionPane.ERROR_MESSAGE);
      e.printStackTrace();
    }

    if (taxes != null && !taxes.isEmpty()) {
      mort.setPropertyTaxes(new BigDecimal(taxes));
    }

    String hoa = hoaTF.getText();
    try {
      if (hoa != null && !hoa.isEmpty()) {
        hoa = currencyFormat.parse(hoa).toString();
      }
    } catch (ParseException e) {
      JOptionPane
          .showConfirmDialog(this, e, "Error", JOptionPane.OK_CANCEL_OPTION,
              JOptionPane.ERROR_MESSAGE);
      e.printStackTrace();
    }

    if (hoa != null && !hoa.isEmpty()) {
      mort.setHoaDues(new BigDecimal(hoa));
    }

    String overall = overallTF.getText();
    try {
      if (overall != null && !overall.isEmpty()) {
        overall = currencyFormat.parse(overall).toString();
      }
    } catch (ParseException e) {
      JOptionPane
          .showConfirmDialog(this, e, "Error", JOptionPane.OK_CANCEL_OPTION,
              JOptionPane.ERROR_MESSAGE);
      e.printStackTrace();
    }

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
  } // End setMortgageAmts

  private void calcLoanAmt() {
    if (isMortgage.isSelected()) {
      setMortgageAmts();
    }

    String pmt = null;
    try {
      pmt = currencyFormat.parse(monthlyTF.getText()).toString();
    } catch (ParseException e) {
      JOptionPane
          .showConfirmDialog(this, e, "Error", JOptionPane.OK_CANCEL_OPTION,
              JOptionPane.ERROR_MESSAGE);
      e.printStackTrace();
    }

    if (pmt != null) {
      loan.setMonthlyPmt(new BigDecimal(pmt));
    }

    String rate = null;
    try {
      rate = NumberFormat.getInstance(Locale.US).parse(interestTF.getText()).toString();
    } catch (ParseException e) {
      JOptionPane
          .showConfirmDialog(this, e, "Error", JOptionPane.OK_CANCEL_OPTION,
              JOptionPane.ERROR_MESSAGE);
      e.printStackTrace();
    }

    if (rate != null) {
      loan.setAnnualInterest(new BigDecimal(rate).divide(hundred, MathContext.DECIMAL128));
    }

    try {
      loan.setTerm(Integer.parseInt(termTF.getText()));
    } catch (NumberFormatException e) {
      JOptionPane
          .showConfirmDialog(this, e, "Error", JOptionPane.OK_CANCEL_OPTION,
              JOptionPane.ERROR_MESSAGE);
      e.printStackTrace();
    }

    loanTF.setText(currencyFormat.format(loan.calcLoan()));
  }

  private void calcMonthlyPmt() {
    if (isMortgage.isSelected()) {
      setMortgageAmts();
    }

    String amt = null;
    try {
      amt = currencyFormat.parse(loanTF.getText()).toString();
    } catch (ParseException e) {
      JOptionPane
          .showConfirmDialog(this, e, "Error", JOptionPane.OK_CANCEL_OPTION,
              JOptionPane.ERROR_MESSAGE);
      e.printStackTrace();
    }

    if (amt != null) {
      loan.setPrincipal(new BigDecimal(amt));
    }

    String rate = null;
    try {
      rate = NumberFormat.getInstance(Locale.US).parse(interestTF.getText()).toString();
    } catch (ParseException e) {
      JOptionPane
          .showConfirmDialog(this, e, "Error", JOptionPane.OK_CANCEL_OPTION,
              JOptionPane.ERROR_MESSAGE);
      e.printStackTrace();
    }

    if (rate != null) {
      loan.setAnnualInterest(new BigDecimal(rate).divide(hundred, MathContext.DECIMAL128));
    }

    try {
      loan.setTerm(Integer.parseInt(termTF.getText()));
    } catch (NumberFormatException e) {
      JOptionPane
          .showConfirmDialog(this, e, "Error", JOptionPane.OK_CANCEL_OPTION,
              JOptionPane.ERROR_MESSAGE);
      e.printStackTrace();
    }

    monthlyTF.setText(currencyFormat.format(loan.calcMonthly()));
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
          tf.setText(currencyFormat.format(usFormat.parse(tf.getText())));
        }
      } catch (ParseException parseException) {
        parseException.printStackTrace();
      }
    }
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
}
