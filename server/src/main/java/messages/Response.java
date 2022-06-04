package messages;

import java.io.Serializable;

public class Response implements Serializable {

    static final long serialVersionUID = 1L;

    private String answer;

    public Response(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
