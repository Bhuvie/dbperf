

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.annotation.WebServlet;
/**
 * Created by Bhuvie on 3/26/2017.
 */
@WebServlet("/checkperfrds")
public class checkperfrds extends javax.servlet.http.HttpServlet {
    private static final String url = "jdbc:mysql://bhuviedbi.cwyiughlbpf0.us-west-2.rds.amazonaws.com:3306/bhuviedb";
    private static final String user = "bhuvie93";
    private static final String pass = "###############";

    //Statement DataBaseStatement;
    Connection con;
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

        //ArrayList<String> teamlist =new ArrayList<String>();
        String noqtimes=request.getParameter("txtquerytimesrds");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, user, pass);
            //DataBaseStatement = con.createStatement();
            PreparedStatement ps = con.prepareStatement("select * from bhuviedb.networktraffic" );
            //ps.setString(1,"");
            ResultSet rs;
            long timebefore=System.currentTimeMillis();

            for (int i = 0; i < Integer.parseInt(noqtimes); i++) {
                rs = ps.executeQuery();
                String arr = "";
                while (rs.next()) {
                    arr =  arr+rs.getString("date");
                    System.out.println(arr);
                }
            }

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
        catch (Exception e) {
            e.printStackTrace();
//            PrintWriter pw = response.getWriter().append(e.toString());
//            pw.close();
        }


    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

        doPost(request,response);
    }


}
