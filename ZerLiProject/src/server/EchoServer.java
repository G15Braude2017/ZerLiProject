package server;
// This file contains material supporting section 3.7 of the textbook:

// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import ocsf.server.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import clientServerCommon.PacketClass;


public class EchoServer extends AbstractServer {
	// Class variables *************************************************

	private static Connection sqlConnection;

	// Constructors ****************************************************

	
	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port
	 *            The port number to connect on.
	 */
	public EchoServer(int port) {
		super(port);
	}

	// Instance methods ************************************************

	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg
	 *            The message received from the client.
	 * @param client
	 *            The connection from which the message originated.
	 */
	public void handleMessageFromClient(Object packet, ConnectionToClient client) {

		Statement stmt;
		String tempSqlCommand;
		ArrayList<String> rawList = new ArrayList<String>();
		boolean queryResultEmpty = true;

		try {
			stmt = sqlConnection.createStatement();
			tempSqlCommand = ((PacketClass) packet).getSqlCommand();

			// TODO change valid sql command rule
			if (true) {
				
				ResultSet rs;
				
				if(tempSqlCommand.toLowerCase().contains("update"))
				{
					try {
					stmt.executeUpdate(tempSqlCommand);
					((PacketClass)packet).setSuccessSql(true);
					}catch(Exception ex) {
						((PacketClass)packet).setSuccessSql(false);
					}
					
				}
				else
				{
					rs = stmt.executeQuery(tempSqlCommand);
				
				while (rs.next()) {
					int i = 1;

					try {
						while (true) {
							rawList.add(rs.getString(i));
							i++;
							queryResultEmpty = false;
						}
					} catch (Exception ex) {};

					((PacketClass) packet).setResults(rawList);
				}
				

				if (queryResultEmpty && ((PacketClass)packet).getReturnWithResult())
					ServerMain.getServerPanelControl().UpdateConsol("SqlCommand " + ((PacketClass) packet).getSqlCommand() + " return empty result");
				else
					ServerMain.getServerPanelControl().UpdateConsol(
							"SqlCommand " + ((PacketClass) packet).getSqlCommand() + " succeed from " + client);
				
				((PacketClass)packet).setSuccessSql(true);
				}
				
			} else{
				ServerMain.getServerPanelControl().UpdateConsol("Invalid Sql Command");
				((PacketClass)packet).setSuccessSql(false);
			}

		} catch (Exception e) {
			ServerMain.getServerPanelControl().UpdateConsol(e.toString());
			ServerMain.getServerPanelControl().UpdateConsol("SqlCommand " + ((PacketClass) packet).getSqlCommand() + " failed from " + client);
		}

	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		ServerMain.getServerPanelControl().UpdateConsol("Server listening for connections on port " + getPort());
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		ServerMain.getServerPanelControl().UpdateConsol("Server has stopped listening for connections.");
	}

	// Class methods ***************************************************


	public static void initializeServer() {
		
		
		EchoServer sv = new EchoServer(ServerMain.getPort());

		try {
			sv.listen(); // Start listening for connections

			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
			} catch (Exception ex) {// handle the error
				ServerMain.getServerPanelControl().UpdateConsol("Error jdbc: "+ ex.toString());
			}

			try {

				// jdbc:mysql://localhost:3306/?user=root
				sqlConnection = DriverManager.getConnection("jdbc:mysql://" + ServerMain.getSqlHost()+":"+ServerMain.getSqlPort() +"/"+ServerMain.getSqlDbName(), ServerMain.getUserName(), ServerMain.getPassword());
				ServerMain.getServerPanelControl().UpdateConsol("SQL connection succeed");

			} catch (SQLException ex) {// handle any errors
				ServerMain.getServerPanelControl().UpdateConsol("SQLException: " + ex.getMessage());
				ServerMain.getServerPanelControl().UpdateConsol("SQLState: " + ex.getSQLState());
				ServerMain.getServerPanelControl().UpdateConsol("VendorError: " + ex.getErrorCode());

				try { // close server
					sv.close();
				} catch (IOException e) {
					ServerMain.getServerPanelControl().UpdateConsol(e.toString());
				}
			}

		} catch (Exception ex) {
			ServerMain.getServerPanelControl().UpdateConsol(ex.toString());

			try { // close server
				sv.close();
			} catch (IOException e) {
				ServerMain.getServerPanelControl().UpdateConsol(e.toString());
			}
		}
	}
}
// End of EchoServer class
