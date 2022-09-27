package hiti.api;
public enum BanType {

    BAN("BAN", 0),
    TEMPBAN("TEMPBAN", 1),
    MUTE("MUTE", 2),
    TEMPMUTE("TEMPMUTE", 3);

    private BanType(final String s, final int i) {
    }
}