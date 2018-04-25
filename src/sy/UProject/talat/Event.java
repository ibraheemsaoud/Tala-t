package sy.UProject.talat;

public class Event {
    // id INTEGER PRIMARY KEY AUTO_INCREMENT, headline LONGTEXT CHARACTER SET
    // utf8, des LONGTEXT CHARACTER SET utf8, host CHAR(50), EX1 LONGTEXT
    // CHARACTER SET utf8, EX2 CHAR(50), cost INTEGER, going INTEGER, fe
    // INTEGER, rank INTEGER, place TEXT, s_date CHAR(8), e_date CHAR(8), s_time
    // CHAR(5), e_time CHAR(5), c_date CHAR(15), section CHAR(2)
    String id, headline, desc, host, place;
    Integer hangging, cost, featured, rank, section, isHangging;
    long s_time, e_time, s_date, e_date;

    public Event(String id, String headline, String desc, String host,
                 int section, int hangging, long s_date, long e_date, long s_time,
                 long e_time) {
        this.id = id;
        this.headline = headline;
        this.desc = desc;
        this.host = host;
        this.section = section;
        this.hangging = hangging;
        this.s_date = s_date;
        this.e_date = e_date;
        this.s_time = s_time;
        this.e_time = e_time;
        this.place = "";
        this.cost = 0;
        this.featured = 0;
        this.rank = 0;
        this.isHangging = 0;
    }

    public int getCost() {
        return cost;
    }

    public String getDesc() {
        return desc;
    }

    public long getE_date() {
        return e_date;
    }

    public long getE_time() {
        return e_time;
    }

    public int getFeatured() {
        return featured;
    }

    public Integer getHangging() {
        return hangging;
    }

    public String getHeadline() {
        return headline;
    }

    public String getHost() {
        return host;
    }

    public String getId() {
        return id;
    }

    public String getPlace() {
        return place;
    }

    public int getRank() {
        return rank;
    }

    public long getS_date() {
        return s_date;
    }

    public long getS_time() {
        return s_time;
    }

    public int getSection() {
        return section;
    }

    public int getIsHangging() {
        return isHangging;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setE_date(long e_date) {
        this.e_date = e_date;
    }

    public void setE_time(long e_time) {
        this.e_time = e_time;
    }

    public void setFeatured(int featured) {
        this.featured = featured;
    }

    public void setHangging(int hangging) {
        this.hangging = hangging;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setS_date(long s_date) {
        this.s_date = s_date;
    }

    public void setS_time(long s_time) {
        this.s_time = s_time;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public void setIsHangging(int isHangging) {
        this.isHangging = isHangging;
    }
}
