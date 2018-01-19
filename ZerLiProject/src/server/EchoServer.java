package server;
// This file contains material supporting section 3.7 of the textbook:

// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import ocsf.server.*;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import clientServerCommon.MyFile;
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

		boolean queryResultEmpty = true;

		try {
			stmt = sqlConnection.createStatement();
			tempSqlCommand = ((PacketClass) packet).getSqlCommand();

			// if (true) {

			ResultSet rs;

			if ((tempSqlCommand.toLowerCase().startsWith("update") || tempSqlCommand.toLowerCase().startsWith("insert into")
					|| tempSqlCommand.toLowerCase().startsWith("delete"))
					&& !((PacketClass) packet).getReturnWithResult()) {
				try {
					stmt.executeUpdate(tempSqlCommand);
					((PacketClass) packet).setSuccessSql(true);
					ServerMain.getServerPanelControl().UpdateConsol(
							"SqlCommand " + ((PacketClass) packet).getSqlCommand() + " succeed from " + client);
				} catch (Exception ex) {
					
					if(((PacketClass) packet).fileList.size() != 0)
					{
						try {
							PreparedStatement statement = sqlConnection.prepareStatement(tempSqlCommand);
							InputStream is = new ByteArrayInputStream(((PacketClass) packet).fileList.get(0).getMybytearray());
							statement.setBlob(1, is);
							statement.setString(2, ((PacketClass) packet).fileList.get(0).getColumName());
							statement.executeUpdate();
							((PacketClass) packet).setSuccessSql(true);
						} catch (Exception e) {
							((PacketClass) packet).setSuccessSql(false);
							ServerMain.getServerPanelControl().UpdateConsol(e.toString());
							ServerMain.getServerPanelControl().UpdateConsol(
									"SqlCommand " + ((PacketClass) packet).getSqlCommand() + " failed from " + client);
						}
					}
					else {
					((PacketClass) packet).setSuccessSql(false);
					ServerMain.getServerPanelControl().UpdateConsol(ex.toString());
					ServerMain.getServerPanelControl().UpdateConsol(
							"SqlCommand " + ((PacketClass) packet).getSqlCommand() + " failed from " + client);
					}
				}
				

			} else if(((PacketClass) packet).getReturnWithResult() && tempSqlCommand.toLowerCase().startsWith("select")) {
				rs = stmt.executeQuery(tempSqlCommand);
				ResultSetMetaData rsmd = rs.getMetaData();

				while (rs.next()) {
					int i = 1;
					ArrayList<String> rawList = new ArrayList<String>();

					try {
						while (true) {
							if (rsmd.getColumnType(i) != -4) {
								rawList.add(rs.getString(i));
							} else {
								try {
									Blob blob = rs.getBlob(i);
									MyFile imageFile = new MyFile(null);
									imageFile.initArray((int) blob.length());
									imageFile.setMybytearray(blob.getBytes(1, (int) blob.length()));
									((PacketClass) packet).fileList.add(imageFile);
								}catch(Exception e) {
									ServerMain.getServerPanelControl().UpdateConsol(
											"Failed insert file into packet for " + client);
								}
							}

							i++;
							queryResultEmpty = false;
						}
					} catch (Exception ex) {
						// ex.printStackTrace();
					}
					;

					((PacketClass) packet).setResults(rawList);
				}

				if (queryResultEmpty && ((PacketClass) packet).getReturnWithResult())
					ServerMain.getServerPanelControl().UpdateConsol(
							"SqlCommand " + ((PacketClass) packet).getSqlCommand() + " return empty result");
				else
					ServerMain.getServerPanelControl().UpdateConsol(
							"SqlCommand " + ((PacketClass) packet).getSqlCommand() + " succeed from " + client);

				((PacketClass) packet).setSuccessSql(true);
			}

			/*
			 * } else{
			 * ServerMain.getServerPanelControl().UpdateConsol("Invalid Sql Command");
			 * ((PacketClass)packet).setSuccessSql(false); }
			 */

		} catch (Exception e) {
			((PacketClass) packet).setSuccessSql(false);
			ServerMain.getServerPanelControl().UpdateConsol(e.toString());
			ServerMain.getServerPanelControl()
					.UpdateConsol("SqlCommand " + ((PacketClass) packet).getSqlCommand() + " failed from " + client);
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
				ServerMain.getServerPanelControl().UpdateConsol("Error jdbc: " + ex.toString());
			}

			try {

				// jdbc:mysql://localhost:3306/?Login=root
				sqlConnection = DriverManager
						.getConnection(
								"jdbc:mysql://" + ServerMain.getSqlHost() + ":" + ServerMain.getSqlPort() + "/"
										+ ServerMain.getSqlDbName(),
								ServerMain.getUserName(), ServerMain.getPassword());
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
