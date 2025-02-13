package vttp.batch5.paf.movies.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import vttp.batch5.paf.movies.models.*;

@Repository
public class MySQLMovieRepository {

    public static final String SQL_BATCH_INSERT =
            "INSERT INTO imdb (imdb_id, vote_average, vote_count, release_date, revenue, budget, runtime) VALUES (?, ?, ?, ?, ?, ?, ?) " ;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void batchInsertMovies(List<MySQLmodel> movies) {
        jdbcTemplate.batchUpdate(SQL_BATCH_INSERT, movies, 25,
            (ps, movie) -> {
                ps.setString(1, movie.getImdb_id());
                ps.setDouble(2, movie.getVote_average());
                ps.setInt(3, movie.getVote_count());
                ps.setDate(4, new java.sql.Date(movie.getRelease_date().getTime()));
                ps.setDouble(5, movie.getRevenue());
                ps.setDouble(6, movie.getBudget());
                ps.setInt(7, movie.getRuntime());
            });
        System.out.println("Inserted batch of " + movies.size() + " movies into MySQL.");
    }
  

  // TODO: Task 3

}
