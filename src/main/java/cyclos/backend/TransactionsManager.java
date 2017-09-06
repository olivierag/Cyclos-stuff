package cyclos.backend;

import org.cyclos.model.EntityNotFoundException;
import org.cyclos.model.banking.*;
import org.cyclos.model.banking.accounts.InternalAccountOwner;
import org.cyclos.model.banking.transactions.*;
import org.cyclos.model.banking.transfertypes.TransferTypeWithCurrencyVO;
import org.cyclos.server.utils.HttpServiceFactory;
import org.cyclos.services.banking.PaymentService;
import org.cyclos.services.banking.TransactionService;
import org.cyclos.utils.CollectionHelper;

import java.math.BigDecimal;
import java.util.List;

/**
    Class managing transactions through a singleton, used when wanting to perform them.
 */
public class TransactionsManager {

    private HttpServiceFactory factory;
    private TransactionService transactionService;
    private PaymentService paymentService;

    private static TransactionsManager singleton;

    public static TransactionsManager getTransactionsManager(){
        if (singleton == null) singleton = new TransactionsManager();
        return singleton;
    }

    private TransactionsManager(){
        factory = Cyclos.getServiceFactory();
        transactionService = factory.getProxy(TransactionService.class);
        paymentService = factory.getProxy(PaymentService.class);
    }

    public void pay(InternalAccountOwner from, InternalAccountOwner to, BigDecimal amount) {

        // fetch data needed for the payment
        PerformPaymentData data;
        try {
            data = transactionService.getPaymentData(from, to, null);
        } catch (EntityNotFoundException e) {
            System.out.println("Some of the users were not found. Transaction aborted.");
            return;
        }

        // try to figure out the type of payment (for some reason)
        List<TransferTypeWithCurrencyVO> types = data.getPaymentTypes();
        TransferTypeWithCurrencyVO paymentType = CollectionHelper.first(types);
        if (paymentType == null) {
            System.out.println("There is no possible payment type.");
        }

        // set up payment to be made
        PerformPaymentDTO payment = new PerformPaymentDTO();
        payment.setType(paymentType);
        payment.setFrom(from);
        payment.setTo(to);
        payment.setAmount(amount);

        try {
            PaymentVO result = paymentService.perform(payment);
            TransactionAuthorizationStatus authStatus = result.getAuthorizationStatus();
            if (authStatus == TransactionAuthorizationStatus.PENDING_AUTHORIZATION) {
                System.out.println("The payment is pending authorization.");
            } else {
                System.out.println("The payment has been processed.");
            }
        } catch (InsufficientBalanceException e) {
            System.out.println("Insufficient balance");
        } catch (MaxPaymentsPerDayExceededException e) {
            System.out.println("Maximum daily amount of transfers "
                    + e.getMaxPayments() + " has been reached");
        } catch (MaxPaymentsPerWeekExceededException e) {
            System.out.println("Maximum weekly amount of transfers "
                    + e.getMaxPayments() + " has been reached");
        } catch (MaxPaymentsPerMonthExceededException e) {
            System.out.println("Maximum monthly amount of transfers "
                    + e.getMaxPayments() + " has been reached");
        } catch (MinTimeBetweenPaymentsException e) {
            System.out.println("A minimum period of time should be awaited to make "
                    + "a payment of this type");
        } catch (MaxAmountPerDayExceededException e) {
            System.out.println("Maximum daily amount of "
                    + e.getMaxAmount() + " has been reached");
        } catch (MaxAmountPerWeekExceededException e) {
            System.out.println("Maximum weekly amount of "
                    + e.getMaxAmount() + " has been reached");
        } catch (MaxAmountPerMonthExceededException e) {
            System.out.println("Maximum monthly amount of "
                    + e.getMaxAmount() + " has been reached");
        } catch (Exception e) {
            System.out.println("The payment couldn't be performed");
        }

    }


}
