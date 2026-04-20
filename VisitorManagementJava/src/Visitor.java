import java.sql.Timestamp;

/** Simple model class for a visitor row. */
public class Visitor {
    private int id;
    private String name;
    private String phone;
    private String whomToVisit;
    private String purpose;
    private Timestamp checkInTime;
    private Timestamp checkOutTime;
    private String status;

    public Visitor(int id, String name, String phone, String whomToVisit, String purpose,
                   Timestamp checkInTime, Timestamp checkOutTime, String status) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.whomToVisit = whomToVisit;
        this.purpose = purpose;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.status = status;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getWhomToVisit() { return whomToVisit; }
    public String getPurpose() { return purpose; }
    public Timestamp getCheckInTime() { return checkInTime; }
    public Timestamp getCheckOutTime() { return checkOutTime; }
    public String getStatus() { return status; }
}
