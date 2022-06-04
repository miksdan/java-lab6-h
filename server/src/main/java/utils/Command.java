package utils;

import messages.Request;
import messages.Response;

/**
 * executes commands
 */
@FunctionalInterface
public interface Command {
    Response execute(Request request);
}
