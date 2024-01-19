package account.service.rest.adapter;

import account.service.aggregate.Account;
import account.service.aggregate.AccountId;
import account.service.aggregate.AccountType;
import account.service.aggregate.Token;
import account.service.service.AccountAlreadyExists;
import account.service.service.AccountService;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

@Path("/accounts")
public class AccountResource {

	AccountServiceFactory factory = new AccountServiceFactory();
	AccountService service = factory.getService();


	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response registerAccount(AccountDTO account) {
		try {
			return Response.ok(service.register(account.getName(), account.getLastName(),new AccountType(account.getType()),account.getCpr(),account.getBankId()).toString()).build();
		} catch (AccountAlreadyExists e) {
			return Response.status(409).entity(account.getCpr()).build();
		} catch (Exception e) {
			return Response.status(500).build();
		}
	}
	@GET
	@Path("{accountId}")
	@Produces("application/json")
	public Response getAccount(@PathParam("accountId") String accountId) {

		Account account = service.getAccount(new AccountId(UUID.fromString(accountId)));
		AccountDTO accountDTO = new AccountDTO(account.getAccountId().getUuid().toString(),account.getName(),account.getLastname(),account.getType().getType(),account.getCpr(),account.getBankId());

		if (account != null) {
			return Response.ok(accountDTO).build();
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
	@Path("/{accountId}/tokens")
	@GET
	@Produces("application/json")
	public Response getTokens(@PathParam("accountId") String accountId) {
		List<Token> tokens = service.getTokens(new AccountId(UUID.fromString(accountId)));
		return Response.ok(tokens).build();
	}
	@Path("/{accountId}/tokens")
	@POST
	@Consumes("text/plain")
	@Produces("application/json")
	public Response newTokens(@PathParam("accountId") String accountId, String amount) {
		//service.generateNewTokens(accountId, Integer.parseInt(amount));
		//List<Token> tokens = service.getTokens(accountId).join();
		return Response.ok().build();
	}


}

