package vttp.batch5.paf.movies.controllers;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vttp.batch5.paf.movies.services.MovieService;

@RestController
@RequestMapping
public class MainController {

  @Autowired
  private MovieService movieService;
  // TODO: Task 3
  

    @GetMapping("/api/summary")
    public ResponseEntity<List<Document>> getProlificDirectors(@RequestParam int count) {

        List<Document> getDirectors = movieService.getProlificDirectors();


        if (getDirectors.size() > count) {
            getDirectors = getDirectors.subList(0, count);
        }

        return ResponseEntity.ok(getDirectors);
    }



  
  // TODO: Task 4
//     @GetMapping("/api/summary/pdf")
//     public ResponseEntity<T> printPDF(@RequestParam int count) {
//       movieService.generatePDFReport();
//       return ResponseEntity.ok().body(null);
//       // .headers(some headers)
//       // .contentLength(file name.length())
//       // .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
//       // .body(body here);
//     }
}