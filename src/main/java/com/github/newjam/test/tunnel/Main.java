package com.github.newjam.test.tunnel;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
  
  private final static Logger log = LoggerFactory.getLogger(Main.class);
  
  public static void main(String...args) throws IOException, ClassNotFoundException, SQLException {
    ExecutorService service = Executors.newSingleThreadExecutor();
      final String _DATABASE_URL = "178.62.194.56";
      final String _DATABASE_USERNAME = "root";
      final String _DATABASE_PASSWORD = "357535";
      final String _REMOTE_HOST = "178.62.194.56";
      final int _REMOTE_PORT = 3306;
      final String _HOST_USERNAME= "root";                  // SSH loging username
      final String _HOST_PASSWORD = "4438krh-*";
      final int _LOCAL_PORT = 22;

      try(Tunnel tunnel = new Tunnel(_REMOTE_HOST,_HOST_USERNAME,_HOST_PASSWORD,_REMOTE_PORT,_LOCAL_PORT)) {
    
      service.execute(tunnel);

      tunnel.waitForConnection();

      Class.forName("com.mysql.jdbc.Driver");



      Connection conn = DriverManager.getConnection(_DATABASE_URL, _DATABASE_USERNAME, _DATABASE_PASSWORD);

      Statement stmt = conn.createStatement();

      final String APPROVED_CLIP_COUNT_QUERY = "SELECT bolumAdi,tanitim FROM db44.meslek_bilgisi";

      ResultSet result = stmt.executeQuery(APPROVED_CLIP_COUNT_QUERY);
      result.beforeFirst();
      result.next();

      long approvedClipCount = result.getLong("approved_clip_count");

      log.debug("There are {} approved clips.", approvedClipCount);
    
    } finally {
      service.shutdownNow();
    }
    
  }
}
