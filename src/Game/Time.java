package Game;

public class Time {
    Time() {}

    public void Wait(double seconds)
    {
        double end = System.currentTimeMillis() / 1000 + seconds;
        while (System.currentTimeMillis() < end) {}
        return;
    }
}
