package cyclos.backend;

import org.cyclos.model.users.users.UserQuery;
import org.cyclos.model.users.users.UserWithFieldsVO;
import org.cyclos.server.utils.HttpServiceFactory;
import org.cyclos.services.users.UserService;
import org.cyclos.utils.Page;

/**
    Class managing searches for users through a singleton, used when wanting to perform them.
 */
public class UserSearchManager {

    private HttpServiceFactory factory;
    private UserService userService;
    private UserQuery userQuery;
    private static final int DEFAULT_PAGE_LIMIT = 5;

    private static UserSearchManager singleton;

    public static UserSearchManager getUserSearchManager(){
        if (singleton == null) singleton = new UserSearchManager();
        return singleton;
    }

    private UserSearchManager(){
        factory = Cyclos.getServiceFactory();
        userService = factory.getProxy(UserService.class);
        userQuery = new UserQuery();
    }

    public Page<UserWithFieldsVO> searchForUsers(String query){ return searchForUsers(query, DEFAULT_PAGE_LIMIT); }

    public Page<UserWithFieldsVO> searchForUsers(String query, int limit) {
        userQuery.setKeywords(query);
        userQuery.setIgnoreProfileFieldsInList(true);
        userQuery.setPageSize(limit);
        return userService.search(userQuery);
    }

}
