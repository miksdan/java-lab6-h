package messages;

import model.Movie;

import java.io.Serializable;

public class Request implements Serializable {

    static final long serialVersionUID = 2L;

    private String command;
    private String argument;
    private Movie movie;

    public Request(String command, String argument, Movie movie) {
        this.command = command;
        this.argument = argument;
        this.movie = movie;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
