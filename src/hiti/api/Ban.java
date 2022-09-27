package hiti.api;

public class Ban {

    private long expire;
    private String playerName;
    private String reason;
    private long time;
    private long timestart;
    private String comment;
    private String admin;
    private BanType type;

    public Ban(final String playerName, final String admin, final String reason, final long time, final String comment, final BanType type, final long expire) {
        this.playerName = playerName;
        this.admin = admin;
        this.reason = reason;
        this.comment = comment;
        this.type = type;
        this.time = time;
        this.timestart = System.currentTimeMillis();
        this.expire = expire;
    }

    public long getTime() {
        return this.time;
    }

    public long getExpire() {
        return this.expire;
    }

    public BanType getType() {
        return this.type;
    }

    public String getAdmin() {
        return this.admin;
    }

    public String getComment() {
        return this.comment;
    }

    public String getName() {
        return this.playerName;
    }

    public String getReason() {
        return this.reason;
    }

    public boolean isLeft() {
        return this.getTimeLeft() < 0L;
    }

    public long getTimeLeft() {
        return this.time - System.currentTimeMillis();
    }
}

