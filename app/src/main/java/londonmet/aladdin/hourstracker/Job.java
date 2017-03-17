package londonmet.aladdin.hourstracker;

/**
 * Created by Aladdin on 20/05/2015.
 */
public class Job {
    private int id;
    private String jobName;
    private String jobRate;
    private String jobPayment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobRate() {
        return jobRate;
    }

    public void setJobRate(String jobRate) {
        this.jobRate = jobRate;
    }

    public String getJobPayment() {
        return jobPayment;
    }

    public void setJobPayment(String jobPayment) {
        this.jobPayment = jobPayment;
    }

    public String toString() {
        return jobName;
    }
}
