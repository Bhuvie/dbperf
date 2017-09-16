import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.FailureMode;
import net.spy.memcached.MemcachedClient;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

/**
 * Created by Bhuvie on 4/4/2017.
 */
@WebServlet("/checkperfmc5kspecfic")
public class checkperfmc5kspecfic extends javax.servlet.http.HttpServlet {
    private static final String url = "jdbc:mysql://bhuviedbi.cwyiughlbpf0.us-west-2.rds.amazonaws.com:3306/bhuviedb";
    private static final String user = "bhuvie93";
    private static final String pass = "############";
    MemcachedClient mc;
    Connection con;

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

        Memcache();
        int noofrows=0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, user, pass);
            PreparedStatement ps = con.prepareStatement("select * from bhuviedb.UNPrecip where GivenName='"+request.getParameter("givenname")+"' AND Age BETWEEN "+request.getParameter("age1")+" AND "+request.getParameter("age2")+";" );
            ResultSet rs;
            long timebefore = System.currentTimeMillis();
            String data = (String) mc.get("mykey");
            if (data == null ) {
                data = (String) mc.get("mykey");
                rs = ps.executeQuery();
                String arr = "";
                rs.last();noofrows=rs.getRow();rs.beforeFirst();
                while (rs.next()) {
                    arr = arr + "\n" + rs.getString("GivenName");
                    //System.out.println("Row : "+arr);
                }
                mc.set("mykey", 10000, arr);
            }
            for (int i = 0; i < 5000; i++) {
                data = (String) mc.get("mykey");

            }

            long timeafter = System.currentTimeMillis();
            long timetaken = timeafter - timebefore;
            con.close();
            mc.flush();
            PrintWriter pw = response.getWriter().append("No. of Matches: "+noofrows+" Time Taken: "+Long.toString(timetaken));
            pw.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doPost(request, response);
    }

    public void Memcache() {
        try {
            mc = new MemcachedClient(new ConnectionFactoryBuilder()
                    .setProtocol(ConnectionFactoryBuilder.Protocol.BINARY).setDaemon(true).setFailureMode(FailureMode.Retry).build(), AddrUtil.getAddresses("dbccdemo.uwgzbd.cfg.usw2.cache.amazonaws.com:11211"));
        } catch (IOException ioe) {
            System.err.println("Couldn't create a connection to MemCachier: \nIOException " + ioe.getMessage());
        }
    }
}