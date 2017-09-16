import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;

/**
 * Created by Bhuvie on 4/3/2017.
 */
@WebServlet("/createtable")
public class createtable extends HttpServlet {
    private static final String url = "jdbc:mysql://bhuviedbi.cwyiughlbpf0.us-west-2.rds.amazonaws.com:3306/bhuviedb";
    private static final String user = "bhuvie93";
    private static final String pass = "############";

    Connection con;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //String noqtimes=request.getParameter("txtquerytimesrdslimited");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, user, pass);
            PreparedStatement ps;
            long timebefore=System.currentTimeMillis();
            Scanner scanner = new Scanner(new File("/home/ubuntu/Desktop/"+request.getParameter("filename")));
            String fields=scanner.nextLine();
            fields=fields.replaceAll(","," varchar(20), ")+" varchar(20));";
            //String createquery="create table bhuviedb.UNPrecip(Country varchar(50), StationName varchar(50),WMOStationNumber varchar(20),Unit varchar(20),Jan varchar(20),Feb	varchar(20),Mar	varchar(20),Apr	varchar(20),May	varchar(20),Jun	varchar(20),Jul	varchar(20),Aug	varchar(20),Sep	varchar(20),Oct	varchar(20),Nov	varchar(20),Decem varchar(20));";
            String createquery="create table bhuviedb.UNPrecip( "+fields;
            //createquery.replace("Dec","Decem");
            ps=con.prepareStatement(createquery);
            ps.executeUpdate();
            ps.close(); ///home/ubuntu/Desktop/
            PreparedStatement ps1=con.prepareStatement("LOAD DATA LOCAL INFILE '/home/ubuntu/Desktop/"+request.getParameter("filename")+"' \n" +
                    "INTO TABLE UNPrecip  \n" +
                    "    FIELDS TERMINATED BY ',' \n" +
                    "           OPTIONALLY ENCLOSED BY '\"'\n" +
                    "    LINES  TERMINATED BY '\\n' "+" IGNORE 1 LINES");
            ps1.executeUpdate();
            long timeafter=System.currentTimeMillis();
            long timetaken=timeafter-timebefore;
            PrintWriter pw = response.getWriter().append(Long.toString(timetaken));
            pw.close();
            ps1.close();
            con.close();


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doPost(request,response);
    }
}
