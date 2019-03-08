package api;

import controllers.AccountController;
import entities.Account;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

@Path("accounts")
public class AccountEndpoint {

    @Inject
    private AccountController accountController;

    @GET
    public List<Account> getAllAccounts() {
        return accountController.getAll();
    }

    @GET
    @Path("/{username}")
    public Account getSpecificAccount(@PathParam("username") String username) {
        Account account = accountController.find(username);

        if (account == null) throw new NotFoundException();
        return account;
    }
}
