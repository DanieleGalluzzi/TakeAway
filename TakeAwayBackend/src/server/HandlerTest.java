package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import database.GestoreDatabase;
import database.Query;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Base64;

public class HandlerTest  implements HttpHandler{

	private GestoreDatabase gestoreDatabase;
	
	public HandlerTest(GestoreDatabase gestoreDatabase) {
		this.gestoreDatabase = gestoreDatabase;
	}
	
	@Override
	public void handle(HttpExchange exchange)  throws IOException {
		try {
			
			StringBuilder html = new StringBuilder();
			
			html.append("""
					<html>
					<head>
					<meta charset="UTF-8">
					<title> Test </title>
					</head>
					<body>
					<h2> Test Immagini </h2>
					"""
					);
			
			Connection conn = gestoreDatabase.getConnessione();
			PreparedStatement ps = conn.prepareStatement(Query.SELECT_TUTTI_ID);
			System.out.println("ok 1");

			ResultSet rs = ps.executeQuery();
			System.out.println("ok 2");
			while (rs.next()) {
				System.out.println("ok");
			    String id = rs.getString("id");
			    byte[] img = rs.getBytes("immagine");

			    if (img == null || img.length == 0) {
			    	System.out.println("ID: " + id + " | IMG bytes: " + img.length);
			        continue;
			    }

			    String base64 = Base64.getEncoder().encodeToString(img);

			    html.append("<div style=\"margin-bottom:20px\">")
			      .append("<img alt=\"img ")
			      .append(id)
			      .append("\" src=\"data:image/png;base64,")
			     .append(base64)
			     .append("\" style=\"max-width:300px\"/>")
			     .append("</div>");
			}

			
			html.append("""
					</body>
					</html>
					""");
			
			byte[] response = html.toString().getBytes(StandardCharsets.UTF_8);
			
			exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
			exchange.sendResponseHeaders(200, response.length);
			
			OutputStream os = exchange.getResponseBody();
			os.write(response);
			os.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
}
