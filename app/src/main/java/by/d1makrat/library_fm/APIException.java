package by.d1makrat.library_fm;

public class APIException extends Exception {

    private String msg;

    APIException(){}

    APIException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public String getMessage(){
        switch (msg) {
            case "1":
                msg = "Artist was ignored";
                break;
            case "2":
                msg = "Track was ignored";
                break;
            case "3":
                msg = "Timestamp was too old";
                break;
            case "4":
                msg = "Timestamp was too new";
                break;
            case "5":
                msg = "Daily scrobble limit exceeded";
                break;
        }
        return msg;
    }
}
