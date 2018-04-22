package com.projects.blockchain.ethereum.poc.node_connector.restful;

import java.math.BigInteger;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.projects.blockchain.ethereum.poc.node_connector.util.ServletContextAttribute;
import com.projects.blockchain.ethereum.smart_contracts.CoinManager;

@Path("/depositmanager")
public final class DepositManagerProxyImpl implements DepositManagerProxy {

	@Context 
	private ServletContext sc;
	
	@Override
	@POST
	@Path("/deposit")
	@Consumes(MediaType.TEXT_PLAIN)
	public Response deposit(final long amount) {
		final CoinManager coinManager = (CoinManager)sc.getAttribute(ServletContextAttribute.CoinManager.toString());
		try {
			coinManager.sendToDepositManager(BigInteger.valueOf(amount)).send();
			return Response.ok("Deposited "+amount+" WEIs.").build();
		} catch (final Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
	
	
	@Override
	@GET
	@Path("/getAccountBalance/{account}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getAccountBalance(@PathParam("account") final String account) {
		final CoinManager coinManager = (CoinManager)sc.getAttribute(ServletContextAttribute.CoinManager.toString());
		try {
			return Response.ok(coinManager.balances(account).send().longValue()).build();
		} catch (final Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
}
