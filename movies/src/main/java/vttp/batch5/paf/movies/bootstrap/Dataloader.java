package vttp.batch5.paf.movies.bootstrap;

import org.json.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import vttp.batch5.paf.movies.models.MySQLmodel;
import vttp.batch5.paf.movies.services.MovieService;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import java.nio.charset.StandardCharsets;


@Component
public class Dataloader implements CommandLineRunner {

    @Autowired
    private MovieService movieService;
    // @Value("${zipFilePath}")
    // private String zipfilePath;
    // @Value("${jsonFileName}")
    // private String jsonFileName;
    
    // TODO: Task 2

    @Override
    public void run(String... args) throws Exception {
        
        
        String zipFilePath = "../data/movies_post_2010.zip";
        String jsonFileName = "movies_post_2010.json";

        
        System.out.println("filtering movies -> start...");
        //List<MySQLmodel> filteredMoviesforSQL = filterMoviesFromZipForSQL(zipFilePath, jsonFileName);
        filterMoviesFromZip(zipFilePath, jsonFileName);
        System.out.println(" filtering movies -> end ....");
    }

    public static void filterMoviesFromZip(String zipFilePath, String jsonFileName) {
            try (ZipFile zipFile = new ZipFile(zipFilePath)) {
                ZipEntry jsonFileEntry = zipFile.getEntry(jsonFileName);
                if (jsonFileEntry == null) {
                    System.err.println("error: json file not found in the zip.");
                    return;
                }
    
                try (InputStream inputStream = zipFile.getInputStream(jsonFileEntry);
                     BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
    
                    List<JSONObject> filteredMovies = new ArrayList<>();
                    String line;
    
                    while ((line = reader.readLine()) != null) {
                        try {
                            JSONObject movie = new JSONObject(line.trim());
    
                        // Get release date and filter based on it
                        String releaseDate = movie.optString("release_date", "");
                        if (releaseDate.isEmpty()) {
                            continue; 
                        }

                        
                        int year = extractYearFromDate(releaseDate);
                        if (year >= 2018) {
                            filteredMovies.add(sanitizeMovieData(movie));
                        } 
                    } catch (JSONException e) {
                    System.err.println("Skipping bad JSON: " + line);
                    }
                    
                    

                }

                
                //System.out.println("filtered movies: " + filteredMovies.size());
                System.out.println("filtered movies list: " + filteredMovies);
                System.out.println("filtered movies: " + filteredMovies.size());

            } catch (IOException | JSONException e) {
                System.out.println("error processing the json file: " + e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("error reading the zip file: " + e.getMessage());
        }
    }
    
    private static int extractYearFromDate(String releaseDate) {
        try {
            return Integer.parseInt(releaseDate.substring(0, 4));
        } catch (Exception e) {
            return 0; 
        }
    }

    private static JSONObject sanitizeMovieData(JSONObject movie) {
    JSONObject cleanedMovieData = new JSONObject();

    cleanedMovieData.put("title", movie.optString("title", ""));
    cleanedMovieData.put("vote_average", movie.optInt("vote_average", 0));
    cleanedMovieData.put("vote_count", movie.optInt("vote_count", 0));
    cleanedMovieData.put("status", movie.optString("status", ""));
    cleanedMovieData.put("release_date", movie.optString("release_date", ""));
    cleanedMovieData.put("revenue", movie.optFloat("revenue", 0));
    cleanedMovieData.put("runtime", movie.optInt("runtime", 0));
    cleanedMovieData.put("budget", movie.optFloat("budget", 0));
    cleanedMovieData.put("imdb_id", movie.optString("imdb_id", ""));
    cleanedMovieData.put("original_language", movie.optString("original_language",""));
    cleanedMovieData.put("overview", movie.optString("overview", ""));
    cleanedMovieData.put("popularity", movie.optInt("popularity", 0));
    cleanedMovieData.put("tagline", movie.optString("tagline", ""));
    cleanedMovieData.put("genres", movie.optString("genres", ""));
    cleanedMovieData.put("spoken_languages", movie.optString("spoken_languages",""));
    cleanedMovieData.put("casts", movie.optString("casts", ""));
    cleanedMovieData.put("director", movie.optString("director", ""));
    cleanedMovieData.put("imdb_rating", movie.optInt("imdb_rating", 0));
    cleanedMovieData.put("imdb_votes", movie.optInt("imdb_votes", 0));
    cleanedMovieData.put("poster_path", movie.optString("poster_path", ""));

    return cleanedMovieData;
    }


    // public List<MySQLmodel> filterMoviesFromZipForSQL(String zipFilePath, String jsonFileName) {
    //     List<MySQLmodel> moviesList = new ArrayList<>();

    //     try (ZipFile zipFile = new ZipFile(zipFilePath)) {
    //         ZipEntry jsonFileEntry = zipFile.getEntry(jsonFileName);
    //         if (jsonFileEntry == null) {
    //             System.err.println("Error: JSON file not found in the zip.");
    //             return moviesList;
    //         }

    //         try (InputStream inputStream = zipFile.getInputStream(jsonFileEntry);
    //              BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

    //             String line;
    //             while ((line = reader.readLine()) != null) {
    //                 try {
    //                     JSONObject movie = new JSONObject(line.trim());

    //                     // Extract and validate release date
    //                     String releaseDate = movie.optString("release_date", "");
    //                     if (releaseDate.isEmpty()) continue;

    //                     int year = extractYearFromDate(releaseDate);
    //                     if (year >= 2018) {
    //                         MySQLmodel mysqlMovie = convertToMySQLModel(movie);
    //                         moviesList.add(mysqlMovie);
    //                     }

    //                 } catch (JSONException e) {
    //                     System.err.println("Skipping bad JSON: " + line);
    //                 }
    //             }

    //         } catch (IOException | JSONException e) {
    //             System.err.println("Error processing the JSON file: " + e.getMessage());
    //         }
    //     } catch (IOException e) {
    //         System.err.println("Error reading the ZIP file: " + e.getMessage());
    //     }

    //     return moviesList;
    // }


    // private static MySQLmodel convertToMySQLModel(JSONObject movie) {
    //     SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    //     Date parsedDate;
    //     try {
    //         parsedDate = dateFormat.parse(movie.optString("release_date", "2000-01-01"));
    //     } catch (Exception e) {
    //         parsedDate = new Date();
    //     }

    //     return new MySQLmodel(
    //             movie.optString("imdb_id", ""),
    //             movie.optDouble("vote_average", 0.0),
    //             movie.optInt("vote_count", 0),
    //             parsedDate,
    //             movie.optDouble("revenue", 1000000.00),
    //             movie.optDouble("budget", 1000000.00),
    //             movie.optInt("runtime", 90)
    //     );
    // }
}

   

