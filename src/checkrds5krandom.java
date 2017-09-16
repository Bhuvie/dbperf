

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.concurrent.ThreadLocalRandom;
import javax.servlet.annotation.WebServlet;
/**
 * Created by Bhuvie on 3/26/2017.
 */
@WebServlet("/checkrds5krandom")
public class checkrds5krandom extends javax.servlet.http.HttpServlet {
    private static final String url = "jdbc:mysql://bhuviedbi.cwyiughlbpf0.us-west-2.rds.amazonaws.com:3306/bhuviedb";
    private static final String user = "bhuvie93";
    private static final String pass = "###########";

    Connection con;
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

        String noqtimes=request.getParameter("txtquerytimesrdsrandom");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, user, pass);
            PreparedStatement ps; //= con.prepareStatement("select * from bhuviedb.networktraffic where ?=?" );
            ResultSet rs;


            int randomFNum,randomVNum;
            String[] fieldName={"date","l_ipn","r_asn","f"};
            String paramName;
            String[][] fieldValues={{"7/2/2006","7/23/2006","8/6/2006","7/28/2006"},{"0","1","2","3"},{"3","4","8","9"},{"1","2","3","4"}};

            long timebefore=System.currentTimeMillis();


            for (int i = 0; i < Integer.parseInt(noqtimes); i++)
            {
                randomFNum = ThreadLocalRandom.current().nextInt(0,fieldName.length);
                paramName=fieldName[randomFNum];
                String query="select * from bhuviedb.networktraffic where "+paramName+"=?";
                ps = con.prepareStatement(query);

                randomVNum = ThreadLocalRandom.current().nextInt(0, fieldValues[randomFNum].length);
                ps.setString(1, fieldValues[randomFNum][randomVNum]);
//                switch(randomFNum)
//                {
//                    case 0:
//                        randomVNum = ThreadLocalRandom.current().nextInt(0,fieldValues[0].length);
//                        ps.setString(1,fieldValues[randomFNum][randomVNum]);
//                        break;
//                    case 1:
//                        randomVNum = ThreadLocalRandom.current().nextInt(0,fieldValues[1].length);
//                        ps.setInt(1,Integer.parseInt(fieldValues[randomFNum][randomVNum]));
//                        break;
//                    case 2:
//                        randomVNum = ThreadLocalRandom.current().nextInt(0,fieldValues[2].length);
//                        ps.setInt(1,Integer.parseInt(fieldValues[randomFNum][randomVNum]));
//                        break;
//                    case 3:
//                        randomVNum = ThreadLocalRandom.current().nextInt(0,fieldValues[3].length);
//                        ps.setInt(1,Integer.parseInt(fieldValues[randomFNum][randomVNum]));
//                        break;
//                    default:randomVNum=99;break;
//                }
                //System.out.println("RandomFNum: "+randomFNum+" RandomVNum: "+randomVNum);
                rs = ps.executeQuery();

                //rs.last();System.out.println("Rows:"+rs.getRow());rs.beforeFirst();

                String arr = "";
                while (rs.next()) {
                    arr =  rs.getString("date")+rs.getString("l_ipn")+rs.getString("r_asn")+rs.getString("f");
                    //System.out.println("Row: "+arr);
                }
                ps.close();

            }

            long timeafter=System.currentTimeMillis();
            long timetaken=timeafter-timebefore;
            con.close();
            PrintWriter pw = response.getWriter().append(Long.toString(timetaken));
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

        doPost(request,response);
    }


}
