package Networking;

import java.util.Objects;

public class Pair <C1, C2>
{
    private C1 first;
    private C2 second;

    public Pair()
    {
        this.first = null;
        this.second = null;
    }

    public Pair(C1 c1, C2 c2)
    {
        this.first = c1;
        this.second = c2;
    }

    public C1 getFirst()
    {
        return first;
    }

    public void setFirst(C1 first)
    {
        this.first = first;
    }

    public C2 getSecond()
    {
        return second;
    }

    public void setSecond(C2 second)
    {
        this.second = second;
    }

    public Object find(Object o)
    {
        if(o.equals(first))
            return first;
        else
            return second;
    }

    public void swapPair()
    {
        C1 temp = first;
        first = (C1)getSecond();
        second = (C2)temp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
