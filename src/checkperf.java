import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.FailureMode;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.auth.AuthDescriptor;
import net.spy.memcached.auth.PlainCallbackHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.concurrent.CancellationException;
import javax.servlet.annotation.WebServlet;
/**
 * Created by Bhuvie on 3/26/2017.
 */
@WebServlet("/checkperf")
public class checkperf extends javax.servlet.http.HttpServlet {
    private static final String url = "jdbc:mysql://bhuviedbi.cwyiughlbpf0.us-west-2.rds.amazonaws.com:3306/bhuviedb";
    private static final String user = "bhuvie93";
    private static final String pass = "**************";
    MemcachedClient mc;
    //Statement DataBaseStatement;
     Connection con;
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

        //ArrayList<String> teamlist =new ArrayList<String>();
        String noqtimes=request.getParameter("txtquerytimes");
        Memcache();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, user, pass);
            //DataBaseStatement = con.createStatement();
            PreparedStatement ps = con.prepareStatement("select * from bhuviedb.networktraffic " );
            //ps.setString(1,"");
            ResultSet rs;
            long timebefore=System.currentTimeMillis();
            String cond;
            cond = (String) mc.get("mykey");

            if(cond==null)
                 {
                    rs = ps.executeQuery();
                    String arr = "";
                    while (rs.next()) {
                        arr =  arr+rs.getString("date");
                        //System.out.println(arr);
                    }
                     mc.set("mykey",10000,arr);
                }
            String a;
            for (int i = 0; i < Integer.parseInt(noqtimes); i++)
            {
                a=(String) mc.get("mykey");
                System.out.println("Out: "+a);
            }


                 mc.flush();
            long timeafter=System.currentTimeMillis();
            long timetaken=timeafter-timebefore;
            con.close();
            PrintWriter pw = response.getWriter().append(Long.toString(timetaken));
            pw.close();
//            while(rs.next())
//            {
//                String tname=rs.getString("teamname");
//                teamlist.add(tname);
//            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
//            PrintWriter pw = response.getWriter().append(e.toString());
//            pw.close();
        } catch (SQLException e) {
            e.printStackTrace();
//            PrintWriter pw = response.getWriter().append(e.toString());
//            pw.close();
        }


    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

        doPost(request,response);
    }

    public void Memcache()
    {
        //AuthDescriptor ad = new AuthDescriptor(new String[] { "PLAIN" },
          //      new PlainCallbackHandler("AKIAJTC6XAIDKJDZQTPQ", "Iv+ec5wr5iOjw9QTYQTuNWHBPp3StRlQ2g0yomLw"));

        try {
            mc = new MemcachedClient(new ConnectionFactoryBuilder()
                    .setProtocol(ConnectionFactoryBuilder.Protocol.BINARY).setDaemon(true).setFailureMode(FailureMode.Retry).build(), AddrUtil.getAddresses("dbccdemo.uwgzbd.cfg.usw2.cache.amazonaws.com:11211"));
//            mc.set("mykey",10000,arr);


            //System.out.println(mc.get("foo"));
        } catch (IOException ioe) {
            System.err.println("Couldn't create a connection to MemCachier: \nIOException "
                    + ioe.getMessage());
        }
    }
}
