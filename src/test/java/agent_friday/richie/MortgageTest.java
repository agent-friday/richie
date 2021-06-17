package agent_friday.richie;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import org.junit.Test;

public class MortgageTest {

  @Test
  public void testGetHoaDues() throws NoSuchFieldException, IllegalAccessException {
    final Mortgage loan = new Mortgage();
    final Field field = loan.getClass().getDeclaredField("hoaDues");
    field.setAccessible(true);
    field.set(loan, new BigDecimal("194.93"));

    final BigDecimal result = loan.getHoaDues();

    assertEquals(new BigDecimal("194.93"), result);
  }

  @Test
  public void testSetHoaDues() throws NoSuchFieldException, IllegalAccessException {
    Mortgage loan = new Mortgage();

    loan.setHoaDues(new BigDecimal("253.94"));

    final Field field = loan.getClass().getDeclaredField("hoaDues");
    field.setAccessible(true);
    assertEquals(new BigDecimal("253.94"), field.get(loan));
  }

  @Test
  public void testGetPropertyTaxes() throws NoSuchFieldException, IllegalAccessException {
    final Mortgage loan = new Mortgage();
    final Field field = loan.getClass().getDeclaredField("propertyTaxes");
    field.setAccessible(true);
    field.set(loan, new BigDecimal("19468.93"));

    final BigDecimal result = loan.getPropertyTaxes();

    assertEquals(new BigDecimal("19468.93"), result);
  }

  @Test
  public void testSetPropertyTaxes() throws NoSuchFieldException, IllegalAccessException {
    Mortgage loan = new Mortgage();

    loan.setPropertyTaxes(new BigDecimal("19468.93"));

    final Field field = loan.getClass().getDeclaredField("propertyTaxes");
    field.setAccessible(true);
    assertEquals(new BigDecimal("19468.93"), field.get(loan));
  }

  @Test
  public void testGetOverallMonthly() throws NoSuchFieldException, IllegalAccessException {
    final Mortgage loan = new Mortgage();
    final Field field = loan.getClass().getDeclaredField("overallMonthly");
    field.setAccessible(true);
    field.set(loan, new BigDecimal("2500.00"));

    final BigDecimal result = loan.getOverallMonthly();

    assertEquals(new BigDecimal("2500.00"), result);
  }

  @Test
  public void testSetOverallMonthly() throws NoSuchFieldException, IllegalAccessException {
    Mortgage loan = new Mortgage();

    loan.setOverallMonthly(new BigDecimal("2500.00"));

    final Field field = loan.getClass().getDeclaredField("overallMonthly");
    field.setAccessible(true);
    assertEquals(new BigDecimal("2500.00"), field.get(loan));
  }

  @Test
  public void testCalcMonthly() {
    Mortgage loan = new Mortgage();
    loan.setPrincipal(new BigDecimal(25000));
    loan.setAnnualInterest(new BigDecimal("0.03125"));
    loan.setTerm(48);

    BigDecimal loanMonthly = loan.calcMonthly();
    String pmtStr = NumberFormat.getCurrencyInstance(Locale.US).format(loanMonthly);
    String expected = NumberFormat.getCurrencyInstance(Locale.US).format(554.74);
    assertEquals(expected, pmtStr);
  }
}