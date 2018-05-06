package com.projects.blockchain.ethereum.poc.node_connector.restful;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.projects.blockchain.ethereum.poc.node_connector.util.ServletContextAttribute;
import com.projects.blockchain.ethereum.restful.DepositData;
import com.projects.blockchain.ethereum.smart_contracts.CoinManager;
import com.projects.blockchain.ethereum.smart_contracts.DepositManager;

@Path("/depositManager")
public final class DepositManagerRESTfulImpl implements DepositManagerRESTful {

	@Context 
	private ServletContext sc;
	
	@Override
	@PUT
	@Path("/deposit")
	@Consumes(MediaType.APPLICATION_XML)
	public Response deposit(final DepositData depositData) {
		final CoinManager coinManager = (CoinManager)sc.getAttribute(ServletContextAttribute.CoinManager.toString());
		try {
			coinManager.sendToDepositManager(depositData.getTargetAccount(), depositData.getAmount()).send();
			return Response.ok("Deposited "+depositData.getAmount()+" WEIs to "+depositData.getTargetAccount()).build();
		} catch (final Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
	
	
	@Override
	@GET
	@Path("/getAccountBalance/{account}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getAccountBalance(@PathParam("account") final String account) {
		final DepositManager depositManager = (DepositManager)sc.getAttribute(ServletContextAttribute.DepositManager.toString());
		try {
			return Response.ok(depositManager.weiDeposits(account).send().longValue()).build();
		} catch (final Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
	
	@Override
	@GET
	@Path("/getDepositManagerBalance")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getDepositManagerBalance() {
		final DepositManager depositManager = (DepositManager)sc.getAttribute(ServletContextAttribute.DepositManager.toString());
		try {
			return Response.ok(depositManager.getBalance().send().longValue()).build();
		} catch (final Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
}
