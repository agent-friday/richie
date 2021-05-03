package agent_friday.richie;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import org.junit.Test;

public class LoanTest {

  @Test
  public void testSetTerm() throws NoSuchFieldException, IllegalAccessException {
    Loan loan = new Loan();

    loan.setTerm(360);

    final Field field = loan.getClass().getDeclaredField("term");
    field.setAccessible(true);
    assertEquals(360, field.get(loan));
  }

  @Test
  public void testGetTerm() throws NoSuchFieldException, IllegalAccessException {
    final Loan loan = new Loan();
    final Field field = loan.getClass().getDeclaredField("term");
    field.setAccessible(true);
    field.set(loan, 360);

    final int result = loan.getTerm();

    assertEquals(360, result);
  }

  @Test
  public void testSetMonthlyPmt() throws NoSuchFieldException, IllegalAccessException {
    Loan loan = new Loan();

    loan.setMonthlyPmt(new BigDecimal("2507.94"));

    final Field field = loan.getClass().getDeclaredField("monthlyPmt");
    field.setAccessible(true);
    assertEquals(new BigDecimal("2507.94"), field.get(loan));
  }

  @Test
  public void testGetMonthlyPmt() throws NoSuchFieldException, IllegalAccessException {
    final Loan loan = new Loan();
    final Field field = loan.getClass().getDeclaredField("monthlyPmt");
    field.setAccessible(true);
    field.set(loan, new BigDecimal("2507.94"));

    final BigDecimal result = loan.getMonthlyPmt();

    assertEquals(new BigDecimal("2507.94"), result);
  }

  @Test
  public void testSetPrincipal() throws NoSuchFieldException, IllegalAccessException {
    Loan loan = new Loan();

    loan.setPrincipal(new BigDecimal("750000"));

    final Field field = loan.getClass().getDeclaredField("principal");
    field.setAccessible(true);
    assertEquals(new BigDecimal("750000"), field.get(loan));
  }

  @Test
  public void testGetPrincipal() throws NoSuchFieldException, IllegalAccessException {
    final Loan loan = new Loan();
    final Field field = loan.getClass().getDeclaredField("principal");
    field.setAccessible(true);
    field.set(loan, new BigDecimal("750000"));

    final BigDecimal result = loan.getPrincipal();

    assertEquals(new BigDecimal("750000"), result);
  }

  @Test
  public void testSetAnnualInterest() throws NoSuchFieldException, IllegalAccessException {
    Loan loan = new Loan();

    loan.setAnnualInterest(new BigDecimal(".0350"));

    final Field field = loan.getClass().getDeclaredField("annualInterest");
    field.setAccessible(true);
    assertEquals(new BigDecimal(".0350"), field.get(loan));
  }

  @Test
  public void testGetAnnualInterest() throws NoSuchFieldException, IllegalAccessException {
    final Loan loan = new Loan();
    final Field field = loan.getClass().getDeclaredField("annualInterest");
    field.setAccessible(true);
    field.set(loan, new BigDecimal(".0295"));

    final BigDecimal result = loan.getAnnualInterest();

    assertEquals(new BigDecimal(".0295"), result);
  }

  @Test
  public void testCalcMonthly() {
    Loan loan = new Loan();
    loan.setPrincipal(new BigDecimal(25000));
    loan.setAnnualInterest(new BigDecimal("0.03125"));
    loan.setTerm(48);

    BigDecimal loanMonthly = loan.calcMonthly();
    String pmtStr = NumberFormat.getCurrencyInstance(Locale.US).format(loanMonthly);
    String expected = NumberFormat.getCurrencyInstance(Locale.US).format(554.74);
    assertEquals(expected, pmtStr);
  }

  @Test
  public void testCalcLoan() {
    Loan loan = new Loan();
    loan.setAnnualInterest(new BigDecimal("0.03125"));
    loan.setMonthlyPmt(new BigDecimal("554.74"));
    loan.setTerm(48);

    BigDecimal loanAmt = loan.calcLoan();
    String loanStr = NumberFormat.getCurrencyInstance(Locale.US).format(loanAmt);
    String expected = NumberFormat.getCurrencyInstance(Locale.US).format(24999.98);
    assertEquals(expected, loanStr);
  }

  @Test
  public void testCalcTerm() {
    Loan loan = new Loan();
    loan.setPrincipal(new BigDecimal(25000));
    loan.setAnnualInterest(new BigDecimal("0.03125"));
    loan.setMonthlyPmt(new BigDecimal("554.74"));

    int loanTerm = loan.calcTerm();
    int expected = 48;
    assertEquals(expected, loanTerm);
  }
}