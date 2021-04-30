package agent_friday.richie.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ClientFrame extends JFrame implements ChangeListener {

  private final JPanel mainPanel = new JPanel(new BorderLayout());
  private final JTabbedPane tabPane = new JTabbedPane(JTabbedPane.TOP);

  private final LoanPanel[] panels = new LoanPanel[2];
  private LoanPanel currentPanel;

  public ClientFrame() {
    super("Loan Calculator");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    init();
  }

  private void init() {
    this.setSize(550, 375);
    this.setLocationRelativeTo(null);

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton monthlyButton = new JButton("Calculate Monthly");
    JButton loanAmtButton = new JButton("Calculate Loan Amount");
    JButton termButton = new JButton("Calculate Loan Term");

    monthlyButton.addActionListener((ActionEvent e) -> currentPanel.calcMonthlyPmt());
    loanAmtButton.addActionListener((ActionEvent e) -> currentPanel.calcLoanAmt());
    termButton.addActionListener((ActionEvent e) -> currentPanel.calcLoanTerm());

    buttonPanel.add(termButton);
    buttonPanel.add(monthlyButton);
    buttonPanel.add(loanAmtButton);

    panels[0] = new LoanPanel();
    panels[1] = new MortgagePanel();

    tabPane.add("Loan", panels[0]);
    tabPane.add("Mortgage", panels[1]);
    tabPane.addChangeListener(this);

    currentPanel = panels[0];

    mainPanel.add(tabPane, BorderLayout.CENTER);
    mainPanel.add(buttonPanel, BorderLayout.SOUTH);

    this.add(mainPanel);
  } // End init

  @Override
  public void stateChanged(ChangeEvent e) {
    currentPanel = panels[tabPane.getSelectedIndex()];
  }
}
