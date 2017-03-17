package londonmet.aladdin.hourstracker;

/**
 * Created by Aladdin on 20/05/2015.
 */
public class Log {
    private int id;
    private String logStart;
    private String logEnd;
    private String logDate;
    private String logTotal;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogStart() {
        return logStart;
    }

    public void setLogStart(String logStart) {
        this.logStart = logStart;
    }

    public String getLogEnd() {
        return logEnd;
    }

    public void setLogEnd(String logEnd) {
        this.logEnd = logEnd;
    }

    public String getLogDate() {
        return logDate;
    }

    public void setLogDate(String logDate) {
        this.logDate = logDate;
    }

    public String getLogTotal() {
        return logTotal;
    }

    public void setLogTotal(String logTotal) {
        this.logTotal = logTotal;
    }

    public String toString() {
        return "Date " + logDate + " :start " + logStart + " - end " + logEnd + " total: " + getLogTotal() ;
    }
}
