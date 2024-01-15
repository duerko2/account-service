package account.service.rest.adapter;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import account.service.Account;
import account.service.AccountAlreadyExists;
import account.service.AccountService;

@Path("/accounts")
public class AccountResource {

	AccountServiceFactory factory = new AccountServiceFactory();
	AccountService service = factory.getService();


	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response registerAccount(Account account) {
		try {
			Account a = service.register(account);
			return Response.ok(a).build();
		} catch (AccountAlreadyExists e) {
			return Response.status(409).entity(account.getCpr()).build();
		}
	}
	@GET
	@Path("{accountId}")
	@Produces("application/json")
	public Response getAccount(@PathParam("accountId") String accountId) {
		Account account = service.getAccount(accountId);
		if (account != null) {
			return Response.ok(account).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@DELETE
	@Path("/{accountId}")
	@Produces("application/json")
	public Response deleteAccount(@PathParam("accountId") String accountId) {
		service.deleteAccount(accountId);
		return Response.ok().build();
	}
}
