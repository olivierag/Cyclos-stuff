package cyclos.backend;

import org.apache.log4j.PropertyConfigurator;
import org.cyclos.model.banking.accounts.InternalAccountOwner;
import org.cyclos.model.users.users.UserLocatorVO;

import java.math.BigDecimal;


/**
 Class for testing transaction from one class to another,
 values must be changed to usernames of existing users in the system.
 */
public class TransactionsTesting {

    public static void main(String[] args) {

        PropertyConfigurator.configure("src/main/resources/log4j.properties");

        InternalAccountOwner fromAccount = new UserLocatorVO(UserLocatorVO.USERNAME, "test");
        InternalAccountOwner toAccount = new UserLocatorVO(UserLocatorVO.USERNAME, "test2");
        BigDecimal amount = BigDecimal.valueOf(2);

        TransactionsManager.getTransactionsManager().pay(fromAccount, toAccount, amount);

    }
}
