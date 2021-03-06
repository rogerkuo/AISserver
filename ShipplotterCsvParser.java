import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import java.util.Date;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.*;
public class ShipplotterCsvParser
{
  private final String USER_AGENT = "Mozilla/5.0";

  public static void main(String[] args) throws Exception
  {
    ShipplotterCsvParser http = new  ShipplotterCsvParser();
    String inputFileName = args[0];
    File inputFile =new File(inputFileName);
    Scanner in = new Scanner(inputFile);

    while (in.hasNext())
    {
      String ln = in.nextLine();
//      System.out.println(ln);
      http.uploadToDb(ln);
//      System.out.println(output[i]);
    }
  }


  private void uploadToDb(String line) throws Exception
  {
    Statement statement = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    try
      {
        Class.forName("com.mysql.jdbc.Driver");
      }
      catch (ClassNotFoundException e) {
        System.out.println("MySQL JDBC Driver not found !!");
        return;
      }

      System.out.println("MySQL JDBC Driver Registered!");
      Connection connection = null;
      Statement stmt = null;

    //connect and upload data
    try {
      connection = DriverManager.getConnection("jdbc:mysql://localhost/shipplotter", "shipplotter", "shipplotter");
      statement = connection.createStatement();
      System.out.println("SQL Connection to database established!");
      resultSet = statement.executeQuery("select * from shipplotter");
//    writeResultSet(resultSet);
      String[] la = line.split(",");
      System.out.printf("length is %d", la.length);
      System.out.println(line);
      System.out.println("sssssssssssssssssssssssssss");
      System.out.printf("this is mmsi ----%s---", la[0]);
      System.out.println("sssssssssssssssssssssssssss");
//      System.out.println("this was mmsi");

//      int mmsi = Integer.parseInt(la[0]);
//      System.out.printf("this is the mmsi as in %d", mmsi);
      preparedStatement = connection.prepareStatement("INSERT IGNORE INTO shipplotter values (?, ?, ?, ? , ?, ?, ?, ?, ?, ? , ?, ?, ?, ?, ?, ? , ?, ?, ?, ?, ?, ?)");
      preparedStatement.setString(1, la[0]);
      preparedStatement.setString(2, la[1]);
      preparedStatement.setString(3, la[2]);
      preparedStatement.setString(4, la[3]);
      preparedStatement.setString(5, la[4]);
      preparedStatement.setString(6, la[5]);
      preparedStatement.setString(7, la[6]);
      preparedStatement.setString(8, la[7]);
      preparedStatement.setString(9, la[8]);
      preparedStatement.setString(10, la[9]);
      preparedStatement.setString(11, la[10]);
      preparedStatement.setString(12, la[11]);
      preparedStatement.setString(13, la[12]);
      preparedStatement.setString(14, la[13]);
      preparedStatement.setString(15, la[14]);
      preparedStatement.setString(16, la[15]);
      preparedStatement.setString(17, la[16]);
      preparedStatement.setString(18, la[17]);
      preparedStatement.setString(19, la[18]);
      preparedStatement.setString(20, la[19]);
      preparedStatement.setString(21, la[20]);
      preparedStatement.setString(22, la[21]);
      preparedStatement.executeUpdate();

    } catch (SQLException e) {
      System.out.println("Connection Failed! Check output console");
      System.out.println(e);
      return;
    } finally {
      try 
      {
        if(connection != null)
          connection.close();
          System.out.println("Connection closed !!");
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
}


// HTTP POST request
public String[] sendPost() throws Exception {
  Date date = new Date();
//  String filename = "shiplog.csv";
  String url = "http://www.coaa.co.uk/shipinfo.php";
  URL obj = new URL(url);
  HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//  Writer out = new FileWriter(filename, true);
  ArrayList<String> thisout = new ArrayList<String>();
  //add reuqest header
  con.setRequestMethod("POST");
  con.setRequestProperty("User-Agent", USER_AGENT);
  con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

  String urlParameters = "Lines=0&Reg=57470&Extracode=48&Uponly=0&Mouset=86&LatN=+56.518685&LatS=+55.553905&LonE=++14.464614&LonW=++10.767836&Fwd=0&Ver=12.4.8&Tt=1399648978&Desig=219001684";

  // Send post request
  con.setDoOutput(true);
  DataOutputStream wr = new DataOutputStream(con.getOutputStream());
  wr.writeBytes(urlParameters);
  wr.flush();
  wr.close();

  int responseCode = con.getResponseCode();

  BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
  String ln;
  StringBuffer response = new StringBuffer();
  while ((ln = in.readLine()) != null) {
    if ((ln.charAt(26) == ' '))
      {
        ln = (ln.substring(0, 23) + " 0" + ln.substring(24));
      }
    ln = ("\n" + ln.substring(0, 9) + "," + ln.substring(10, 20) + "," + ln.substring(21, 23) + "," + ln.substring(24, 27) + "," +
          ln.substring(29, 38) + "," + ln.substring(41, 50) + "," + ln.substring(51, 56) + "," + ln.substring(57,62) + "," +
          ln.substring(63, 66) + "," + ln.substring(67, 71) + "," + ln.substring(72, 75) + "," + ln.substring(76, 78) + "," + 
          ln.substring(79, 99) + "," + ln.substring(100, 107) + "," + ln.substring(108, 128) + "," + ln.substring(129,134) + "," + 
          ln.substring(135, 140) + "," + ln.substring(141, 142) + "," + ln.substring(143, 150) + "," + ln.substring(151,154) + "," + 
          ln.substring(155, 157) + "," + ln.substring(158, 159));

//    out.write(ln);
    thisout.add(ln);
    //String newInputLine = inputLine.replace("  ", "");
//    System.out.print(ln);
  }
  in.close();
//  out.flush();
//  out.close();

  String[] returnArray = new String[thisout.size()];
  returnArray = thisout.toArray(returnArray);
  return returnArray;

  //print result
}

}
