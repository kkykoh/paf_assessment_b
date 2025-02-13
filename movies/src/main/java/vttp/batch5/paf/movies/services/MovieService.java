package vttp.batch5.paf.movies.services;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.bson.Document;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.json.data.JsonDataSource;
import net.sf.jasperreports.pdf.JRPdfExporter;
import net.sf.jasperreports.pdf.SimplePdfExporterConfiguration;
import net.sf.jasperreports.pdf.SimplePdfReportConfiguration;
import vttp.batch5.paf.movies.bootstrap.Dataloader;
import vttp.batch5.paf.movies.models.MySQLmodel;
import vttp.batch5.paf.movies.repositories.MongoMovieRepository;
import vttp.batch5.paf.movies.repositories.MySQLMovieRepository;

@Service
public class MovieService {

  @Value("${user.name}")
    private String userName;

    @Value("${user.batch}")
    private String userBatch;

  @Autowired
  private MySQLMovieRepository sqlMovieRepository;
  @Autowired
  private MongoMovieRepository mongoMovieRepository;
  // TODO: Task 2

  public void processAndSaveMovies(List<JSONObject> movies) {
        List<MySQLmodel> mySQLModels = new ArrayList<>();

        
        for (JSONObject movie : movies) {
            MySQLmodel mySQLMovie = convertToMySQLModel(movie);
            mySQLModels.add(mySQLMovie);
        }


        int batchSize = 25;
        for (int i = 0; i < mySQLModels.size(); i += batchSize) {
            int end = Math.min(i + batchSize, mySQLModels.size());
            List<MySQLmodel> batch = mySQLModels.subList(i, end);
            sqlMovieRepository.batchInsertMovies(batch);
          }
        }

        private MySQLmodel convertToMySQLModel(JSONObject movie) {
          SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
          Date parsedDate;
          try {
              parsedDate = dateFormat.parse(movie.optString("release_date", "2000-01-01"));
          } catch (Exception e) {
              parsedDate = new Date();
          }
  
          return new MySQLmodel(
                  movie.optString("imdb_id", ""),
                  movie.optDouble("vote_average", 0.0),
                  movie.optInt("vote_count", 0),
                  parsedDate,
                  movie.optDouble("revenue", 0),
                  movie.optDouble("budget", 0),
                  movie.optInt("runtime", 0)
          );
      }
    


  // TODO: Task 3
  // You may change the signature of this method by passing any number of parameters
  // and returning any type
  public List<Document> getProlificDirectors() {
    return mongoMovieRepository.getProlificDirectors();
  }


  // TODO: Task 4
  // You may change the signature of this method by passing any number of parameters
  // and returning any type
  // public void generatePDFReport() {
  //  //first, create overall report json data source
  //  //this one is contains 2 json attribute ->  my name as a string, and my batch as string 'B'
  // String jsonString = new String(userName).concat(userBatch);

  
  // JSONObject firstDataSource = new JSONObject().put("name", userName).put("batch", userBatch);
  // InputStream firstStreamSource = new ByteArrayInputStream(firstDataSource.toString().getBytes(StandardCharsets.UTF_8));
  // JsonDataSource reportDS = new JsonDataSource(firstStreamSource);
  
    
  //   //then create the director table json data source --> my json array
  //   // this is second data sourced called director_table_dataset --> json array
  //   JsonDataSource directorsDS = new JsonDataSource(file here);

  //   //THEN CREATE THE report's parameters
  //   // put is key, value
  //   // data from sql
  //   Map<String, Object> params = new HashMap<>();
  //   params.put("DIRECTOR_TABLE_DATA", directorsDS);

  //   //load the report
  //   JasperReport report = 

    //populate the report with json data sources
    //JasperPrint jasperPrint = JasperFillManager.fillReport(report, params, reportDS);

    //generate report as pdf
    
    //JasperPrintManager.printReport(jasperPrint, true);

    // JRPdfExporter exporter = new JRPdfExporter();
    // exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
    // exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("ProlifericDirectorsReport.pdf"));

    // SimplePdfReportConfiguration reportConfig = new SimplePdfReportConfiguration();
    // reportConfig.setSizePageToContent(true);
    // reportConfig.setForceLineBreakPolicy(false);

    // SimplePdfExporterConfiguration exportConfig= new SimplePdfExporterConfiguration();
    // exportConfig.setMetadataAuthor(myname);
    // exportConfig.setEncrypted(true);
    // exportConfig.setAllowedPermissionsHint("printing now...");

    // exporter.setConfiguration(reportConfig);
    // exporter.setConfiguration(exportConfig);
  //}

}
