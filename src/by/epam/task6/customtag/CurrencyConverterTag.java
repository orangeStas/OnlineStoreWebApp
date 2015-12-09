package by.epam.task6.customtag;

import org.apache.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Handler for custom tag
 * Define type of currency and convert value {@link CurrencyType}
 */
public class CurrencyConverterTag extends SimpleTagSupport {

    private static final Logger LOGGER = Logger.getLogger(CurrencyConverterTag.class.getPackage().getName());

    private static final double EUR_USD_RATIO = 1.0634;
    private static final double BYR_USD_RATIO = 18000;

    private double totalCostValue;
    private String currency;

    public CurrencyConverterTag(){}

    public void setTotalCostValue(double totalCostValue) {
        this.totalCostValue = totalCostValue;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * Called by the container to invoke this tag
     * @throws JspException If an error occurred while processing this tag.
     * @throws IOException If the page that (either directly or indirectly) invoked this tag is to cease evaluation.
     */
    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();
        CurrencyType currencyType;
        try {
           currencyType = CurrencyType.valueOf(currency);
        } catch (IllegalArgumentException e) {
            LOGGER.debug("Illegal currency argument");
            currencyType = CurrencyType.USD;
        }

        NumberFormat formatter = new DecimalFormat("#0.00");
        double convertedTotalCost;
        switch (currencyType) {
            case EUR: {
                convertedTotalCost = convertToEuro(totalCostValue);
                out.write(formatter.format(convertedTotalCost) + "&euro;");
                break;
            }
            case BYR: {
                convertedTotalCost = convertToBYR(totalCostValue);
                out.write(formatter.format(convertedTotalCost) + " BYR");
                break;
            }
            default:{
                convertedTotalCost = totalCostValue;
                out.write(formatter.format(convertedTotalCost) + "$");
            }
        }
    }

    private double convertToEuro(double value) {
        return value/EUR_USD_RATIO;
    }
    private double convertToBYR(double value) {
        return value * BYR_USD_RATIO;
    }
}

enum CurrencyType {
    USD,
    EUR,
    BYR
}
