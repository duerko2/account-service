package account.service.rest.adapter;

import account.service.aggregate.Account;
import account.service.aggregate.AccountId;
import account.service.aggregate.AccountType;
import account.service.service.AccountAlreadyExists;
import account.service.service.AccountService;
import io.cucumber.java.en_old.Ac;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("/accounts")
public class AccountResource {

	AccountServiceFactory factory = new AccountServiceFactory();
	AccountService service = factory.getService();


	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response registerAccount(String firstName, String lastName, AccountType type, String cpr,String bankId) {
		try {
			return Response.ok(service.register(firstName, lastName, type, cpr, bankId)).build();
		} catch (AccountAlreadyExists e) {
			return Response.status(409).entity(cpr).build();
		} catch (Exception e) {
			return Response.status(500).build();
		}
	}
	@GET
	@Path("{accountId}")
	@Produces("application/json")
	public Response getAccount(@PathParam("accountId") String accountId) {

		Account account = service.getAccount(new AccountId(UUID.fromString(accountId)));
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
		service.deleteAccount(new AccountId(UUID.fromString(accountId)));
		return Response.ok().build();
	}
}
