package api;

import api.filters.JWTTokenNeeded;
import controllers.AccountController;
import entities.Account;
import entities.Role;
import utils.PasswordHasher;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.Collection;

@Path("accounts")
public class AccountEndpoint {

    @Inject
    private AccountController accountController;

    @Inject
    private PasswordHasher passwordHasher;

    @Context
    private UriInfo uriInfo;

    @GET
    public Collection<Account> getAllAccounts() {
        Collection<Account> accountList = accountController.getAll();

        if (accountList == null) throw new NotFoundException();
        return accountList;
    }

    @GET
    @Path("/{username}")
    public Account getSpecificAccount(@PathParam("username") String username) {
        Account account = accountController.find(username);
        if (account == null) throw new NotFoundException();

        String selfUri = uriInfo.getBaseUriBuilder()
                .path(AccountEndpoint.class)
                .path(account.getUsername())
                .build()
                .toString();

        String playlistUri = uriInfo.getBaseUriBuilder()
                .path(PlaylistEndpoint.class)
                .path("account")
                .path(account.getUsername())
                .build()
                .toString();

        account.addLink(selfUri, "self");
        account.addLink(playlistUri, "playlists");

        return account;
    }

    @POST
    public void saveAccount(Account account) {
        String hashedPassword = passwordHasher.hash(account.getPassword());
        account.setPassword(hashedPassword);
        accountController.save(account);
    }

    @PUT
    @JWTTokenNeeded(roles = { Role.user, Role.admin })
    public void updateAccount(Account account) {
        String hashedPassword = passwordHasher.hash(account.getPassword());
        account.setPassword(hashedPassword);
        accountController.update(account);
    }

    @DELETE
    @Path("/{username}")
    public void deleteAccount(@PathParam("username") String username) {
        accountController.delete(username);
    }
}
