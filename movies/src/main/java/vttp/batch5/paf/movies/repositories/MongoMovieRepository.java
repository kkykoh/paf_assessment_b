package vttp.batch5.paf.movies.repositories;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.mongodb.client.result.InsertManyResult;

import java.io.*;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.stereotype.Repository;

import vttp.batch5.paf.movies.models.MongoDBmodel;

@Repository
public class MongoMovieRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    // TODO: Task 2.3
    // You can add any number of parameters and return any type from the method
    // You can throw any checked exceptions from the method
    // Write the native Mongo query you implement in the method in the comments
    //
    // native MongoDB query here
    // let movieBatch = [
    // {
    // "title": "The Cabin in the Woods",
    // "imdb_id": "tt1259521",
    // "overview": "A group of teens journey to a remote cabin in the woods where
    // their fate is unknowingly controlled by technicians as part of a worldwide
    // conspiracy where all horror movie clichés are revealed to be part of an
    // elaborate sacrifice ritual.",
    // "tagline": "You think you know the story.",
    // "director": "Drew Goddard",
    // "imdb_rating": 7,
    // "imdb_votes": 465176,
    // }
    //
    // ];
    // db.imdb.insertMany(movieBatch);
    //
    // public void batchInsertMovies(List<MongoDBmodel> movies) {
    //     
    //     try {
    //         // Insert movies in batches of 25
    //         int batchSize = 25;
    //         for (int i = 0; i < movies.size(); i += batchSize) {
    //             int end = Math.min(i + batchSize, movies.size());
    //             List<MongoDBmodel> batch = movies.subList(i, end);
                
    //             
    //             Collection<MongoDBmodel> result = mongoTemplate.insertAll(batch);
    //             System.out.println("Inserted batch of " + batch.size() + " movies into MongoDB.");
    //         }
    //     } catch (Exception e) {
    //         System.err.println("Error inserting batch of movies into MongoDB: " + e.getMessage());
    //     }
    // }
 
// TODO: Task 2.4
        // You can add any number of parameters and return any type from the method
    // You can throw any checked exceptions from the method
    // Write the native Mongo query you implement in the method in the comments
    //
    // native MongoDB query here
    //
    public void logError() {

    }

    // TODO: Task 3
    // Write the native Mongo query you implement in the method in the comments
    //
    // native MongoDB query here
    //     db.getCollection("imdb").aggregate([
    //     {
    //         $group: {
    //             _id: "$director",  
    //             movies_count: { $sum: 1 },  
    //             total_revenue: { $sum: "$revenue" },  
    //             total_budget: { $sum: "$budget" }   
    //         }
    //     },
    //     {
    //         $project: {
    //             movies_count: 1,
    //             total_revenue: 1,
    //             total_budget: 1,
    //             PnL: { $subtract: ["$total_revenue", "$total_budget"] } 
    //         }
    //     },
    //     {
    //         $sort: { movies_count: -1 } 
    //     }
    // ])

    //

    public List<Document> getProlificDirectors() {
        
        GroupOperation groupByDirectors = Aggregation.group("director")
                .count().as("movies_count")
                .sum("revenue").as("total_revenue")
                .sum("budget").as("total_budget");
        
        ProjectionOperation projectDetails = Aggregation
                .project("_id","movies_count", "total_revenue", "total_budget")
                .and("_id").as("director_name")
                .andExpression("total_revenue - total_budget").as("PnL");

        SortOperation sortByMoviesCount = Aggregation.sort(Sort.by(Sort.Order.desc("movies_count")));

        Aggregation pipeline = Aggregation.newAggregation(groupByDirectors, projectDetails, sortByMoviesCount);

        AggregationResults<Document> results = mongoTemplate.aggregate(pipeline, "imdb", Document.class);
        return results.getMappedResults();
    }


}