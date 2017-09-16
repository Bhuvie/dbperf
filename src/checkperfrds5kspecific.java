
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bhuvie on 4/4/2017.
 */
@WebServlet("/checkperfrds5kspecific")
public class checkperfrds5kspecific extends javax.servlet.http.HttpServlet {
    private static final String url = "jdbc:mysql://bhuviedbi.cwyiughlbpf0.us-west-2.rds.amazonaws.com:3306/bhuviedb";
    private static final String user = "bhuvie93";
    private static final String pass = "#############";
//    Map<String, String> colnames = new HashMap<String, String>();

    Connection con;
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {


//        colnames.put("A", "Country");
//        colnames.put("B", "StationName");
//        colnames.put("C", "WMOStationNumber");
//        colnames.put("D", "Unit");
//        colnames.put("E", "Jan");
//        colnames.put("F", "Feb");
//        colnames.put("G", "Mar");
//        colnames.put("H", "Apr");
//        colnames.put("I", "May");
//        colnames.put("J", "Jun");
//        colnames.put("K", "Jul");
//        colnames.put("L", "Aug");
//        colnames.put("M", "Sep");
//        colnames.put("N", "Oct");
//        colnames.put("O", "Nov");
//        colnames.put("P", "Decem");
        int noofrows=0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, user, pass);
            PreparedStatement ps = con.prepareStatement("select * from bhuviedb.UNPrecip where GivenName='"+request.getParameter("givenname")+"' AND Age BETWEEN "+request.getParameter("age1")+" AND "+request.getParameter("age2")+";" );
            ResultSet rs;
            long timebefore=System.currentTimeMillis();

            for (int i = 0; i < 5000; i++) {
                rs = ps.executeQuery();
                rs.last();noofrows=rs.getRow();rs.beforeFirst();
//                String arr = "";
//                while (rs.next()) {
//                    arr = arr + "\n" + rs.getString("Country") + rs.getString("StationName");
//                    System.out.println("Row: "+arr);
//                }

            }
            long timeafter=System.currentTimeMillis();
            long timetaken=timeafter-timebefore;
            con.close();
            PrintWriter pw = response.getWriter().append("No. of Matches: "+noofrows+" Time Taken: "+Long.toString(timetaken));
            pw.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doPost(request,response);
    }
}
