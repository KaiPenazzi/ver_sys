package controller;

import java.util.List;

public class configNode
{
    private final String ip;
    private final int storageVal;
    private final List<String> neigh;

    public configNode(String ip, int storageVal, List<String> neigh)
    {
        this.ip = ip;
        this.storageVal = storageVal;
        this.neigh = neigh;
    }

    public String getIp() {
        return ip;
    }

    public int getStorageVal() {
        return storageVal;
    }

    public List<String> getNeigh() {
        return neigh;
    }
}
