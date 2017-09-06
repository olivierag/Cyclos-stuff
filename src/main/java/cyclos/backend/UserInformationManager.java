package cyclos.backend;

import org.cyclos.model.banking.accounts.*;
import org.cyclos.model.users.users.UserLocatorVO;
import org.cyclos.server.utils.HttpServiceFactory;
import org.cyclos.services.banking.AccountService;
import org.cyclos.utils.Page;

import java.math.BigDecimal;

/**
 * Created by Sander on 05/09/2017.
 */
public class UserInformationManager {

    private static final int DEFAULT_PAGE_LIMIT = 5;

    private HttpServiceFactory factory;
    private UserLocatorVO userLocator;
    private AccountService accountService;
    private AccountHistoryQuery accountHistoryQuery;

    private static UserInformationManager singleton;

    public static UserInformationManager getUserInformationManager(){
        if (singleton == null) singleton = new UserInformationManager();
        return singleton;
    }

    private UserInformationManager(){
        factory = Cyclos.getServiceFactory();
        accountService = factory.getProxy(AccountService.class);
        userLocator = new UserLocatorVO();
        accountHistoryQuery = new AccountHistoryQuery();
    }

    public BigDecimal getBalance(AccountVO accountVo){
        AccountWithStatusVO account = accountService.getAccountWithStatus(accountVo, null);
        return account.getStatus().getBalance();
    }

    public Page<AccountHistoryEntryVO> getAccountHistory(AccountVO accountVO, int limit){
        accountHistoryQuery.setAccount(accountVO);
        accountHistoryQuery.setPageSize(limit);
        return accountService.searchAccountHistory(accountHistoryQuery);
    }

    public Page<AccountHistoryEntryVO> getAccountHistory(AccountVO accountVO){
        return getAccountHistory(accountVO, DEFAULT_PAGE_LIMIT);
    }

}
